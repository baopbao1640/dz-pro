export interface ApiEnvelope<T> {
  code: string;
  message: string;
  data: T;
  traceId?: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  pageNum: number;
  pageSize: number;
}

export interface PageQuery {
  pageNum: number;
  pageSize: number;
}

export interface StatusChangeDTO {
  status: SystemStatus;
  reason?: string;
}

export type SystemStatus = '0' | '1';

export interface TreeNode {
  id: string;
  parentId: string;
  status: SystemStatus;
  children?: TreeNode[];
}

export function unwrapData<T>(response: { data: ApiEnvelope<T> | T }): T {
  const payload = response.data;
  if (payload && typeof payload === 'object' && 'data' in payload) {
    return (payload as ApiEnvelope<T>).data;
  }
  return payload as T;
}
