<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { TableColumnsType } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import { listDeptTree } from '@/api/system/dept';
import type { DeptTreeVO } from '@/api/system/dept';
import { changeUserStatus, createUser, getUser, listUsers, updateUser } from '@/api/system/user';
import type { UserDetailVO, UserListVO, UserSaveDTO } from '@/api/system/user';
import { hasPermissionCode } from '@/permissions';

/*
 * Boundary:
 * 用户管理页采用 Ant Design Vue 后台列表 + 弹窗表单结构，只负责前端展示、表单状态和按钮权限体验。
 * 后端 `system:user:*` permission code 仍是最终授权边界。
 */
interface UserSearchForm {
  userName?: string;
  nickName?: string;
  phoneNumber?: string;
  status?: '0' | '1';
  deptId?: string;
}

const permission = 'system:user:list';
const canView = computed(() => hasPermissionCode(permission));
const canCreate = computed(() => hasPermissionCode('system:user:add'));
const canEdit = computed(() => hasPermissionCode('system:user:edit'));
const canChangeStatus = computed(() => hasPermissionCode('system:user:status'));

const loading = ref(false);
const deptLoading = ref(false);
const saving = ref(false);
const detailLoading = ref(false);
const assignLoading = ref(false);
const errorMessage = ref('');
const rows = ref<UserListVO[]>([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const deptTree = ref<DeptTreeVO[]>([]);
const selectedDeptKeys = ref<string[]>([]);

const searchForm = reactive<UserSearchForm>({});
const formModel = reactive<UserSaveDTO>({
  keycloakUserId: '',
  userName: '',
  nickName: '',
  deptId: undefined,
  email: '',
  phoneNumber: '',
  sex: '0',
  status: '0',
  remark: '',
});

const drawerOpen = ref(false);
const detailOpen = ref(false);
const assignRoleOpen = ref(false);
const assignPostOpen = ref(false);
const editingId = ref<string>();
const detail = ref<UserDetailVO>();

const columns: TableColumnsType<UserListVO> = [
  { title: 'ID', dataIndex: 'id', width: 180 },
  { title: 'Username', dataIndex: 'userName' },
  { title: 'Display name', dataIndex: 'nickName' },
  { title: 'Department', dataIndex: 'deptName' },
  { title: 'Posts', dataIndex: 'postNames' },
  { title: 'Roles', dataIndex: 'roleNames' },
  { title: 'Phone', dataIndex: 'phoneNumber' },
  { title: 'Email', dataIndex: 'email' },
  { title: 'Status', dataIndex: 'status', width: 110 },
  { title: 'Created', dataIndex: 'createTime', width: 180 },
  { title: 'Actions', key: 'actions', width: 270, fixed: 'right' },
];

const deptFieldNames = {
  children: 'children',
  title: 'deptName',
  key: 'id',
};

function resetForm() {
  editingId.value = undefined;
  Object.assign(formModel, {
    keycloakUserId: '',
    userName: '',
    nickName: '',
    deptId: selectedDeptKeys.value[0],
    email: '',
    phoneNumber: '',
    sex: '0',
    status: '0',
    remark: '',
  });
}

async function loadDeptTree() {
  deptLoading.value = true;
  try {
    deptTree.value = await listDeptTree();
  } catch {
    deptTree.value = [];
  } finally {
    deptLoading.value = false;
  }
}

async function loadUsers() {
  if (!canView.value) {
    return;
  }
  loading.value = true;
  errorMessage.value = '';
  try {
    const result = await listUsers({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      deptId: searchForm.deptId,
      includeChildren: Boolean(searchForm.deptId),
      userName: searchForm.userName,
      nickName: searchForm.nickName,
      phoneNumber: searchForm.phoneNumber,
      status: searchForm.status,
    });
    rows.value = result.records ?? [];
    total.value = result.total ?? 0;
  } catch (error) {
    rows.value = [];
    total.value = 0;
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load users.';
  } finally {
    loading.value = false;
  }
}

function handleDeptSelect(keys: (string | number)[]) {
  const id = keys[0] ? String(keys[0]) : undefined;
  selectedDeptKeys.value = id ? [id] : [];
  searchForm.deptId = id;
  pageNum.value = 1;
  void loadUsers();
}

function handleSearch() {
  pageNum.value = 1;
  void loadUsers();
}

function handleReset() {
  Object.assign(searchForm, {
    userName: undefined,
    nickName: undefined,
    phoneNumber: undefined,
    status: undefined,
    deptId: undefined,
  });
  selectedDeptKeys.value = [];
  handleSearch();
}

function openCreate() {
  resetForm();
  drawerOpen.value = true;
}

async function openEdit(record: UserListVO) {
  resetForm();
  editingId.value = record.id;
  Object.assign(formModel, {
    keycloakUserId: record.keycloakUserId,
    userName: record.userName,
    nickName: record.nickName,
    deptId: record.deptId,
    email: record.email,
    phoneNumber: record.phoneNumber,
    sex: record.sex ?? '0',
    status: record.status,
  });
  drawerOpen.value = true;
}

async function openDetail(record: UserListVO) {
  detailOpen.value = true;
  detailLoading.value = true;
  detail.value = undefined;
  try {
    detail.value = await getUser(record.id);
  } catch {
    detail.value = { ...record };
  } finally {
    detailLoading.value = false;
  }
}

async function submitForm() {
  saving.value = true;
  try {
    if (editingId.value) {
      await updateUser(editingId.value, formModel);
    } else {
      await createUser(formModel);
    }
    message.success('Saved');
    drawerOpen.value = false;
    await loadUsers();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Save failed');
  } finally {
    saving.value = false;
  }
}

async function toggleStatus(record: UserListVO) {
  saving.value = true;
  try {
    await changeUserStatus(record.id, record.status === '0' ? '1' : '0');
    message.success('Status updated');
    await loadUsers();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Status update failed');
  } finally {
    saving.value = false;
  }
}

function handlePageChange(page: number, size: number) {
  pageNum.value = page;
  pageSize.value = size;
  void loadUsers();
}

onMounted(() => {
  void loadDeptTree();
  void loadUsers();
});
</script>

<template>
  <a-result v-if="!canView" status="403" title="Permission denied" sub-title="Missing system:user:list." />
  <div v-else class="system-page user-page">
    <aside class="dept-panel">
      <a-spin :spinning="deptLoading">
        <a-tree
          v-if="deptTree.length"
          :tree-data="deptTree"
          :field-names="deptFieldNames"
          :selected-keys="selectedDeptKeys"
          default-expand-all
          @select="handleDeptSelect"
        />
        <a-empty v-else description="No departments" />
      </a-spin>
    </aside>

    <section class="content-panel">
      <a-alert v-if="errorMessage" type="error" show-icon :message="errorMessage" class="state-alert" />
      <a-form layout="inline" :model="searchForm" class="search-form">
        <a-form-item label="Username">
          <a-input v-model:value="searchForm.userName" allow-clear />
        </a-form-item>
        <a-form-item label="Name">
          <a-input v-model:value="searchForm.nickName" allow-clear />
        </a-form-item>
        <a-form-item label="Phone">
          <a-input v-model:value="searchForm.phoneNumber" allow-clear />
        </a-form-item>
        <a-form-item label="Status">
          <a-select v-model:value="searchForm.status" allow-clear class="status-select">
            <a-select-option value="0">Enabled</a-select-option>
            <a-select-option value="1">Disabled</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" :loading="loading" @click="handleSearch">Search</a-button>
            <a-button :disabled="loading" @click="handleReset">Reset</a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <a-space class="toolbar">
        <a-button type="primary" :disabled="!canCreate" @click="openCreate">New user</a-button>
        <a-tag v-if="!canCreate" color="default">system:user:add unavailable</a-tag>
      </a-space>

      <a-table
        row-key="id"
        :columns="columns"
        :data-source="rows"
        :loading="loading"
        :pagination="{ current: pageNum, pageSize, total, showSizeChanger: true, onChange: handlePageChange }"
        :scroll="{ x: 1280 }"
      >
        <template #emptyText>
          <a-empty description="No users" />
        </template>
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'status'">
            <a-tag :color="record.status === '0' ? 'green' : 'default'">
              {{ record.status === '0' ? 'Enabled' : 'Disabled' }}
            </a-tag>
            <a-switch :checked="record.status === '0'" size="small" disabled />
          </template>
          <template v-else-if="column.dataIndex === 'postNames'">
            <a-tag v-for="post in record.postNames || []" :key="post">{{ post }}</a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'roleNames'">
            <a-tag v-for="role in record.roleNames || []" :key="role" color="blue">{{ role }}</a-tag>
          </template>
          <template v-else-if="column.key === 'actions'">
            <a-space>
              <a-button type="link" @click="openDetail(record)">Detail</a-button>
              <a-button type="link" :disabled="!canEdit" @click="openEdit(record)">Edit</a-button>
              <a-button type="link" @click="assignRoleOpen = true">Roles</a-button>
              <a-button type="link" @click="assignPostOpen = true">Posts</a-button>
              <a-popconfirm title="Change this user's local platform status?" @confirm="toggleStatus(record)">
                <a-button type="link" danger :disabled="!canChangeStatus || saving">
                  {{ record.status === '0' ? 'Disable' : 'Enable' }}
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </section>

    <a-drawer v-model:open="drawerOpen" :title="editingId ? 'Edit user' : 'New user'" width="520">
      <a-form layout="vertical" :model="formModel">
        <a-tabs>
          <a-tab-pane key="basic" tab="Basic">
            <a-form-item label="Keycloak subject" required>
              <a-input v-model:value="formModel.keycloakUserId" :disabled="Boolean(editingId)" />
            </a-form-item>
            <a-form-item label="Username" required>
              <a-input v-model:value="formModel.userName" />
            </a-form-item>
            <a-form-item label="Display name" required>
              <a-input v-model:value="formModel.nickName" />
            </a-form-item>
            <a-form-item label="Department">
              <a-tree-select
                v-model:value="formModel.deptId"
                :tree-data="deptTree"
                :field-names="deptFieldNames"
                allow-clear
                tree-default-expand-all
              />
            </a-form-item>
          </a-tab-pane>
          <a-tab-pane key="contact" tab="Contact">
            <a-form-item label="Email">
              <a-input v-model:value="formModel.email" />
            </a-form-item>
            <a-form-item label="Phone">
              <a-input v-model:value="formModel.phoneNumber" />
            </a-form-item>
            <a-form-item label="Sex">
              <a-select v-model:value="formModel.sex">
                <a-select-option value="0">Unknown</a-select-option>
                <a-select-option value="1">Male</a-select-option>
                <a-select-option value="2">Female</a-select-option>
              </a-select>
            </a-form-item>
          </a-tab-pane>
          <a-tab-pane key="status" tab="Status">
            <a-form-item label="Enabled">
              <a-switch
                :checked="formModel.status === '0'"
                @change="(checked: boolean) => (formModel.status = checked ? '0' : '1')"
              />
            </a-form-item>
            <a-form-item label="Remark">
              <a-textarea v-model:value="formModel.remark" :maxlength="500" />
            </a-form-item>
          </a-tab-pane>
        </a-tabs>
      </a-form>
      <template #footer>
        <a-space>
          <a-button @click="drawerOpen = false">Cancel</a-button>
          <a-button type="primary" :loading="saving" :disabled="saving" @click="submitForm">Save</a-button>
        </a-space>
      </template>
    </a-drawer>

    <a-drawer v-model:open="detailOpen" title="User detail" width="520">
      <a-spin :spinning="detailLoading">
        <a-descriptions v-if="detail" bordered :column="1">
          <a-descriptions-item label="Username">{{ detail.userName }}</a-descriptions-item>
          <a-descriptions-item label="Display name">{{ detail.nickName }}</a-descriptions-item>
          <a-descriptions-item label="Department">{{ detail.deptName || '-' }}</a-descriptions-item>
          <a-descriptions-item label="Data scope">{{ detail.dataScopeSummary || '-' }}</a-descriptions-item>
          <a-descriptions-item label="Permissions">
            <a-tag v-for="code in detail.permissionCodes || []" :key="code">{{ code }}</a-tag>
          </a-descriptions-item>
        </a-descriptions>
        <a-empty v-else description="No detail" />
      </a-spin>
    </a-drawer>

    <a-modal v-model:open="assignRoleOpen" title="Assign roles" :confirm-loading="assignLoading">
      <a-empty description="Role assignment API scaffold is ready; role page is outside this slice." />
    </a-modal>
    <a-modal v-model:open="assignPostOpen" title="Assign posts" :confirm-loading="assignLoading">
      <a-empty description="Post assignment API scaffold is ready; use post management for the post catalog." />
    </a-modal>
  </div>
</template>

<style scoped>
.user-page {
  display: grid;
  grid-template-columns: 260px minmax(0, 1fr);
  gap: 16px;
}

.dept-panel {
  min-height: 420px;
  padding-right: 16px;
  border-right: 1px solid #f0f0f0;
}

.content-panel {
  min-width: 0;
}

.search-form,
.toolbar,
.state-alert {
  margin-bottom: 16px;
}

.status-select {
  width: 140px;
}

@media (max-width: 900px) {
  .user-page {
    grid-template-columns: 1fr;
  }

  .dept-panel {
    min-height: auto;
    padding-right: 0;
    padding-bottom: 16px;
    border-right: 0;
    border-bottom: 1px solid #f0f0f0;
  }
}
</style>
