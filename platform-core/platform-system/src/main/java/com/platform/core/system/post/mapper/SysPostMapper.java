package com.platform.core.system.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.core.system.post.domain.SysPost;
import com.platform.core.system.post.dto.PostPageQueryDTO;
import com.platform.core.system.post.vo.PostListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 岗位 Mapper，封装 `sys_post` 查询、分页计数和占用检查。
 *
 * <p>职责：提供岗位分页、详情、编码唯一性和用户占用查询。
 *
 * <p>边界：不执行业务状态流转，不处理用户岗位关系写入。
 *
 * <p>当前阶段能力：支撑岗位管理 CRUD 和分页 total 正确返回。
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

  Page<PostListVO> selectPostPage(Page<PostListVO> page, @Param("query") PostPageQueryDTO query);

  long countPostPage(@Param("query") PostPageQueryDTO query);

  PostListVO selectPostDetail(@Param("id") Long id);

  SysPost selectActiveById(@Param("id") Long id);

  long countActiveByPostCode(
      @Param("postCode") String postCode, @Param("excludeId") Long excludeId);

  long countActiveUsersByPostId(@Param("id") Long id);
}
