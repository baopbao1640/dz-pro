import { UserManager, WebStorageStateStore, User } from 'oidc-client-ts';

const settings = {
  authority: 'http://localhost:8080/realms/platform',
  client_id: 'platform-client',
  redirect_uri: `${window.location.origin}/oauth2/callback`,
  post_logout_redirect_uri: `${window.location.origin}/login`,
  response_type: 'code',
  scope: 'openid profile email roles',
  userStore: new WebStorageStateStore({ store: window.localStorage }),
  automaticSilentRenew: true,
  loadUserInfo: true,
};

export const userManager = new UserManager(settings);

const asRecord = (v: unknown): Record<string, unknown> | undefined =>
  v && typeof v === 'object' ? (v as Record<string, unknown>) : undefined;

const asStringArray = (v: unknown): string[] =>
  Array.isArray(v) ? v.filter((x): x is string => typeof x === 'string') : [];

export const toUserInfo = (user: User | null) => {
  if (!user || !user.profile) return null;
  const profile: Record<string, unknown> = user.profile as Record<string, unknown>;
  const realmAccess = asRecord(profile.realm_access);
  return {
    id: String(profile.sub ?? ''),
    username: String(profile.preferred_username ?? profile.email ?? ''),
    name: String(profile.name ?? profile.nickname ?? ''),
    roles: realmAccess ? asStringArray(realmAccess.roles) : [],
  };
};

export const getLoginUrl = () => {
  const base = 'http://localhost:8080/realms/platform/protocol/openid-connect/auth';
  const params = new URLSearchParams({
    client_id: 'platform-client',
    redirect_uri: `${window.location.origin}/oauth2/callback`,
    response_type: 'code',
    scope: 'openid profile email roles',
    ui_locales: 'zh-CN',
  });
  return `${base}?${params.toString()}`;
};
