<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { TableColumnsType } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import { changeDeptStatus, createDept, getDept, listDeptTree, updateDept } from '@/api/system/dept';
import type { DeptSaveDTO, DeptTreeVO } from '@/api/system/dept';
import { hasPermissionCode } from '@/permissions';

interface DeptSearchForm {
  deptName?: string;
  status?: '0' | '1';
}

const canView = computed(() => hasPermissionCode('system:dept:list'));
const canCreate = computed(() => hasPermissionCode('system:dept:add'));
const canEdit = computed(() => hasPermissionCode('system:dept:edit'));
const canChangeStatus = computed(() => hasPermissionCode('system:dept:status'));

const loading = ref(false);
const saving = ref(false);
const detailLoading = ref(false);
const errorMessage = ref('');
const rows = ref<DeptTreeVO[]>([]);
const drawerOpen = ref(false);
const detailOpen = ref(false);
const editingId = ref<string>();
const detail = ref<DeptTreeVO>();

const searchForm = reactive<DeptSearchForm>({});
const formModel = reactive<DeptSaveDTO>({
  parentId: '0',
  deptName: '',
  orderNum: 0,
  leaderUserId: '',
  phone: '',
  email: '',
  status: '0',
  remark: '',
});

const columns: TableColumnsType<DeptTreeVO> = [
  { title: 'Department', dataIndex: 'deptName' },
  { title: 'Sort', dataIndex: 'orderNum', width: 90 },
  { title: 'Leader', dataIndex: 'leaderName' },
  { title: 'Phone', dataIndex: 'phone' },
  { title: 'Email', dataIndex: 'email' },
  { title: 'Status', dataIndex: 'status', width: 120 },
  { title: 'Actions', key: 'actions', width: 220 },
];

const treeFieldNames = {
  children: 'children',
  label: 'deptName',
  value: 'id',
};

function resetForm() {
  editingId.value = undefined;
  Object.assign(formModel, {
    parentId: '0',
    deptName: '',
    orderNum: 0,
    leaderUserId: '',
    phone: '',
    email: '',
    status: '0',
    remark: '',
  });
}

async function loadRows() {
  if (!canView.value) {
    return;
  }
  loading.value = true;
  errorMessage.value = '';
  try {
    rows.value = await listDeptTree(searchForm);
  } catch (error) {
    rows.value = [];
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load departments.';
  } finally {
    loading.value = false;
  }
}

function openCreate(parentId = '0') {
  resetForm();
  formModel.parentId = parentId;
  drawerOpen.value = true;
}

function openEdit(record: DeptTreeVO) {
  resetForm();
  editingId.value = record.id;
  Object.assign(formModel, {
    parentId: record.parentId,
    deptName: record.deptName,
    orderNum: record.orderNum,
    leaderUserId: record.leaderUserId,
    phone: record.phone,
    email: record.email,
    status: record.status,
  });
  drawerOpen.value = true;
}

async function openDetail(record: DeptTreeVO) {
  detailOpen.value = true;
  detailLoading.value = true;
  detail.value = undefined;
  try {
    detail.value = await getDept(record.id);
  } catch {
    detail.value = record;
  } finally {
    detailLoading.value = false;
  }
}

async function submitForm() {
  saving.value = true;
  try {
    if (editingId.value) {
      await updateDept(editingId.value, formModel);
    } else {
      await createDept(formModel);
    }
    message.success('Saved');
    drawerOpen.value = false;
    await loadRows();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Save failed');
  } finally {
    saving.value = false;
  }
}

async function toggleStatus(record: DeptTreeVO) {
  saving.value = true;
  try {
    await changeDeptStatus(record.id, record.status === '0' ? '1' : '0');
    message.success('Status updated');
    await loadRows();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Status update failed');
  } finally {
    saving.value = false;
  }
}

function handleReset() {
  Object.assign(searchForm, { deptName: undefined, status: undefined });
  void loadRows();
}

onMounted(() => {
  void loadRows();
});
</script>

