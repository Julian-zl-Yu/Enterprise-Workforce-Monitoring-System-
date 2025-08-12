<template>
  <el-card
    shadow="never"
    class="app-card--fill"
  >
    <div class="mod-sys__region">
      <el-form
        :inline="true"
        :model="dataForm"
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-button
            v-if="$hasPermission('sys:region:save')"
            type="primary"
            @click="handleAddOrUpdateByID()"
          >
            {{ $t('add') }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        :load="load"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        row-key="id"
        border
        lazy
        style="width: 100%"
      >
        <el-table-column
          :label="$t('region.name')"
          prop="name"
          header-align="center"
          min-width="180"
        >
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('region.id')"
          prop="id"
          header-align="center"
          min-width="180"
        >
          <template slot-scope="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('region.type')"
          prop="treeLevel"
          header-align="center"
          min-width="150"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.treeLevel === 1">{{ $t('region.province') }}</span>
            <span v-else-if="scope.row.treeLevel === 2">{{ $t('region.city') }}</span>
            <span v-else>{{ $t('region.county') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('region.sort')"
          prop="sort"
          header-align="center"
          align="center"
          min-width="150"
        />
        <el-table-column
          :label="$t('region.updateDate')"
          prop="updateDate"
          header-align="center"
          align="center"
          min-width="150"
        />
        <el-table-column
          :label="$t('handle')"
          fixed="right"
          header-align="center"
          align="center"
          width="150"
        >
          <template slot-scope="scope">
            <el-button
              v-if="$hasPermission('sys:region:update')"
              type="text"
              size="small"
              @click="handleAddOrUpdateByID(scope.row.id)"
            >
              {{ $t('update') }}
            </el-button>
            <!-- <el-button
              v-if="$hasPermission('sys:region:delete')"
              type="text"
              size="small"
              @click="deleteHandle(scope.row.id)"
            >
              {{ $t('delete') }}
            </el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update
        v-if="addOrUpdateVisible"
        ref="addOrUpdate"
        @refreshDataList="getDataList"
      />
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import AddOrUpdate from "./region-add-or-update";

export default {
    components: {
        AddOrUpdate
    },
    mixins: [mixinViewModule],
    data() {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/sys/region/list",
                deleteURL: "/sys/region"
            }
        };
    },
    methods: {
        load(tree, treeNode, resolve) {
            this.$http.get(`/sys/region/list?pid=${tree.id}`).then(({data: res}) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                resolve(res.data);
            }).catch(() => {
            });
        },
        // 新增 / 修改
        handleAddOrUpdateByID(id) {
            this.addOrUpdateVisible = true;
            this.$nextTick(() => {
                this.$refs.addOrUpdate.init(id);
            });
        }
    }
};
</script>
