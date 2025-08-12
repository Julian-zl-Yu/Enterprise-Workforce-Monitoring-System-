package io.renren.modules.enterprise.bcp03config.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业用工缺工情况配置表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
public class BCp03ConfigExcel {
    @Excel(name = "主键ID")
    private BigDecimal id;
    @Excel(name = "开始日")
    private String startday;
    @Excel(name = "结束日")
    private String endday;

}