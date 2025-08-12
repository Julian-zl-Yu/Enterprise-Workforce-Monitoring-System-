<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <div class="mod-activiti__process">
      <el-form 
        :inline="true" 
        :model="dataForm" 
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-input 
            v-model="dataForm.taskName" 
            :placeholder="$t('process.name')" 
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">
            {{ $t('query') }}
          </el-button>
        </el-form-item>
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
          :label="$t('process.businessKey')" 
          prop="businessKey" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('running.id')" 
          prop="processInstanceId" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.processDefinitionName')" 
          prop="processDefinitionName" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.startTime')" 
          prop="startTime" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.taskName')" 
          prop="taskName" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.createTime')" 
          prop="createTime" 
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
              @click="claimTask(scope.row.taskId, scope.row.processDefinitionKey, scope.row.businessKey)"
            >
              {{ $t('process.claim') }}
            </el-button>
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
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
import processModule from "@/mixins/process-module";
import qs from "qs";
export default {
    components: {
    },
    mixins: [mixinViewModule, processModule],
    data () {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/act/task/page",
                getDataListIsPage: true,
                deleteIsBatch: true,
                deleteIsBatchKey: "deploymentId"
            },
            dataForm: {
                isRoleGroup: "1",
                taskName: ""
            }
        };
    },
    methods: {
        claimTask (taskId, processDefinitionKey, businessKey) {
            var params = qs.stringify({
                "taskId": taskId
            });
            this.$http.post("/act/task/claim", params).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                this.$message({
                    message: this.$t("prompt.success"),
                    type: "success",
                    duration: 500,
                    onClose: () => {
                        this.getDataList();
                    }
                });
            }).catch(error => console.error(error));
        },
        showDetail (row) {
            if (!row.businessKey) {
                return this.$message.error(this.$t("task.detailError"));
            }
            this.getProcDefRouteSet(row, this.forwardDetail);
        }
    }
};
</script>
