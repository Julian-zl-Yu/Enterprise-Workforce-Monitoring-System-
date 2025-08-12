<template>
    <div class="view">
        <CFormQuery
                v-model="cp02.formDataQuery"
                :configs="cp02.configsCFormQuery"
                @query="cp02.getTableList"
        />
        <Card>
            <el-table
                    v-loading="cp02.dataListLoading"
                    :data="cp02.dataList"
                    border
                    style="width: 100%"
                    @selection-change="cp02.dataListSelectionChangeHandle"
            >
                <!-- 选择列 -->
                <el-table-column
                        type="selection"
                        header-align="center"
                        align="center"
                        width="50"
                />
                <!-- 业务列 -->
                <TableColumn
                        v-for="header in cp02.DSLTableHeader"
                        :key="header.prop"
                        :configs="header"
                />
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
                                :configs="cp02.configsListBtns"
                                :scope="scope"
                        />
                    </template>
                </el-table-column>
            </el-table>
            <Paginationbar
                    v-model="cp02.pageInfo"
                    @query="cp02.query"
            />
        </Card>
    </div>
</template>

<script>
    export default {
        inject: ["cp02"],
    };
</script>

<style>
</style>