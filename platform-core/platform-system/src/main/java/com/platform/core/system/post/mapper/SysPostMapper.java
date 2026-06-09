package com.platform.core.system.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.core.system.post.domain.SysPost;
import com.platform.core.system.post.dto.PostPageQueryDTO;
import com.platform.core.system.post.vo.PostListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
