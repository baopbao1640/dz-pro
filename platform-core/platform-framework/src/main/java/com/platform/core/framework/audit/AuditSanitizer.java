package com.platform.core.framework.audit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 审计脱敏工具，负责在事件进入持久化前屏蔽敏感字段并限制文本长度。
 *
 * <p>职责：统一处理 token、session、MFA、secret、credential 等敏感键。
 *
 * <p>边界：只做启发式字段名脱敏，不解析复杂对象语义。
 *
 * <p>当前阶段能力：支持 Map 参数递归脱敏和文本截断。
 */
public final class AuditSanitizer {

  private static final String MASK = "******";
  private static final int MAX_TEXT_LENGTH = 4000;
  private static final Set<String> SENSITIVE_KEY_PARTS =
      Set.of("password", "token", "session", "mfa", "secret", "credential", "authorization");

  private AuditSanitizer() {}

  public static String sanitize(Map<String, ?> values) {
    if (values == null || values.isEmpty()) {
      return null;
    }
    return truncate(sanitizeMap(values).toString());
  }

  public static String sanitizeText(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return truncate(value);
  }

  private static Map<String, Object> sanitizeMap(Map<String, ?> values) {
    Map<String, Object> sanitized = new LinkedHashMap<>();
    values.forEach((key, value) -> sanitized.put(key, sanitizeValue(key, value)));
    return sanitized;
  }

  private static Object sanitizeValue(String key, Object value) {
    if (isSensitiveKey(key)) {
      return MASK;
    }
    if (value instanceof Map<?, ?> nestedMap) {
      Map<String, Object> nested = new LinkedHashMap<>();
      nestedMap.forEach(
          (nestedKey, nestedValue) ->
              nested.put(
                  String.valueOf(nestedKey),
                  sanitizeValue(String.valueOf(nestedKey), nestedValue)));
      return nested;
    }
    if (value instanceof CharSequence text) {
      return truncate(text.toString());
    }
    return value;
  }

  private static boolean isSensitiveKey(String key) {
    String normalized = key == null ? "" : key.toLowerCase();
    return SENSITIVE_KEY_PARTS.stream().anyMatch(normalized::contains);
  }

  private static String truncate(String value) {
    if (value == null || value.length() <= MAX_TEXT_LENGTH) {
      return value;
    }
    return value.substring(0, MAX_TEXT_LENGTH);
  }
}
