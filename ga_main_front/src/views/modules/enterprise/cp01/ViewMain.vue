<template>
  <div class="view">
    <CFormQuery
      v-model="cp01.formDataQuery"
      :configs="cp01.configsCFormQuery"
      @query="cp01.getTableList"
    />
    <Card>
      <el-table
        v-loading="cp01.dataListLoading"
        :data="cp01.dataList"
        border
        style="width: 100%"
        @selection-change="cp01.dataListSelectionChangeHandle"
      >
        <!-- 选择列 -->
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50"
        />
        <!-- 业务列 -->
        <template v-for="header in cp01.DSLTableHeader">
          <TableColumn
            :key="header.prop"
            :configs="header"
          />
        </template>
        <!-- 固定列 -->
        <el-table-column
          :label="$t('handle')"
          fixed="right"
          header-align="center"
          align="center"
          width="150"
        >
          <template slot-scope="scope">
            <BtnGroup
              :configs="cp01.configsListBtns"
              :scope="scope"
            />
          </template>
        </el-table-column>
      </el-table>
      <Paginationbar
        v-model="cp01.pageInfo"
        @query="cp01.query"
      />
    </Card>
  </div>
</template>

<script>
export default {
  inject: ["cp01"],
};
</script>

<style>
</style>