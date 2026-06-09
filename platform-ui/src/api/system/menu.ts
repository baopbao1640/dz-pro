import request from '@/utils/request';
import type { SystemStatus, TreeNode } from './types';
import { unwrapData } from './types';

export type MenuType = 'M' | 'C' | 'F';

export interface MenuQueryDTO {
  menuName?: string;
  menuType?: MenuType;
  permissionCode?: string;
  status?: SystemStatus;
}

export interface MenuSaveDTO {
  parentId: string;
  menuName: string;
  orderNum: number;
  path?: string;
  component?: string;
  queryParam?: string;
  routeName?: string;
  isFrame: 'Y' | 'N';
  isCache: 'Y' | 'N';
  menuType: MenuType;
  visible: 'Y' | 'N';
  status: SystemStatus;
  permissionCode?: string;
  icon?: string;
  remark?: string;
}

export interface MenuTreeVO extends TreeNode {
  menuName: string;
  menuType: MenuType;
  orderNum: number;
  path?: string;
  component?: string;
  routeName?: string;
  permissionCode?: string;
  icon?: string;
  visible: 'Y' | 'N';
  isCache: 'Y' | 'N';
  isFrame: 'Y' | 'N';
  children?: MenuTreeVO[];
}

export interface RouteVO {
  name: string;
  path: string;
  component?: string;
  redirect?: string;
  meta?: {
    title?: string;
    icon?: string;
    hidden?: boolean;
    keepAlive?: boolean;
  };
  children?: RouteVO[];
}

export function listCurrentRoutes(): Promise<RouteVO[]> {
  return request.get('/system/menus/routes').then(unwrapData<RouteVO[]>);
}

export function listCurrentPermissions(): Promise<string[]> {
  return request.get('/system/menus/permissions').then(unwrapData<string[]>);
}

export function listMenuTree(params?: MenuQueryDTO): Promise<MenuTreeVO[]> {
  return request.get('/system/menus/tree', { params }).then(unwrapData<MenuTreeVO[]>);
}

export function getMenu(id: string): Promise<MenuTreeVO> {
  return request.get(`/system/menus/${id}`).then(unwrapData<MenuTreeVO>);
}

export function createMenu(data: MenuSaveDTO): Promise<MenuTreeVO> {
  return request.post('/system/menus', data).then(unwrapData<MenuTreeVO>);
}

export function updateMenu(id: string, data: MenuSaveDTO): Promise<MenuTreeVO> {
  return request.put(`/system/menus/${id}`, data).then(unwrapData<MenuTreeVO>);
}

export function changeMenuStatus(id: string, status: SystemStatus): Promise<void> {
  return request.patch(`/system/menus/${id}/status`, { status }).then(unwrapData<void>);
}
