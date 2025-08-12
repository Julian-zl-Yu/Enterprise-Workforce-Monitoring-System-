<template>
  <el-card 
    shadow="never" 
    class="app-card--fill"
  >
    <div class="mod-demo__syssmslog">
      <el-form 
        :inline="true" 
        :model="dataForm" 
        @keyup.enter.native="getDataList()"
      >
        <el-form-item>
          <el-input 
            v-model="dataForm.smsCode" 
            :placeholder="$t('sms.smsCode')" 
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="dataForm.mobile" 
            :placeholder="$t('sms.mobile')" 
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-select 
            v-model="dataForm.status" 
            :placeholder="$t('sms.status')" 
            clearable
          >
            <el-option 
              :label="$t('sms.status1')" 
              :value="1"
            />
            <el-option 
              :label="$t('sms.status0')" 
              :value="0"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">
            {{ $t('query') }}
          </el-button>
        </el-form-item>
        <!-- <el-form-item>
          <el-button 
            v-if="$hasPermission('sys:smslog:all')" 
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
          :label="$t('sms.smsCode')" 
          prop="smsCode" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('sms.mobile')" 
          prop="mobile" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('sms.params1')" 
          prop="params1" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('sms.params2')" 
          prop="params2" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('sms.params3')" 
          prop="params3" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('sms.params4')" 
          prop="params4" 
          header-align="center" 
          align="center"
        />
        <el-table-column 
          :label="$t('sms.status')" 
          prop="status" 
          header-align="center" 
          align="center"
        >
          <template slot-scope="scope">
            <el-tag 
              v-if="scope.row.status === 1" 
              size="small"
            >
              {{ $t('sms.status1') }}
            </el-tag>
            <el-tag 
              v-else 
              size="small" 
              type="danger"
            >
              {{ $t('sms.status0') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column 
          :label="$t('sms.createDate')" 
          prop="createDate" 
          header-align="center" 
          align="center" 
          width="170"
        />
        <!-- <el-table-column 
          :label="$t('handle')" 
          fixed="right" 
          header-align="center" 
          align="center" 
          width="150"
        >
          <template slot-scope="scope">
            <el-button 
              v-if="$hasPermission('sys:smslog:all')" 
              type="text" 
              size="small" 
              @click="deleteHandle(scope.row.id)"
            >
              {{ $t('delete') }}
            </el-button>
          </template>
        </el-table-column> -->
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
export default {
    mixins: [mixinViewModule],
    data () {
        return {
            mixinViewModuleOptions: {
                getDataListURL: "/sys/smslog/page",
                getDataListIsPage: true,
                deleteURL: "/sys/smslog",
                deleteIsBatch: true
            },
            dataForm: {
                mobile: "",
                status: "",
                smsCode: ""
            }
        };
    }
};
</script>
