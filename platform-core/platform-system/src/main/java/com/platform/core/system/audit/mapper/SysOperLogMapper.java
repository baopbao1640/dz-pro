package com.platform.core.system.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.audit.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作审计日志 Mapper，负责 `sys_oper_log` 的基础持久化。
 *
 * <p>职责：提供审计落库入口，供 `SystemAuditEventHandler` 写入操作日志。
 *
 * <p>边界：不承载审计脱敏、异步执行或当前用户解析。
 *
 * <p>当前阶段能力：复用 MyBatis Plus `BaseMapper` 完成插入。
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {}
