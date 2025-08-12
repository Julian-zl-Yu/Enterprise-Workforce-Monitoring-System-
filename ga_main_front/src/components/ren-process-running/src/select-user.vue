<template>
  <el-dialog 
    :visible.sync="visibleSelect" 
    :modal="false" 
    :title="$t('user.select')" 
    :close-on-click-modal="false" 
    :close-on-press-escape="false" 
    width="60%"
  >
    <div class="mod-sys__user">
      <el-form 
        :inline="true" 
        :model="dataForm" 
        size="mini" 
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-input 
            v-model="dataForm.username" 
            :placeholder="$t('user.username')" 
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
        size="mini"
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
          :label="$t('user.username')" 
          prop="username" 
          sortable="custom" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('user.deptName')" 
          prop="deptName" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('user.email')" 
          prop="email" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('user.mobile')" 
          prop="mobile" 
          sortable="custom" 
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
    </div>
    <template slot="footer">
      <el-button 
        type="default" 
        @click="cancelHandle()"
      >
        {{ $t('cancel') }}
      </el-button>
      <el-button 
        type="primary" 
        @click="commitHandle()"
      >
        {{ $t('confirm') }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import mixinViewModule from "@/mixins/view-module";
export default {
    components: {},
    mixins: [mixinViewModule],
    data () {
        return {
            visibleSelect: false,
            mixinViewModuleOptions: {
                getDataListURL: "/sys/user/page",
                getDataListIsPage: true
            },
            dataForm: {
                username: ""
            },
            callback: null
        };
    },
    methods: {
        init (callback) {
            this.visibleSelect = true;
            this.callback = callback;
            this.$nextTick(() => {
                this.getDataList();
            });
        },
        cancelHandle () {
            this.visibleSelect = false;
        },
        commitHandle () {
            if (this.callback) {
                if (this.dataListSelections.length !== 1) {
                    this.$message.error(this.$t("user.selecterror"));
                    return;
                } else {
                    this.callback(this.dataListSelections[0]);
                }
            }
            this.visibleSelect = false;
        }
    }
};
</script>
