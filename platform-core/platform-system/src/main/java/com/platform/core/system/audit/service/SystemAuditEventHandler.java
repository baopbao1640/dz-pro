package com.platform.core.system.audit.service;

import com.platform.core.framework.audit.AuditEvent;
import com.platform.core.framework.audit.AuditEventHandler;
import com.platform.core.framework.security.context.CurrentUserProvider;
import com.platform.core.system.audit.domain.SysOperLog;
import com.platform.core.system.audit.mapper.SysOperLogMapper;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/** 审计落库放在 system 模块，是为了保持 framework 只发布事件、不依赖日志表。当前实现只写操作日志；登录日志后续可复用同一事件管道扩展。 */
@Primary
@Service
public class SystemAuditEventHandler implements AuditEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemAuditEventHandler.class);

  private final SysOperLogMapper operLogMapper;
  private final CurrentUserProvider currentUserProvider;

  public SystemAuditEventHandler(
      SysOperLogMapper operLogMapper, CurrentUserProvider currentUserProvider) {
    this.operLogMapper = operLogMapper;
    this.currentUserProvider = currentUserProvider;
  }

  @Override
  public void handle(AuditEvent event) {
    try {
      SysOperLog log = new SysOperLog();
      currentUserProvider
          .getCurrentUser()
          .ifPresent(
              user -> {
                log.setCreateBy(String.valueOf(user.userId()));
                log.setOperUserId(user.userId());
                log.setOperName(user.userName());
                log.setDeptId(user.deptId());
              });
      log.setCreateTime(OffsetDateTime.now());
      log.setTraceId(event.traceId());
      log.setModuleTitle(event.moduleTitle());
      log.setBusinessType(event.action().getCode());
      log.setOperParam(event.operParam());
      log.setJsonResult(event.jsonResult());
      log.setStatus(event.status());
      log.setOperTime(event.operTime());
      operLogMapper.insert(log);
    } catch (RuntimeException ex) {
      LOGGER.warn("Audit persistence failed and was ignored: {}", event.action(), ex);
    }
  }
}
