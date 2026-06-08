import request from '@/utils/request';
import type { SystemStatus, TreeNode } from './types';
import { unwrapData } from './types';

export interface DeptTreeQueryDTO {
  deptName?: string;
  status?: SystemStatus;
}

export interface DeptSaveDTO {
  parentId: string;
  deptName: string;
  orderNum: number;
  leaderUserId?: string;
  phone?: string;
  email?: string;
  status: SystemStatus;
  remark?: string;
}

export interface DeptTreeVO extends TreeNode {
  ancestors?: string;
  deptName: string;
  orderNum: number;
  leaderUserId?: string;
  leaderName?: string;
  phone?: string;
  email?: string;
  children?: DeptTreeVO[];
}

export function listDeptTree(params?: DeptTreeQueryDTO): Promise<DeptTreeVO[]> {
  return request.get('/system/depts/tree', { params }).then(unwrapData<DeptTreeVO[]>);
}

export function getDept(id: string): Promise<DeptTreeVO> {
  return request.get(`/system/depts/${id}`).then(unwrapData<DeptTreeVO>);
}

export function createDept(data: DeptSaveDTO): Promise<DeptTreeVO> {
  return request.post('/system/depts', data).then(unwrapData<DeptTreeVO>);
}

export function updateDept(id: string, data: DeptSaveDTO): Promise<DeptTreeVO> {
  return request.put(`/system/depts/${id}`, data).then(unwrapData<DeptTreeVO>);
}

export function changeDeptStatus(id: string, status: SystemStatus, reason?: string): Promise<void> {
  return request.patch(`/system/depts/${id}/status`, { status, reason }).then(unwrapData<void>);
}