<template>
  <a-result v-if="!canView" status="403" title="Permission denied" sub-title="Missing system:dept:list." />
  <div v-else class="system-page">
    <a-alert v-if="errorMessage" type="error" show-icon :message="errorMessage" class="state-alert" />
    <a-form layout="inline" :model="searchForm" class="search-form">
      <a-form-item label="Department">
        <a-input v-model:value="searchForm.deptName" allow-clear />
      </a-form-item>
      <a-form-item label="Status">
        <a-select v-model:value="searchForm.status" allow-clear class="status-select">
          <a-select-option value="0">Enabled</a-select-option>
          <a-select-option value="1">Disabled</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-space>
          <a-button type="primary" :loading="loading" @click="loadRows">Search</a-button>
          <a-button :disabled="loading" @click="handleReset">Reset</a-button>
        </a-space>
      </a-form-item>
    </a-form>

    <a-space class="toolbar">
      <a-button type="primary" :disabled="!canCreate" @click="openCreate()">New department</a-button>
      <a-button :disabled="true">Save sort</a-button>
      <a-tag color="default">Drag sort is scaffolded for a later backend-confirmed slice</a-tag>
    </a-space>

    <a-table
      row-key="id"
      :columns="columns"
      :data-source="rows"
      :loading="loading"
      :pagination="false"
      default-expand-all-rows
    >
      <template #emptyText>
        <a-empty description="No departments" />
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <a-tag :color="record.status === '0' ? 'green' : 'default'">
            {{ record.status === '0' ? 'Enabled' : 'Disabled' }}
          </a-tag>
          <a-switch :checked="record.status === '0'" size="small" disabled />
        </template>
        <template v-else-if="column.key === 'actions'">
          <a-space>
            <a-button type="link" @click="openDetail(record)">Detail</a-button>
            <a-button type="link" :disabled="!canEdit" @click="openEdit(record)">Edit</a-button>
            <a-button type="link" :disabled="!canCreate" @click="openCreate(record.id)">Child</a-button>
            <a-popconfirm title="Change this department status?" @confirm="toggleStatus(record)">
              <a-button type="link" danger :disabled="!canChangeStatus || saving">
                {{ record.status === '0' ? 'Disable' : 'Enable' }}
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-drawer v-model:open="drawerOpen" :title="editingId ? 'Edit department' : 'New department'" width="520">
      <a-form layout="vertical" :model="formModel">
        <a-form-item label="Parent department" required>
          <a-tree-select
            v-model:value="formModel.parentId"
            :tree-data="rows"
            :field-names="treeFieldNames"
            allow-clear
            tree-default-expand-all
          />
        </a-form-item>
        <a-form-item label="Department name" required>
          <a-input v-model:value="formModel.deptName" />
        </a-form-item>
        <a-form-item label="Sort" required>
          <a-input-number v-model:value="formModel.orderNum" :min="0" class="wide-control" />
        </a-form-item>
        <a-form-item label="Leader user ID">
          <a-input v-model:value="formModel.leaderUserId" />
        </a-form-item>
        <a-form-item label="Phone">
          <a-input v-model:value="formModel.phone" />
        </a-form-item>
        <a-form-item label="Email">
          <a-input v-model:value="formModel.email" />
        </a-form-item>
        <a-form-item label="Enabled">
          <a-switch
            :checked="formModel.status === '0'"
            @change="(checked: boolean) => (formModel.status = checked ? '0' : '1')"
          />
        </a-form-item>
        <a-form-item label="Remark">
          <a-textarea v-model:value="formModel.remark" :maxlength="500" />
        </a-form-item>
      </a-form>
      <template #footer>
        <a-space>
          <a-button @click="drawerOpen = false">Cancel</a-button>
          <a-button type="primary" :loading="saving" :disabled="saving" @click="submitForm">Save</a-button>
        </a-space>
      </template>
    </a-drawer>

    <a-drawer v-model:open="detailOpen" title="Department detail" width="520">
      <a-spin :spinning="detailLoading">
        <a-descriptions v-if="detail" bordered :column="1">
          <a-descriptions-item label="Department">{{ detail.deptName }}</a-descriptions-item>
          <a-descriptions-item label="Parent">{{ detail.parentId }}</a-descriptions-item>
          <a-descriptions-item label="Ancestors">{{ detail.ancestors || '-' }}</a-descriptions-item>
          <a-descriptions-item label="Leader">{{ detail.leaderName || detail.leaderUserId || '-' }}</a-descriptions-item>
          <a-descriptions-item label="Contact">{{ detail.phone || '-' }} / {{ detail.email || '-' }}</a-descriptions-item>
        </a-descriptions>
        <a-empty v-else description="No detail" />
      </a-spin>
    </a-drawer>
  </div>
</template>

<style scoped>
.search-form,
.toolbar,
.state-alert {
  margin-bottom: 16px;
}

.status-select {
  width: 140px;
}

.wide-control {
  width: 100%;
}
</style>
