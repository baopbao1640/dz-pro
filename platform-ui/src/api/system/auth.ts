import request from '@/utils/request';

/*
 * Boundary:
 * 认证授权 API client 只消费后端 `/api/system/auth/**` 查询接口，不参与登录跳转、token 签发或权限判定。
 */
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

/*
 * Risk:
 * 这里按 Phase 3B 统一 envelope 直接读取 `data.data`；如果后端错误响应结构调整，需要同步 request 拦截器和本类型。
 */
export async function getAuthProfile(): Promise<AuthProfile> {
  const { data } = await request.get<ApiResult<AuthProfile>>('/system/auth/profile');
  return data.data;
}

/*
 * Deferred:
 * 动态路由结果当前尚未在本文件做组件白名单校验，后续接入全量动态注入前必须补齐。
 */
export async function getAuthRoutes(): Promise<DynamicRoute[]> {
  const { data } = await request.get<ApiResult<DynamicRoute[]>>('/system/auth/routes');
  return data.data;
}

export async function getAuthPermissions(): Promise<string[]> {
  const { data } = await request.get<ApiResult<string[]>>('/system/auth/permissions');
  return data.data;
}
