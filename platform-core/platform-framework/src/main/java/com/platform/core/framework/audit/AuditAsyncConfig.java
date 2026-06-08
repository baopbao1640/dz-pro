package com.platform.core.framework.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 审计异步线程池属于 framework 基础设施。容量刻意保守，是为了避免日志写入高峰抢占业务线程； 后续如果接入持久化和监控，应基于吞吐量再调整线程数与队列长度。 */
@Configuration
@EnableAsync
public class AuditAsyncConfig {

  public static final String AUDIT_TASK_EXECUTOR_BEAN = "auditTaskExecutor";

  @Bean(name = AUDIT_TASK_EXECUTOR_BEAN)
  public TaskExecutor auditTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("audit-");
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(1000);
    executor.initialize();
    return executor;
  }
}
