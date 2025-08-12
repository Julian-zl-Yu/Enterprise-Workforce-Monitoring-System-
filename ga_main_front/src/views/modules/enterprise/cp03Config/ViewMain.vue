<template>
  <div class="view">
    <Card>
      <el-table
        v-loading="cp03.dataListLoading"
        :data="cp03.dataList"
        border
        style="width: 100%"
        @selection-change="cp03.dataListSelectionChangeHandle"
      >
        <!-- 选择列 -->
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50"
        />
        <!-- 业务列 -->
        <template v-for="header in cp03.DSLTableHeader">
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
              :configs="cp03.configsListBtns"
              :scope="scope"
            />
          </template>
        </el-table-column>
      </el-table>
      <Paginationbar
        v-model="cp03.pageInfo"
        @query="cp03.query"
      />
    </Card>
  </div>
</template>

<script>
export default {
  inject: ["cp03"],
};
</script>

<style>
</style>