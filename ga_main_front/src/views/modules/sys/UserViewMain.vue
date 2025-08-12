<template>
  <div class="view">
    <CFormQuery
      v-model="userModule.formDataQuery"
      :configs="userModule.configsCFormQuery"
      @query="userModule.query"
    />
    <Card>
      <div class="option-btn-wrapper">
        <BtnGroup
          :configs="userModule.configsBtns"
          :can-delete="userModule.dataListSelections"
        />
      </div>
      <el-table
        v-loading="userModule.dataListLoading"
        :data="userModule.dataList"
        border
        style="width: 100%"
        @selection-change="userModule.dataListSelectionChangeHandle"
        @sort-change="userModule.dataListSortChangeHandle"
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
          show-overflow-tooltip
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
        <el-table-column
          :label="$t('user.gender')"
          prop="gender"
          sortable="custom"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            {{ $getDictLabel("GENDER", scope.row.gender) }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('user.status')"
          prop="status"
          sortable="custom"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.status === 0"
              size="small"
              type="danger"
            >
              {{ $t("user.status0") }}
            </el-tag>
            <el-tag
              v-else
              size="small"
              type="success"
            >
              {{ $t("user.status1") }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('user.createDate')"
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
            <BtnGroup
              :configs="userModule.configsListBtns"
              :scope="scope"
            />
          </template>
        </el-table-column>
      </el-table>
      <Paginationbar
        v-model="userModule.pageInfo"
        @query="userModule.query"
      />
    </Card>
  </div>
</template>

<script>
export default {
  inject: ["userModule"],
};
</script>

<style>
</style>