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
