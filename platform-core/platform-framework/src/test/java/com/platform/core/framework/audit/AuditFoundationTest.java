package com.platform.core.framework.audit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

class AuditFoundationTest {

  @Test
  void auditActionsKeepStableDatabaseCodes() {
    assertThat(AuditAction.CREATE.getCode()).isEqualTo("create");
    assertThat(AuditAction.UPDATE.getCode()).isEqualTo("update");
    assertThat(AuditAction.DELETE.getCode()).isEqualTo("delete");
    assertThat(AuditAction.ENABLE.getCode()).isEqualTo("enable");
    assertThat(AuditAction.DISABLE.getCode()).isEqualTo("disable");
    assertThat(AuditAction.ASSIGN.getCode()).isEqualTo("assign");
    assertThat(AuditAction.REVOKE.getCode()).isEqualTo("revoke");
  }

  @Test
  void sanitizerMasksSensitiveKeysBeforeEventsArePublished() {
    String sanitized =
        AuditSanitizer.sanitize(
            Map.of(
                "userName", "admin",
                "password", "secret",
                "accessToken", "token-value",
                "mfaCode", "123456",
                "nested", Map.of("refreshToken", "refresh-value", "role", "admin")));

    assertThat(sanitized).contains("userName=admin");
    assertThat(sanitized).contains("password=******");
    assertThat(sanitized).contains("accessToken=******");
    assertThat(sanitized).contains("mfaCode=******");
    assertThat(sanitized).contains("refreshToken=******");
    assertThat(sanitized).doesNotContain("secret", "token-value", "123456", "refresh-value");
  }

  @Test
  void publisherDoesNotBlockCallerThread() throws InterruptedException {
    CountDownLatch handlerStarted = new CountDownLatch(1);
    CountDownLatch releaseHandler = new CountDownLatch(1);
    AuditEventHandler blockingHandler =
        event -> {
          handlerStarted.countDown();
          releaseHandler.await(5, TimeUnit.SECONDS);
        };
    AsyncAuditEventPublisher publisher =
        new AsyncAuditEventPublisher(new SimpleAsyncTaskExecutor("audit-test-"), blockingHandler);
    AuditEvent event =
        new AuditEvent(
            AuditAction.CREATE,
            "User",
            "0",
            OffsetDateTime.parse("2026-06-08T14:40:00+08:00"),
            "trace-1",
            "1",
            null,
            null);

    long startedAt = System.nanoTime();
    publisher.publish(event);
    long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);

    assertThat(elapsedMs).isLessThan(200);
    assertThat(handlerStarted.await(1, TimeUnit.SECONDS)).isTrue();
    releaseHandler.countDown();
  }
}
