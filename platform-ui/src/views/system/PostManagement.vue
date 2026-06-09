<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { TableColumnsType } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import { changePostStatus, createPost, getPost, listPosts, updatePost } from '@/api/system/post';
import type { PostListVO, PostSaveDTO } from '@/api/system/post';
import { hasPermissionCode } from '@/permissions';

/*
 * Boundary:
 * 岗位管理页负责后台分页、表单弹窗和状态控制体验；用户岗位关系和删除占用约束由后端服务层保证。
 */
interface PostSearchForm {
  postCode?: string;
  postName?: string;
  status?: '0' | '1';
}

const canView = computed(() => hasPermissionCode('system:post:list'));
const canCreate = computed(() => hasPermissionCode('system:post:add'));
const canEdit = computed(() => hasPermissionCode('system:post:edit'));
const canChangeStatus = computed(() => hasPermissionCode('system:post:status'));

const loading = ref(false);
const saving = ref(false);
const detailLoading = ref(false);
const errorMessage = ref('');
const rows = ref<PostListVO[]>([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const drawerOpen = ref(false);
const detailOpen = ref(false);
const editingId = ref<string>();
const detail = ref<PostListVO>();

const searchForm = reactive<PostSearchForm>({});
const formModel = reactive<PostSaveDTO>({
  postCode: '',
  postName: '',
  postSort: 0,
  status: '0',
  remark: '',
});

const columns: TableColumnsType<PostListVO> = [
  { title: 'Post code', dataIndex: 'postCode' },
  { title: 'Post name', dataIndex: 'postName' },
  { title: 'Sort', dataIndex: 'postSort', width: 90 },
  { title: 'Bound users', dataIndex: 'boundUserCount', width: 120 },
  { title: 'Status', dataIndex: 'status', width: 120 },
  { title: 'Created', dataIndex: 'createTime', width: 180 },
  { title: 'Actions', key: 'actions', width: 220 },
];

function resetForm() {
  editingId.value = undefined;
  Object.assign(formModel, {
    postCode: '',
    postName: '',
    postSort: 0,
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
    const result = await listPosts({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      postCode: searchForm.postCode,
      postName: searchForm.postName,
      status: searchForm.status,
    });
    rows.value = result.records ?? [];
    total.value = result.total ?? 0;
  } catch (error) {
    rows.value = [];
    total.value = 0;
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load posts.';
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  resetForm();
  drawerOpen.value = true;
}

function openEdit(record: PostListVO) {
  resetForm();
  editingId.value = record.id;
  Object.assign(formModel, {
    postCode: record.postCode,
    postName: record.postName,
    postSort: record.postSort,
    status: record.status,
  });
  drawerOpen.value = true;
}

async function openDetail(record: PostListVO) {
  detailOpen.value = true;
  detailLoading.value = true;
  detail.value = undefined;
  try {
    detail.value = await getPost(record.id);
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
      await updatePost(editingId.value, formModel);
    } else {
      await createPost(formModel);
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

async function toggleStatus(record: PostListVO) {
  saving.value = true;
  try {
    await changePostStatus(record.id, record.status === '0' ? '1' : '0');
    message.success('Status updated');
    await loadRows();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Status update failed');
  } finally {
    saving.value = false;
  }
}

function handleSearch() {
  pageNum.value = 1;
  void loadRows();
}

function handleReset() {
  Object.assign(searchForm, { postCode: undefined, postName: undefined, status: undefined });
  handleSearch();
}

function handlePageChange(page: number, size: number) {
  pageNum.value = page;
  pageSize.value = size;
  void loadRows();
}

onMounted(() => {
  void loadRows();
});
</script>

<template>
  <a-result v-if="!canView" status="403" title="Permission denied" sub-title="Missing system:post:list." />
  <div v-else class="system-page">
    <a-alert v-if="errorMessage" type="error" show-icon :message="errorMessage" class="state-alert" />
    <a-form layout="inline" :model="searchForm" class="search-form">
      <a-form-item label="Code">
        <a-input v-model:value="searchForm.postCode" allow-clear />
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model:value="searchForm.postName" allow-clear />
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
      <a-button type="primary" :disabled="!canCreate" @click="openCreate">New post</a-button>
      <a-tag v-if="!canCreate" color="default">system:post:add unavailable</a-tag>
    </a-space>

    <a-table
      row-key="id"
      :columns="columns"
      :data-source="rows"
      :loading="loading"
      :pagination="{ current: pageNum, pageSize, total, showSizeChanger: true, onChange: handlePageChange }"
    >
      <template #emptyText>
        <a-empty description="No posts" />
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
            <a-popconfirm title="Change this post status?" @confirm="toggleStatus(record)">
              <a-button type="link" danger :disabled="!canChangeStatus || saving">
                {{ record.status === '0' ? 'Disable' : 'Enable' }}
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-drawer v-model:open="drawerOpen" :title="editingId ? 'Edit post' : 'New post'" width="480">
      <a-form layout="vertical" :model="formModel">
        <a-form-item label="Post code" required>
          <a-input v-model:value="formModel.postCode" />
        </a-form-item>
        <a-form-item label="Post name" required>
          <a-input v-model:value="formModel.postName" />
        </a-form-item>
        <a-form-item label="Sort" required>
          <a-input-number v-model:value="formModel.postSort" :min="0" class="wide-control" />
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

    <a-drawer v-model:open="detailOpen" title="Post detail" width="480">
      <a-spin :spinning="detailLoading">
        <a-descriptions v-if="detail" bordered :column="1">
          <a-descriptions-item label="Post code">{{ detail.postCode }}</a-descriptions-item>
          <a-descriptions-item label="Post name">{{ detail.postName }}</a-descriptions-item>
          <a-descriptions-item label="Sort">{{ detail.postSort }}</a-descriptions-item>
          <a-descriptions-item label="Bound users">{{ detail.boundUserCount ?? 0 }}</a-descriptions-item>
          <a-descriptions-item label="Created">{{ detail.createTime || '-' }}</a-descriptions-item>
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
