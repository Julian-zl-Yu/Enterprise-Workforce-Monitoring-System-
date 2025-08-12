<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <div class="mod-sys__dict">
      <el-form 
        :inline="true" 
        :model="dataForm" 
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-input 
            v-model="dataForm.dictName" 
            :placeholder="$t('dict.dictName')" 
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="dataForm.dictType" 
            :placeholder="$t('dict.dictType')" 
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">
            {{ $t('query') }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button 
            v-if="$hasPermission('sys:dict:save')" 
            type="primary" 
            @click="handleAddOrUpdateByID()"
          >
            {{ $t('add') }}
          </el-button>
        </el-form-item>
        <!-- <el-form-item>
          <el-button 
            v-if="$hasPermission('sys:dict:delete')" 
            type="danger" 
            @click="deleteHandle()"
          >
            {{ $t('deleteBatch') }}
          </el-button>
        </el-form-item> -->
      </el-form>
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        style="width: 100%;"
        @selection-change="dataListSelectionChangeHandle"
        @sort-change="dataListSortChangeHandle"
      >
        <el-table-column 
          type="selection" 
          header-align="center" 
          align="center" 
          width="50"
        />
        <el-table-column 
          :label="$t('dict.dictName')" 
          prop="dictName" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('dict.dictType')" 
          prop="dictType" 
          header-align="center" 
          align="center"
        >
          <template slot-scope="scope">
            <el-button 
              type="text" 
              @click="childHandle(scope.row)"
            >
              {{ scope.row.dictType }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column 
          :label="$t('dict.sort')" 
          prop="sort" 
          sortable="custom" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('dict.remark')" 
          prop="remark" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('dict.createDate')" 
          prop="createDate" 
          sortable="custom" 
          header-align="center" 
          align="center" 
          width="180"
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
              v-if="$hasPermission('sys:dict:update')" 
              type="text" 
              size="small" 
              @click="handleAddOrUpdateByID(scope.row.id)"
            >
              {{ $t('update') }}
            </el-button>
            <!-- <el-button 
              v-if="$hasPermission('sys:dict:delete')" 
              type="text" 
              size="small" 
              @click="deleteHandle(scope.row.id)"
            >
              {{ $t('delete') }}
            </el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle"
      />
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
import AddOrUpdate from "./dict-type-add-or-update";
import { addDynamicRoute } from "@/router";
export default {
    components: {
        AddOrUpdate
    },
    mixins: [mixinViewModule],
    data () {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/sys/dict/type/page",
                getDataListIsPage: true,
                deleteURL: "/sys/dict/type",
                deleteIsBatch: true
            },
            dataForm: {
                id: "0",
                dictName: "",
                dictType: ""
            }
        };
    },
    methods: {
    // 子级
        childHandle (row) {
            // 路由参数
            const routeParams = {
                routeName: `${this.$route.name}__${row.id}`,
                title: `${this.$route.meta.title} - ${row.dictType}`,
                path: "sys/dict-data",
                params: {
                    dictTypeId: row.id
                }
            };
            // 动态路由
            addDynamicRoute(routeParams, this.$router);
        }
    }
};
</script>
