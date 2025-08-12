<template>
  <div class="view">
    <Card>
      <div class="option-btn-wrapper">
        <BtnGroup
          :configs="ps11.configsBtns"
          :can-delete="ps11.dataListSelections"
        />
        <br/>
        <span style="color: red">
          第一步：点击导入检验，校验导入数据有效性；第二步：点击保存数据，将数据同步
        </span>
      </div>
   
      <el-table
        v-loading="ps11.dataListLoading"
        :data="ps11.dataList"
        border
        style="width: 100%"
        @selection-change="ps11.dataListSelectionChangeHandle"
      >
        <!-- 选择列 -->
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50"
        />
        <!-- 业务列 -->
        <template v-for="header in ps11.DSLTableHeader">
          <TableColumn
            :key="header.prop"
            :configs="header"
          />
        </template>
      </el-table>
      <Paginationbar
        v-model="ps11.pageInfo"
        @query="ps11.query"
      />
    </Card>
  </div>
</template>

<script>
export default {
  inject: ["ps11"],
};
</script>

<style>
</style>