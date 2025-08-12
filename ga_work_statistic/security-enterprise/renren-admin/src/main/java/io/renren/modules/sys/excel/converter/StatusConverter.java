package io.renren.modules.sys.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class StatusConverter implements Converter<Integer> {
        @Override
        public Class supportJavaTypeKey() {
            return Integer.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            return cellData.getStringValue().equals("正常") ? 1 : 0;
        }

        @Override
        public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            return new CellData(value == 1 ? "正常" : "停用");
        }
    }