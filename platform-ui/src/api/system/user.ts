import request from '@/utils/request';
import type { PageQuery, PageResult, SystemStatus } from './types';
import { unwrapData } from './types';

export interface UserPageQueryDTO extends PageQuery {
  deptId?: string;
  includeChildren?: boolean;
  userName?: string;
  nickName?: string;
  phoneNumber?: string;
  status?: SystemStatus;
  createdStartTime?: string;
  createdEndTime?: string;
}

export interface UserSaveDTO {
  keycloakUserId?: string;
  userName: string;
  nickName: string;
  deptId?: string;
  postIds?: string[];
  roleIds?: string[];
  email?: string;
  phoneNumber?: string;
  sex?: '0' | '1' | '2';
  status: SystemStatus;
  remark?: string;
}

export interface UserListVO {
  id: string;
  keycloakUserId: string;
  userName: string;
  nickName: string;
  deptId?: string;
  deptName?: string;
  postNames?: string[];
  roleNames?: string[];
  email?: string;
  phoneNumber?: string;
  sex?: '0' | '1' | '2';
  status: SystemStatus;
  lastSyncTime?: string;
  createTime?: string;
}

export interface UserDetailVO extends UserListVO {
  roleIds?: string[];
  postIds?: string[];
  dataScopeSummary?: string;
  permissionCodes?: string[];
  menuIds?: string[];
}

export function listUsers(params: UserPageQueryDTO): Promise<PageResult<UserListVO>> {
  return request.get('/system/users', { params }).then(unwrapData<PageResult<UserListVO>>);
}

export function getUser(id: string): Promise<UserDetailVO> {
  return request.get(`/system/users/${id}`).then(unwrapData<UserDetailVO>);
}

export function createUser(data: UserSaveDTO): Promise<UserDetailVO> {
  return request.post('/system/users', data).then(unwrapData<UserDetailVO>);
}

export function updateUser(id: string, data: UserSaveDTO): Promise<UserDetailVO> {
  return request.put(`/system/users/${id}`, data).then(unwrapData<UserDetailVO>);
}

export function changeUserStatus(id: string, status: SystemStatus, reason?: string): Promise<void> {
  return request.patch(`/system/users/${id}/status`, { status, reason }).then(unwrapData<void>);
}

export function assignUserPosts(id: string, postIds: string[]): Promise<void> {
  return request.put(`/system/users/${id}/posts`, { postIds }).then(unwrapData<void>);
}

export function assignUserRoles(id: string, roleIds: string[]): Promise<void> {
  return request.put(`/system/users/${id}/roles`, { roleIds }).then(unwrapData<void>);
}
