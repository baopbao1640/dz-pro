import request from '@/utils/request';

export interface AuthProfile {
  userId: number;
  keycloakUserId: string;
  userName: string;
  deptId?: number;
  roleIds: number[];
  roleKeys: string[];
  permissions: string[];
  dataScope: string;
  superAdmin: boolean;
}

export interface DynamicRouteMeta {
  title?: string;
  icon?: string;
  hidden?: boolean;
  keepAlive?: boolean;
}

export interface DynamicRoute {
  name?: string;
  path: string;
  component?: string;
  redirect?: string;
  meta?: DynamicRouteMeta;
  children?: DynamicRoute[];
}

interface ApiResult<T> {
  code: number;
  message: string;
  data: T;
}

export async function getAuthProfile(): Promise<AuthProfile> {
  const { data } = await request.get<ApiResult<AuthProfile>>('/system/auth/profile');
  return data.data;
}

export async function getAuthRoutes(): Promise<DynamicRoute[]> {
  const { data } = await request.get<ApiResult<DynamicRoute[]>>('/system/auth/routes');
  return data.data;
}

export async function getAuthPermissions(): Promise<string[]> {
  const { data } = await request.get<ApiResult<string[]>>('/system/auth/permissions');
  return data.data;
}
