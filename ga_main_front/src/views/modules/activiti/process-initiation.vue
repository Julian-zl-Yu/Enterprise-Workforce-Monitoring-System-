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
            v-model="dataForm.processName" 
            :placeholder="$t('process.name')" 
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="dataForm.key" 
            :placeholder="$t('process.key')" 
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
          :label="$t('process.id')" 
          prop="id" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.deploymentId')" 
          prop="deploymentId" 
          header-align="center" 
          align="center" 
          width="80"
        />
        <el-table-column 
          :label="$t('process.name')" 
          prop="name" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.key')" 
          prop="key" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.version')" 
          prop="version" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('process.resourceName')" 
          :show-overflow-tooltip="true" 
          prop="resourceName" 
          header-align="center" 
          align="center" 
          width="100"
        >
          <template slot-scope="scope">
            <a 
              :href="getResourceURL(scope.row.deploymentId, scope.row.resourceName)" 
              target="_blank"
            >{{ scope.row.resourceName }}</a>
          </template>
        </el-table-column>
        <el-table-column 
          :label="$t('process.diagramResourceName')" 
          :show-overflow-tooltip="true" 
          prop="diagramResourceName" 
          header-align="center" 
          align="center" 
          width="100"
        >
          <template slot-scope="scope">
            <a 
              :href="getResourceURL(scope.row.deploymentId, scope.row.diagramResourceName)" 
              target="_blank"
            >{{ scope.row.diagramResourceName }}</a>
          </template>
        </el-table-column>
        <el-table-column 
          :label="$t('process.deploymentTime')" 
          prop="deploymentTime" 
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
              type="text" 
              size="small" 
              @click="createProcessInstance(scope.row)"
            >
              {{ $t('process.createInstance') }}
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
import Cookies from "js-cookie";
import { addDynamicRoute } from "@/router";
import qs from "qs";
import {SITE_CONFIG} from "@/main.config";

export default {
    components: {
    },
    mixins: [mixinViewModule],
    data () {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/act/process/lastestPage",
                getDataListIsPage: true,
                deleteURL: "/act/process",
                deleteIsBatch: true,
                deleteIsBatchKey: "deploymentId"
            },
            dataForm: {
                processName: "",
                key: ""
            },
            deployVisible: false
        };
    },
    methods: {
    // 获取流程(xml/image)url地址
        getResourceURL (id, name) {
            var params = qs.stringify({
                "token": Cookies.get("token"),
                "deploymentId": id,
                "resourceName": name
            });
            return `${SITE_CONFIG["apiURL"]}/act/process/resource?${params}`;
        },
        // 发起流程
        createProcessInstance (row) {
            this.$http.get(`/act/process/getProcDefBizRoute/${row.id}`).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                if (!res.data || !res.data.bizRoute) {
                    return this.$message.error(this.$t("process.routeError"));
                }
                this.forwardUrl(res.data, row);
            }).catch(error => console.error(error));
        },
        forwardUrl (data, row) {
            var routeParams = {
                routeName: `${this.$route.name}__instance_${data.id}`,
                menuId: `${this.$route.meta.menuId}`,
                title: `${this.$route.meta.title} - ${row.name}`,
                path: data.bizRoute,
                params: {
                    processDefinitionId: data.procDefId,
                    processDefinitionKey: row.key,
                    processShowType: "start"
                }
            };
            addDynamicRoute(routeParams, this.$router, this.$route);
        },
        // 启动其他流程
        startProcess (key) {
            var params = qs.stringify({
                "key": key
            });
            this.$http.post("/act/running/start", params).then(({ data: res }) => {
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
        }
    }
};
</script>
