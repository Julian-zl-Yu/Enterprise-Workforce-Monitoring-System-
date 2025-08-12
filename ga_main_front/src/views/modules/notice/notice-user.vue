<template>
  <el-card shadow="never" class="app-card--fill">
    <div class="mod-demo__sysnoticeuser">
      <el-form
        :inline="true"
        :model="dataForm"
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <ren-select
            v-model="dataForm.type"
            :placeholder="$t('notice.type')"
            dict-type="NOTICE_TYPE"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">
            {{ $t("query") }}
          </el-button>
        </el-form-item>
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
          :label="$t('notice.title')"
          prop="title"
          header-align="center"
          align="center"
          width="600"
        />
        <el-table-column
          :label="$t('notice.type')"
          prop="type"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            <ren-select
            v-model="scope.row.type"
            :placeholder="$t('notice.type')"
            dict-type="NOTICE_TYPE"
            :readOnly='true'
          />
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('notice.senderName')"
          prop="senderName"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('notice.senderDate')"
          prop="senderDate"
          header-align="center"
          align="center"
        />
        <el-table-column
          :label="$t('notice.readStatus')"
          prop="readStatus"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.readStatus === 0"
              size="small"
              type="danger"
            >
              {{ $t("notice.readStatus0") }}
            </el-tag>
            <el-tag v-else size="small" type="success">
              {{ $t("notice.readStatus1") }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('handle')"
          fixed="right"
          header-align="center"
          align="center"
          width="150"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewHandle(scope.row)">
              {{ $t("notice.view") }}
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
import { addDynamicRoute } from "@/router";
export default {
  mixins: [mixinViewModule],
  data() {
    return {
      mixinViewModuleOptions: {
        activatedIsNeed: true,
        getDataListURL: "/sys/notice/mynotice/page",
        getDataListIsPage: true,
      },
      dataForm: {
        type: "",
      },
    };
  },
   watch: {
    "$store.state.isNoticeRefresh": {
      handler: function(val, oldVal) {
        this.query()
      },
      // 深度观察监听
      deep: true,
    },
  },
  mounted(){
 let xx  = this.$getDictLabel('NOTICE_TYPE', '0')
 console.log(`xx`, xx)
  },
  methods: {
    viewHandle(row) {
      // 路由参数
      const routeParams = {
        routeName: `${this.$route.name}__${row.id}`,
        title: this.$t("notice.view2"),
        path: "notice/notice-user-view",
        params: {
          id: row.id,
        },
      };

      // 如果未读，则标记为已读
      if (row.readStatus === 0) {
        this.updateReadStatus(row.id);
        this.$store.state.isNoticeRefresh = true
      }
      // 动态路由
      addDynamicRoute(routeParams, this.$router);
    },
    updateReadStatus(noticeId) {
      this.$http["put"]("/sys/notice/mynotice/read/" + noticeId)
        .then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg);
          }
        })
        .catch((error) => console.error(error));
    },
  },
};
</script>
