<template>
  <el-card shadow="never" class="app-card--fill">
    <div class="mod-sys__menu">
      <el-form
        :inline="true"
        :model="dataForm"
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-button
            v-if="$hasPermission('sys:menu:save')"
            type="primary"
            @click="handleAddOrUpdateByID()"
          >
            {{ $t("add") }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        row-key="id"
        border
        style="width: 100%"
      >
        <el-table-column
          :label="$t('menu.name')"
          prop="name"
          header-align="center"
          min-width="150"
        />
        <el-table-column
          :label="$t('menu.icon')"
          prop="icon"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            <svg class="icon-svg" aria-hidden="true">
              <use :xlink:href="`#${scope.row.icon}`" />
            </svg>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('menu.type')"
          prop="type"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 0" size="small">
              {{ $t("menu.type0") }}
            </el-tag>
            <el-tag v-else size="small" type="info">
              {{ $t("menu.type1") }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('menu.sort')"
          prop="sort"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('menu.url')"
          :show-overflow-tooltip="true"
          prop="url"
          header-align="center"
          align="center"
          width="150"
        />
        <el-table-column
          :label="$t('menu.permissions')"
          :show-overflow-tooltip="true"
          prop="permissions"
          header-align="center"
          align="center"
          width="150"
        />
        <el-table-column
          :label="$t('handle')"
          fixed="right"
          header-align="center"
          align="center"
          width="200"
        >
          <template slot-scope="scope">
            <el-button
              v-if="$hasPermission('sys:menu:save') && scope.row.pid == 0"
              type="text"
              size="small"
              @click="handleAddOrUpdateBatchAdd(scope.row.id, 0)"
            >
              批量新增菜单
            </el-button>
            <el-button
              v-if="
                $hasPermission('sys:menu:save') &&
                scope.row.pid != 0 &&
                scope.row.url
              "
              type="text"
              size="small"
              @click="handleAddOrUpdateBatchAdd(scope.row.id, 1)"
            >
              批量新增权限
            </el-button>
            <el-button
              v-if="$hasPermission('sys:menu:update')"
              type="text"
              size="small"
              @click="handleAddOrUpdateByID(scope.row.id)"
            >
              {{ $t("update") }}
            </el-button>
            <el-button
              v-if="$hasPermission('sys:menu:delete')"
              type="text"
              size="small"
              @click="deleteHandle(scope.row.id)"
            >
              {{ $t("delete") }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update
        v-if="addOrUpdateVisible"
        ref="addOrUpdate"
        @refreshDataList="getDataList"
      />
      <add-or-update-batch
        v-if="addOrUpdateBatchVisible"
        ref="addOrUpdateBatch"
        @refreshDataList="getDataList"
      />
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import AddOrUpdate from "./menu-add-or-update";
import AddOrUpdateBatch from "./menu-add-or-update-batch.vue";
export default {
  components: {
    AddOrUpdate,
    AddOrUpdateBatch,
  },
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        getDataListURL: "/sys/menu/list",
        deleteURL: "/sys/menu",
      },
    };
  },
};
</script>
