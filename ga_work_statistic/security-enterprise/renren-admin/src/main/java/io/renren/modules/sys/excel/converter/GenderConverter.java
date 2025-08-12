package io.renren.modules.sys.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class GenderConverter implements Converter<Integer> {
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
            if(cellData.getStringValue().equals("男")){
                return 0;
            }else if(cellData.getStringValue().equals("女")){
                return 1;
            }else {
                return 2;
            }
        }

        @Override
        public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            if(value == 0){
                return new CellData("男");
            }else if(value == 1){
                return new CellData("女");
            }else {
                return new CellData("保密");
            }
        }
    }