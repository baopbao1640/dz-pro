import request from '@/utils/request';
import type { SystemStatus } from './types';
import { unwrapData } from './types';

export type DataScope = '1' | '2' | '3' | '4' | '5';

export interface RoleQueryDTO {
  roleName?: string;
  roleKey?: string;
  status?: SystemStatus;
}

export interface RoleSaveDTO {
  roleName: string;
  roleKey: string;
  roleSort: number;
  dataScope: DataScope;
  menuCheckStrictly: 'Y' | 'N';
  deptCheckStrictly: 'Y' | 'N';
  status: SystemStatus;
  remark?: string;
}

export interface RoleListVO {
  id: string;
  roleName: string;
  roleKey: string;
  roleSort: number;
  dataScope: DataScope;
  dataScopeLabel?: string;
  status: SystemStatus;
  createTime?: string;
}

export interface RoleDetailVO extends RoleListVO {
  menuCheckStrictly: 'Y' | 'N';
  deptCheckStrictly: 'Y' | 'N';
  menuIds?: string[];
  deptIds?: string[];
  assignedUserCount?: number;
  remark?: string;
}

export function listRoles(params?: RoleQueryDTO): Promise<RoleListVO[]> {
  return request.get('/system/roles', { params }).then(unwrapData<RoleListVO[]>);
}

export function getRole(id: string): Promise<RoleDetailVO> {
  return request.get(`/system/roles/${id}`).then(unwrapData<RoleDetailVO>);
}

export function createRole(data: RoleSaveDTO): Promise<RoleDetailVO> {
  return request.post('/system/roles', data).then(unwrapData<RoleDetailVO>);
}

export function updateRole(id: string, data: RoleSaveDTO): Promise<RoleDetailVO> {
  return request.put(`/system/roles/${id}`, data).then(unwrapData<RoleDetailVO>);
}

export function changeRoleStatus(id: string, status: SystemStatus): Promise<void> {
  return request.patch(`/system/roles/${id}/status`, { status }).then(unwrapData<void>);
}

export function assignRoleMenus(
  id: string,
  menuIds: string[],
  menuCheckStrictly: 'Y' | 'N',
): Promise<void> {
  return request.put(`/system/roles/${id}/menus`, { menuIds, menuCheckStrictly }).then(unwrapData<void>);
}
