<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <h4>{{ $t('process.flowImage') }}</h4>
    <img 
      :src="getResourceURL()" 
      class="image"
    >
    <h4>{{ $t('process.circulation') }}</h4>
    <div class="mod-sys__dict">
      <el-table
        v-loading="dataListLoading"
        :data="dataList"
        border
        style="width: 100%;"
        @selection-change="dataListSelectionChangeHandle"
        @sort-change="dataListSortChangeHandle"
      >
        <el-table-column
          :label="$t('process.taskName')"
          prop="activityName"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('process.assignee')"
          prop="assignee"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('task.startTime')"
          prop="startTime"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('task.endTime')"
          prop="endTime"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('process.comment')"
          prop="comment"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('task.durationInSeconds')"
          prop="durationInSeconds"
          header-align="center"
          align="center"
          width="180"
        />
      </el-table>
      <el-pagination
        v-if="dataForm.pid === '0'"
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
<style scoped>
.image {
  width: 60%;
  display: block;
  margin: 0 auto 30px auto;
}
</style>

<script>
import Cookies from "js-cookie";
import qs from "qs";
import mixinViewModule from "@/mixins/view-module";
import {SITE_CONFIG} from "@/main.config";

export default {
    name: "RenProcessDetail",
    mixins: [mixinViewModule],
    data() {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/act/his/getTaskHandleDetailInfo",
                getDataListIsPage: false,
                createdIsNeed: false,
                deleteIsBatch: true,
                deleteIsBatchKey: "deploymentId"
            },
            dataForm: {
                processInstanceId: ""
            }
        };
    },
    created() {
        this.dataForm.processInstanceId = this.$route.params.processInstanceId;
        this.getResourceURL();
        this.getDataList();
    },
    methods: {
    // 获取流程(xml/image)url地址
        getResourceURL() {
            var params = qs.stringify({
                token: Cookies.get("token"),
                processInstanceId: this.dataForm.processInstanceId
            });
            return `${SITE_CONFIG["apiURL"]}/act/his/getInstImage?${params}`;
        }
    }
};
</script>
