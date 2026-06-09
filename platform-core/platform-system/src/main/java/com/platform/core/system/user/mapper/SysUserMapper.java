package com.platform.core.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.core.system.user.domain.SysUser;
import com.platform.core.system.user.dto.UserPageQueryDTO;
import com.platform.core.system.user.dto.UserRelationRow;
import com.platform.core.system.user.vo.UserDetailVO;
import com.platform.core.system.user.vo.UserListVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户管理 Mapper，封装 `sys_user` 及用户角色、岗位关系的查询和写入。
 *
 * <p>职责：提供分页、详情、Keycloak subject 映射和关系表维护 SQL。
 *
 * <p>边界：不执行业务校验，不判断权限，不处理 token。
 *
 * <p>当前阶段能力：为用户分页 total、当前用户映射和授权关系维护提供持久化入口。
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

  Page<UserListVO> selectUserPage(Page<UserListVO> page, @Param("query") UserPageQueryDTO query);

  long countUserPage(@Param("query") UserPageQueryDTO query);

  UserDetailVO selectUserDetail(@Param("id") Long id);

  SysUser selectActiveByKeycloakUserId(@Param("keycloakUserId") String keycloakUserId);

  List<Long> selectRoleIdsByUserId(@Param("id") Long id);

  List<Long> selectPostIdsByUserId(@Param("id") Long id);

  Long countActiveByKeycloakUserId(
      @Param("keycloakUserId") String keycloakUserId, @Param("excludeId") Long excludeId);

  void deleteRolesByUserId(@Param("id") Long id);

  void deletePostsByUserId(@Param("id") Long id);

  void insertUserRoles(@Param("id") Long id, @Param("relations") List<UserRelationRow> relations);

  void insertUserPosts(@Param("id") Long id, @Param("relations") List<UserRelationRow> relations);
}
