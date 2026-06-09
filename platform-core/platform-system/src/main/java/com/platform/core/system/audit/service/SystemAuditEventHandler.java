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

/**
 * system 审计事件处理器，负责把 framework 审计事件落库为 `sys_oper_log`。
 *
 * <p>职责：补充当前用户信息、转换审计字段并调用 Mapper 写入操作日志。
 *
 * <p>边界：不阻塞业务主流程，不处理登录日志，不改变业务事务提交结果。
 *
 * <p>当前阶段能力：支持操作审计异步落库，失败只记录 warn。
 */
/*
 * Boundary:
 * framework 只发布事件，system 负责持久化到业务日志表。
 */
/*
 * Deferred:
 * 登录日志、IP、user-agent、traceId 生成和失败监控暂未完整接入。
 */
/*
 * Risk:
 * 审计失败被降级处理，当前不能作为强一致审计凭证。
 */
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

  /**
   * 处理并持久化审计事件。
   *
   * <p>关键规则：当前用户可用时补充操作者信息；用户不可用时仍写入事件本身。
   *
   * <p>异常行为：持久化失败被捕获并记录 warn，避免影响业务主流程。
   */
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
