<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <div class="mod-demo__correction}">
      <el-form 
        :inline="true" 
        :model="dataForm" 
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-button 
            v-if="$hasPermission('activiti:correction:all')" 
            type="primary" 
            @click="handleAddOrUpdateByID()"
          >
            {{ $t('add') }}
          </el-button>
        </el-form-item>
        <!-- <el-form-item>
          <el-button 
            v-if="$hasPermission('activiti:correction:all')" 
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
      >
        <el-table-column 
          type="selection" 
          header-align="center" 
          align="center" 
          width="50"
        />
        <el-table-column 
          :label="$t('running.id')" 
          prop="instanceId" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('correction.post')" 
          prop="applyPost" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('correction.entryDate')" 
          prop="entryDate" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('correction.correctionDate')" 
          prop="correctionDate" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('correction.workContent')" 
          prop="workContent" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('correction.achievement')" 
          prop="achievement" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('createDate')" 
          prop="createDate" 
          header-align="center" 
          align="center"
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
              type="text" 
              size="small" 
              @click="showDetail(scope.row)"
            >
              {{ $t('process.viewFlowImage') }}
            </el-button>
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
import AddOrUpdate from "./correction-add-or-update";
// 引入工作流公共方法
import processModule from "@/mixins/process-module";
export default {
    components: {
        AddOrUpdate
    },
    // 注入公共方法
    mixins: [mixinViewModule, processModule],
    data () {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/act/demo/correction/page",
                getDataListIsPage: true,
                exportURL: "/act/demo/correction/export",
                deleteURL: "/act/demo/correction",
                deleteIsBatch: true
            },
            dataForm: {
                id: ""
            },
            // 流程定义KEY
            procDefKey: "correctionprocess"
        };
    },
    methods: {
    // 查看流程处理详情
        showDetail (row) {
            if (!row.id) {
                return this.$message.error(this.$t("task.detailError"));
            }
            var params = {
                businessKey: row.id,
                procDefKey: this.procDefKey
            };
            this.getProcDefBizRouteAndProcessInstance(params, this.forwardDetail);
        }
    }
};
</script>
