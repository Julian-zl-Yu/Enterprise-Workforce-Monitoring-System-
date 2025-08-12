<template>
  <div class="view">
    <CFormQuery
      v-model="pj07.formDataQuery"
      :configs="pj07.configsCFormQuery"
      @query="pj07.getTableList"
    />
    <Card>
      <el-table
        v-loading="pj07.dataListLoading"
        :data="pj07.dataList"
        border
        style="width: 100%"
        @selection-change="pj07.dataListSelectionChangeHandle"
      >
        <!-- 选择列 -->
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50"
        />
        <!-- 业务列 -->
        <template v-for="header in pj07.DSLTableHeader">
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
              :configs="pj07.configsListBtns"
              :scope="scope"
            />
          </template>
        </el-table-column>
      </el-table>
      <Paginationbar
        v-model="pj07.pageInfo"
        @query="pj07.query"
      />
    </Card>
  </div>
</template>

<script>
export default {
  inject: ["pj07"],
};
</script>

<style>
</style>