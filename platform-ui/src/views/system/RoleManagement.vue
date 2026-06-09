<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { TableColumnsType } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import {
  changeRoleStatus,
  createRole,
  getRole,
  listRoles,
  updateRole,
  type RoleDetailVO,
  type RoleListVO,
  type RoleSaveDTO,
} from '@/api/system/role';
import { hasPermissionCode } from '@/permissions';

/*
 * Boundary:
 * 角色管理页只维护后台列表、表单弹窗和状态切换体验；菜单授权和数据范围真实生效仍由后端角色关系与 data scope 实现保证。
 */
const canView = computed(() => hasPermissionCode('system:role:list'));
const canCreate = computed(() => hasPermissionCode('system:role:add'));
const canEdit = computed(() => hasPermissionCode('system:role:edit'));
const canChangeStatus = computed(() => hasPermissionCode('system:role:status'));

const loading = ref(false);
const saving = ref(false);
const drawerOpen = ref(false);
const detailOpen = ref(false);
const errorMessage = ref('');
const rows = ref<RoleListVO[]>([]);
const detail = ref<RoleDetailVO>();
const editingId = ref<string>();

const searchForm = reactive({ roleName: '', roleKey: '', status: undefined as '0' | '1' | undefined });
const formModel = reactive<RoleSaveDTO>({
  roleName: '',
  roleKey: '',
  roleSort: 0,
  dataScope: '1',
  menuCheckStrictly: 'Y',
  deptCheckStrictly: 'Y',
  status: '0',
  remark: '',
});

const columns: TableColumnsType<RoleListVO> = [
  { title: 'Role name', dataIndex: 'roleName' },
  { title: 'Role key', dataIndex: 'roleKey' },
  { title: 'Sort', dataIndex: 'roleSort', width: 90 },
  { title: 'Data scope', dataIndex: 'dataScopeLabel' },
  { title: 'Status', dataIndex: 'status', width: 100 },
  { title: 'Created', dataIndex: 'createTime', width: 180 },
  { title: 'Actions', key: 'actions', width: 220, fixed: 'right' },
];

function resetForm() {
  editingId.value = undefined;
  Object.assign(formModel, {
    roleName: '',
    roleKey: '',
    roleSort: 0,
    dataScope: '1',
    menuCheckStrictly: 'Y',
    deptCheckStrictly: 'Y',
    status: '0',
    remark: '',
  });
}

async function loadRoles() {
  if (!canView.value) return;
  loading.value = true;
  errorMessage.value = '';
  try {
    rows.value = await listRoles(searchForm);
  } catch (error) {
    rows.value = [];
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load roles.';
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  resetForm();
  drawerOpen.value = true;
}

async function openEdit(record: RoleListVO) {
  resetForm();
  editingId.value = record.id;
  const role = await getRole(record.id);
  Object.assign(formModel, role);
  drawerOpen.value = true;
}

async function openDetail(record: RoleListVO) {
  detailOpen.value = true;
  detail.value = await getRole(record.id);
}

async function submitForm() {
  saving.value = true;
  try {
    if (editingId.value) {
      await updateRole(editingId.value, formModel);
    } else {
      await createRole(formModel);
    }
    message.success('Saved');
    drawerOpen.value = false;
    await loadRoles();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Save failed');
  } finally {
    saving.value = false;
  }
}

async function toggleStatus(record: RoleListVO) {
  saving.value = true;
  try {
    await changeRoleStatus(record.id, record.status === '0' ? '1' : '0');
    await loadRoles();
  } finally {
    saving.value = false;
  }
}

function setFormStatus(checked: boolean) {
  formModel.status = checked ? '0' : '1';
}

onMounted(loadRoles);
</script>

<template>
  <a-result v-if="!canView" status="403" title="Permission denied" sub-title="Missing system:role:list." />
  <div v-else class="system-page">
    <a-alert v-if="errorMessage" type="error" show-icon :message="errorMessage" />
    <a-form layout="inline" :model="searchForm" class="search-form">
      <a-form-item label="Name"><a-input v-model:value="searchForm.roleName" allow-clear /></a-form-item>
      <a-form-item label="Key"><a-input v-model:value="searchForm.roleKey" allow-clear /></a-form-item>
      <a-form-item label="Status">
        <a-select v-model:value="searchForm.status" allow-clear style="width: 120px">
          <a-select-option value="0">Enabled</a-select-option>
          <a-select-option value="1">Disabled</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="loadRoles">Search</a-button>
        <a-button :disabled="!canCreate" @click="openCreate">New</a-button>
      </a-form-item>
    </a-form>
    <a-table :columns="columns" :data-source="rows" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <a-tag :color="record.status === '0' ? 'green' : 'red'">{{ record.status === '0' ? 'Enabled' : 'Disabled' }}</a-tag>
        </template>
        <template v-if="column.key === 'actions'">
          <a-button type="link" @click="openDetail(record)">Detail</a-button>
          <a-button type="link" :disabled="!canEdit" @click="openEdit(record)">Edit</a-button>
          <a-popconfirm title="Change status?" @confirm="toggleStatus(record)">
            <a-button type="link" :disabled="!canChangeStatus">{{ record.status === '0' ? 'Disable' : 'Enable' }}</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-drawer v-model:open="drawerOpen" width="520" :title="editingId ? 'Edit role' : 'New role'">
      <a-form layout="vertical" :model="formModel">
        <a-form-item label="Role name" required><a-input v-model:value="formModel.roleName" /></a-form-item>
        <a-form-item label="Role key" required><a-input v-model:value="formModel.roleKey" /></a-form-item>
        <a-form-item label="Sort" required><a-input-number v-model:value="formModel.roleSort" :min="0" /></a-form-item>
        <a-form-item label="Data scope">
          <a-select v-model:value="formModel.dataScope">
            <a-select-option value="1">All</a-select-option>
            <a-select-option value="2">Custom</a-select-option>
            <a-select-option value="3">Dept</a-select-option>
            <a-select-option value="4">Dept and children</a-select-option>
            <a-select-option value="5">Self</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Status"><a-switch :checked="formModel.status === '0'" @change="setFormStatus" /></a-form-item>
        <a-form-item label="Remark"><a-textarea v-model:value="formModel.remark" /></a-form-item>
      </a-form>
      <template #footer><a-button type="primary" :loading="saving" @click="submitForm">Save</a-button></template>
    </a-drawer>
    <a-modal v-model:open="detailOpen" title="Role detail" :footer="null">
      <a-descriptions v-if="detail" bordered size="small">
        <a-descriptions-item label="Name">{{ detail.roleName }}</a-descriptions-item>
        <a-descriptions-item label="Key">{{ detail.roleKey }}</a-descriptions-item>
        <a-descriptions-item label="Users">{{ detail.assignedUserCount ?? 0 }}</a-descriptions-item>
      </a-descriptions>
      <a-empty v-else />
    </a-modal>
  </div>
</template>
