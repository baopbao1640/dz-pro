package com.platform.core.framework.audit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 审计脱敏放在 framework，是为了让所有日志入口先共享同一条安全底线。Keycloak token、 session、MFA 和 credential
 * 相关字段不得进入审计明文，避免日志系统扩大敏感数据暴露面。
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
