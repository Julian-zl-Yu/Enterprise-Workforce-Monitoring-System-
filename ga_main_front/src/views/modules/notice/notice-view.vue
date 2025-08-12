<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <div style="text-align: center;font-size: 28px">
      {{ dataForm.title }}
    </div>
    <el-divider />
    <div v-html="dataForm.content" />
    <div>
      <hr 
        size="1" 
        color="#ddd" 
        style="margin:30px 0 10px 0;"
      >
      <span><i 
        class="el-icon-user-solid" 
        style="color: #e6444a"
      /> {{ $t('notice.senderName') }}：{{ dataForm.senderName }}</span>
      <el-divider direction="vertical" />
      <span><i 
        class="el-icon-time" 
        style="color: #e6444a"
      /> {{ $t('notice.senderDate') }}：{{ dataForm.senderDate }}</span>
      <el-divider 
        direction="vertical" 
        style="margin: 0px;padding:0px;"
      />
      <span><i 
        class="el-icon-s-order" 
        style="color: #E6A23C"
      /> {{ $t('notice.type') }}：
        <template>
          {{ $getDictLabel("NOTICE_TYPE", dataForm.type) }}
        </template>
      </span>
      <hr 
        size="1" 
        color="#ddd" 
        style="margin:10px 0 30px 0;"
      >
    </div>
    <el-table 
      v-loading="dataListLoading" 
      :data="dataList" 
      border 
      style="width: 100%;" 
      @selection-change="dataListSelectionChangeHandle"
    >
      <el-table-column 
        :label="$t('notice.receiverName')" 
        prop="receiverName" 
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
            {{ $t('notice.readStatus0') }}
          </el-tag>
          <el-tag 
            v-else 
            size="small" 
            type="success"
          >
            {{ $t('notice.readStatus1') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column 
        :label="$t('notice.readDate')" 
        prop="readDate" 
        header-align="center" 
        align="center"
      />
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
    <el-divider />
    <div style="text-align: center;">
      <el-button 
        type="danger" 
        @click="closeCurrentTab()"
      >
        {{ $t('notice.close') }}
      </el-button>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
export default {
    mixins: [mixinViewModule],
    data () {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/sys/notice/user/page",
                createdIsNeed: true,
                activatedIsNeed: true,
                getDataListIsPage: true
            },
            dataForm: {
                id: this.$route.params.id
            }
        };
    },
    created () {
        this.dataForm.id = this.$route.params.id || 0;
        this.getInfo();
    },
    methods: {
    // 获取信息
        getInfo () {
            this.$http.get(`/sys/notice/${this.dataForm.id}`).then(({ data: res }) => {
                if (res.code !== 0) {
                    return this.$message.error(res.msg);
                }
                this.dataForm = {
                    ...this.dataForm,
                    ...res.data
                };
            }).catch(error => console.error(error));
        }
    }
};
</script>
