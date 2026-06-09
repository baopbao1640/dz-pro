<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type { TableColumnsType } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import {
  changeMenuStatus,
  createMenu,
  getMenu,
  listMenuTree,
  updateMenu,
  type MenuSaveDTO,
  type MenuTreeVO,
} from '@/api/system/menu';
import { hasPermissionCode } from '@/permissions';

/*
 * Boundary:
 * 菜单管理页负责 Ant Design Vue 树表格和菜单资源表单体验，不直接决定动态路由最终可访问性。
 * 后端菜单授权和 `@RequiresPermission` 仍是权限边界。
 */
const canView = computed(() => hasPermissionCode('system:menu:list'));
const canCreate = computed(() => hasPermissionCode('system:menu:add'));
const canEdit = computed(() => hasPermissionCode('system:menu:edit'));

const loading = ref(false);
const saving = ref(false);
const drawerOpen = ref(false);
const errorMessage = ref('');
const rows = ref<MenuTreeVO[]>([]);
const editingId = ref<string>();
const searchForm = reactive({ menuName: '', permissionCode: '', status: undefined as '0' | '1' | undefined });
const formModel = reactive<MenuSaveDTO>({
  parentId: '0',
  menuName: '',
  orderNum: 0,
  path: '',
  component: '',
  routeName: '',
  isFrame: 'N',
  isCache: 'N',
  menuType: 'C',
  visible: 'Y',
  status: '0',
  permissionCode: '',
  icon: '',
  remark: '',
});

const columns: TableColumnsType<MenuTreeVO> = [
  { title: 'Menu name', dataIndex: 'menuName' },
  { title: 'Type', dataIndex: 'menuType', width: 90 },
  { title: 'Permission', dataIndex: 'permissionCode' },
  { title: 'Path', dataIndex: 'path' },
  { title: 'Component', dataIndex: 'component' },
  { title: 'Status', dataIndex: 'status', width: 100 },
  { title: 'Actions', key: 'actions', width: 190 },
];

function resetForm() {
  editingId.value = undefined;
  Object.assign(formModel, {
    parentId: '0',
    menuName: '',
    orderNum: 0,
    path: '',
    component: '',
    routeName: '',
    isFrame: 'N',
    isCache: 'N',
    menuType: 'C',
    visible: 'Y',
    status: '0',
    permissionCode: '',
    icon: '',
    remark: '',
  });
}

async function loadMenus() {
  if (!canView.value) return;
  loading.value = true;
  errorMessage.value = '';
  try {
    rows.value = await listMenuTree(searchForm);
  } catch (error) {
    rows.value = [];
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load menus.';
  } finally {
    loading.value = false;
  }
}

function openCreate(parentId = '0') {
  resetForm();
  formModel.parentId = parentId;
  drawerOpen.value = true;
}

async function openEdit(record: MenuTreeVO) {
  resetForm();
  editingId.value = record.id;
  Object.assign(formModel, await getMenu(record.id));
  drawerOpen.value = true;
}

async function submitForm() {
  saving.value = true;
  try {
    if (editingId.value) {
      await updateMenu(editingId.value, formModel);
    } else {
      await createMenu(formModel);
    }
    message.success('Saved');
    drawerOpen.value = false;
    await loadMenus();
  } catch (error) {
    message.error(error instanceof Error ? error.message : 'Save failed');
  } finally {
    saving.value = false;
  }
}

async function toggleStatus(record: MenuTreeVO) {
  await changeMenuStatus(record.id, record.status === '0' ? '1' : '0');
  await loadMenus();
}

function setVisible(checked: boolean) {
  formModel.visible = checked ? 'Y' : 'N';
}

function setCache(checked: boolean) {
  formModel.isCache = checked ? 'Y' : 'N';
}

function setStatus(checked: boolean) {
  formModel.status = checked ? '0' : '1';
}

onMounted(loadMenus);
</script>

<template>
  <a-result v-if="!canView" status="403" title="Permission denied" sub-title="Missing system:menu:list." />
  <div v-else class="system-page">
    <a-alert v-if="errorMessage" type="error" show-icon :message="errorMessage" />
    <a-form layout="inline" :model="searchForm" class="search-form">
      <a-form-item label="Name"><a-input v-model:value="searchForm.menuName" allow-clear /></a-form-item>
      <a-form-item label="Permission"><a-input v-model:value="searchForm.permissionCode" allow-clear /></a-form-item>
      <a-form-item><a-button type="primary" @click="loadMenus">Search</a-button><a-button :disabled="!canCreate" @click="openCreate()">New</a-button></a-form-item>
    </a-form>
    <a-table :columns="columns" :data-source="rows" :loading="loading" row-key="id" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'menuType'">
          <a-tag>{{ record.menuType }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'status'">
          <a-tag :color="record.status === '0' ? 'green' : 'red'">{{ record.status === '0' ? 'Enabled' : 'Disabled' }}</a-tag>
        </template>
        <template v-if="column.key === 'actions'">
          <a-button type="link" :disabled="!canCreate" @click="openCreate(record.id)">Add child</a-button>
          <a-button type="link" :disabled="!canEdit" @click="openEdit(record)">Edit</a-button>
          <a-popconfirm title="Change status?" @confirm="toggleStatus(record)">
            <a-button type="link" :disabled="!canEdit">{{ record.status === '0' ? 'Disable' : 'Enable' }}</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
    <a-drawer v-model:open="drawerOpen" width="560" :title="editingId ? 'Edit menu' : 'New menu'">
      <a-form layout="vertical" :model="formModel">
        <a-form-item label="Parent ID"><a-input v-model:value="formModel.parentId" /></a-form-item>
        <a-form-item label="Menu name" required><a-input v-model:value="formModel.menuName" /></a-form-item>
        <a-form-item label="Type">
          <a-radio-group v-model:value="formModel.menuType">
            <a-radio-button value="M">Directory</a-radio-button>
            <a-radio-button value="C">Menu</a-radio-button>
            <a-radio-button value="F">Button</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="Path"><a-input v-model:value="formModel.path" /></a-form-item>
        <a-form-item label="Component"><a-input v-model:value="formModel.component" /></a-form-item>
        <a-form-item label="Route name"><a-input v-model:value="formModel.routeName" /></a-form-item>
        <a-form-item label="Permission"><a-input v-model:value="formModel.permissionCode" /></a-form-item>
        <a-form-item label="Sort"><a-input-number v-model:value="formModel.orderNum" :min="0" /></a-form-item>
        <a-tabs>
          <a-tab-pane key="state" tab="State">
            <a-form-item label="Visible"><a-switch :checked="formModel.visible === 'Y'" @change="setVisible" /></a-form-item>
            <a-form-item label="Cache"><a-switch :checked="formModel.isCache === 'Y'" @change="setCache" /></a-form-item>
            <a-form-item label="Status"><a-switch :checked="formModel.status === '0'" @change="setStatus" /></a-form-item>
          </a-tab-pane>
        </a-tabs>
      </a-form>
      <template #footer><a-button type="primary" :loading="saving" @click="submitForm">Save</a-button></template>
    </a-drawer>
  </div>
</template>
