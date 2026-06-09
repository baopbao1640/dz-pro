#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if command -v python3 >/dev/null 2>&1; then
  PYTHON_BIN=(python3)
elif command -v python >/dev/null 2>&1; then
  PYTHON_BIN=(python)
elif command -v py >/dev/null 2>&1; then
  PYTHON_BIN=(py -3)
else
  echo "Comment governance check failed: python3/python/py is required." >&2
  exit 1
fi

"${PYTHON_BIN[@]}" - <<'PY'
from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path.cwd()

JAVA_ROOTS = [
    ROOT / "platform-core/platform-framework/src/main/java/com/platform/core/framework/config",
    ROOT / "platform-core/platform-framework/src/main/java/com/platform/core/framework/security",
    ROOT / "platform-core/platform-framework/src/main/java/com/platform/core/framework/audit",
    ROOT / "platform-core/platform-framework/src/main/java/com/platform/core/framework/web",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/auth",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/audit",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/user",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/role",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/menu",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/dept",
    ROOT / "platform-core/platform-system/src/main/java/com/platform/core/system/post",
]

FRONTEND_ROOTS = [
    ROOT / "platform-ui/src/router",
    ROOT / "platform-ui/src/permissions",
    ROOT / "platform-ui/src/store",
    ROOT / "platform-ui/src/api/system",
    ROOT / "platform-ui/src/views/system",
]

CLASS_KEYWORDS = (
    "Controller",
    "Service",
    "ServiceImpl",
    "Config",
    "Aspect",
    "Annotation",
    "Provider",
    "Handler",
    "Interceptor",
    "Resolver",
    "Context",
    "Route",
    "Audit",
    "Security",
    "DataScope",
    "Permission",
    "Mapper",
)

METHOD_PATH_KEYWORDS = (
    "controller",
    "service",
    "authz",
    "audit",
    "security",
    "route",
    "web",
)

FRAMEWORK_BOUNDARY_FILES = {
    "SecurityConfig.java",
    "GlobalExceptionHandler.java",
    "RestAuthenticationEntryPoint.java",
    "RestAccessDeniedHandler.java",
    "SecurityJsonResponseWriter.java",
    "RequiresPermissionAspect.java",
    "CurrentUser.java",
    "CurrentUserProvider.java",
    "AuditLogAspect.java",
    "AuditEvent.java",
    "AuditEventPublisher.java",
    "DataScope.java",
    "DataScopeCondition.java",
    "DataScopeConditionProvider.java",
    "DynamicRouteService.java",
    "RouteDefinition.java",
    "RouteMeta.java",
}

SKIP_CLASS_SUFFIXES = ("DTO", "VO", "Entity")
LOW_VALUE_COMMENT = re.compile(r"//\s*(设置|返回|调用|查询|保存|删除|更新|新增|获取|遍历|组装|构建|校验|判断)(值|结果|方法|数据|对象|列表|参数)?\s*$")
ISOLATED_TODO = re.compile(r"(?<!Deferred: )(?<!Risk: )(?<!Debt: )(?<!Extension: )\bTODO\b", re.IGNORECASE)
CLASS_DECL = re.compile(r"\b(public|private|protected)?\s*(?:final\s+|abstract\s+)?(class|interface|enum|record|@interface)\s+([A-Za-z_][A-Za-z0-9_]*)")
PUBLIC_METHOD = re.compile(r"^\s*public\s+(?!class\b|interface\b|enum\b|record\b|@interface\b)(?:[\w<>\[\], ?]+\s+)+([a-zA-Z_][a-zA-Z0-9_]*)\s*\(")

violations: list[str] = []


def rel(path: Path) -> str:
    return str(path.relative_to(ROOT)).replace("\\", "/")


def java_files() -> list[Path]:
    files: list[Path] = []
    for root in JAVA_ROOTS:
        if root.exists():
            files.extend(root.rglob("*.java"))
    return sorted(set(files))


def frontend_files() -> list[Path]:
    files: list[Path] = []
    for root in FRONTEND_ROOTS:
        if root.exists():
            files.extend(p for p in root.rglob("*") if p.suffix in {".ts", ".vue"})
    return sorted(set(files))


def has_javadoc_before(text: str, start: int) -> bool:
    prefix = text[:start].rstrip()
    lines = prefix.splitlines()
    while lines and (not lines[-1].strip() or lines[-1].strip().startswith("@")):
        lines.pop()
    prefix = "\n".join(lines).rstrip()
    return prefix.endswith("*/") and "/**" in prefix[prefix.rfind("/**") :]


def class_requires_javadoc(path: Path, class_name: str, kind: str) -> bool:
    if class_name.endswith(SKIP_CLASS_SUFFIXES):
        return False
    if kind == "@interface":
        return True
    if "mapper" in rel(path).lower():
        return True
    return any(keyword in class_name for keyword in CLASS_KEYWORDS)


def method_requires_javadoc(path: Path, method_name: str) -> bool:
    if method_name in {"getCode", "setCode", "toString", "equals", "hashCode"}:
        return False
    if method_name.startswith(("get", "set", "is")):
        return False
    path_text = rel(path).lower()
    if any(keyword in path_text for keyword in METHOD_PATH_KEYWORDS):
        return True
    return any(keyword in path.name for keyword in ("Controller", "Service", "Provider", "Handler", "Aspect"))


def check_java(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    path_rel = rel(path)
    for match in CLASS_DECL.finditer(text):
        visibility = match.group(1)
        kind = match.group(2)
        class_name = match.group(3)
        if visibility == "private":
            continue
        if class_requires_javadoc(path, class_name, kind) and not has_javadoc_before(text, match.start()):
            violations.append(f"{path_rel}: {class_name} 缺少中文类头 Javadoc")
    for match in PUBLIC_METHOD.finditer(text):
        method_name = match.group(1)
        if method_requires_javadoc(path, method_name) and not has_javadoc_before(text, match.start()):
            violations.append(f"{path_rel}: public 方法 {method_name} 缺少 Javadoc")
    if path.name in FRAMEWORK_BOUNDARY_FILES:
        missing = [label for label in ("Boundary:", "Deferred:", "Risk:") if label not in text]
        if missing:
            violations.append(f"{path_rel}: framework 关键类缺少 {'/'.join(missing)} 说明")
    for line_no, line in enumerate(text.splitlines(), 1):
        if ISOLATED_TODO.search(line):
            violations.append(f"{path_rel}:{line_no}: 禁止孤立 TODO，请改为 Deferred/Risk/Debt/Extension")
        if LOW_VALUE_COMMENT.search(line):
            violations.append(f"{path_rel}:{line_no}: 明显低价值中文注释")


def check_frontend(path: Path) -> None:
    text = path.read_text(encoding="utf-8")
    path_rel = rel(path)
    for line_no, line in enumerate(text.splitlines(), 1):
        if ISOLATED_TODO.search(line):
            violations.append(f"{path_rel}:{line_no}: 禁止孤立 TODO，请改为 Deferred/Risk/Debt/Extension")
        if LOW_VALUE_COMMENT.search(line):
            violations.append(f"{path_rel}:{line_no}: 明显低价值中文注释")


for file_path in java_files():
    check_java(file_path)

for file_path in frontend_files():
    check_frontend(file_path)

if violations:
    print("Comment governance check failed:")
    for item in violations:
        print(f"- {item}")
    sys.exit(1)

print("Comment governance check passed.")
PY
