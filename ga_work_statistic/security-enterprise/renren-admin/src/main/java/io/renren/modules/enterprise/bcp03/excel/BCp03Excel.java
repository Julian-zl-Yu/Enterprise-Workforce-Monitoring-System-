package io.renren.modules.enterprise.bcp03.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业用工缺工情况表
 *
 * @author JY 
 * @since 1.0.0 2025-08-06
 */
@Data
public class BCp03Excel {
    @Excel(name = "主键ID")
    private BigDecimal cp0301;
    @Excel(name = "企业信息表ID")
    private BigDecimal cp0101;
    @Excel(name = "管理人员用工数量")
    private BigDecimal useController;
    @Excel(name = "普通工人用工数量")
    private BigDecimal useOrdWorker;
    @Excel(name = "技术工人用工数量")
    private BigDecimal useTechWorker;
    @Excel(name = "技能工人用工数量")
    private BigDecimal useSkillWorker;
    @Excel(name = "管理人员缺工数量")
    private BigDecimal lackController;
    @Excel(name = "普通人员缺工数量")
    private BigDecimal lackOrdWorker;
    @Excel(name = "技术人员缺工数量")
    private BigDecimal lackTechWorker;
    @Excel(name = "技能人员缺工数量")
    private BigDecimal lackSkillWorker;
    @Excel(name = "创建者")
    private BigDecimal creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private BigDecimal updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}