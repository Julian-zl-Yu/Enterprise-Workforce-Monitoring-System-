<template>
  <div class="view">
    <CFormQuery
      v-model="kq01.formDataQuery"
      :configs="kq01.configsCFormQuery"
      @query="kq01.getTableList"
    />
    <el-card
      shadow="never"
      class="aui-card--fill"
    >
      <div class="mod-enterprise__kq01">
        <div class="option-btn-wrapper">
          <BtnGroup
            :configs="kq01.configsBtns"
            :can-delete="kq01.dataListSelections"
          />
        </div>
        <el-table
          v-loading="kq01.dataListLoading"
          :data="kq01.dataList"
          border
          style="width: 100%"
          @selection-change="kq01.dataListSelectionChangeHandle"
        >
          <!-- 选择列 -->
          <!-- <el-table-column
            type="selection"
            header-align="center"
            align="center"
            width="50"
          /> -->
          <!-- 业务列 -->
          <template v-for="header in kq01.DSLTableHeader">
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
                :configs="kq01.configsListBtns"
                :scope="scope"
              />
            </template>
          </el-table-column>
        </el-table>
        <Paginationbar
          v-model="kq01.pageInfo"
          @query="kq01.query"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  inject: ["kq01"],
};
</script>

<style>
</style>