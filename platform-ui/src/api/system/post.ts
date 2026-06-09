import request from '@/utils/request';
import type { PageQuery, PageResult, SystemStatus } from './types';
import { unwrapData } from './types';

export interface PostPageQueryDTO extends PageQuery {
  postCode?: string;
  postName?: string;
  status?: SystemStatus;
}

export interface PostSaveDTO {
  postCode: string;
  postName: string;
  postSort: number;
  status: SystemStatus;
  remark?: string;
}

export interface PostListVO {
  id: string;
  postCode: string;
  postName: string;
  postSort: number;
  status: SystemStatus;
  boundUserCount?: number;
  createTime?: string;
}

export function listPosts(params: PostPageQueryDTO): Promise<PageResult<PostListVO>> {
  return request.get('/system/posts', { params }).then(unwrapData<PageResult<PostListVO>>);
}

export function getPost(id: string): Promise<PostListVO> {
  return request.get(`/system/posts/${id}`).then(unwrapData<PostListVO>);
}

export function createPost(data: PostSaveDTO): Promise<PostListVO> {
  return request.post('/system/posts', data).then(unwrapData<PostListVO>);
}

export function updatePost(id: string, data: PostSaveDTO): Promise<PostListVO> {
  return request.put(`/system/posts/${id}`, data).then(unwrapData<PostListVO>);
}

export function changePostStatus(id: string, status: SystemStatus, reason?: string): Promise<void> {
  return request.patch(`/system/posts/${id}/status`, { status, reason }).then(unwrapData<void>);
}
