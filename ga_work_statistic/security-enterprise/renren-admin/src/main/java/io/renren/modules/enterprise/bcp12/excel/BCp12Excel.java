package io.renren.modules.enterprise.bcp12.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批量导入企业明细表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
public class BCp12Excel {
    @Excel(name = "主键ID")
    private BigDecimal cp1201;
    @Excel(name = "CP11主键ID")
    private BigDecimal cp1101;
    @Excel(name = "校验结果(0待校验，1成功、2失败)")
    private String result;
    @Excel(name = "导入失败消息")
    private String msg;
    @Excel(name = "创建者")
    private BigDecimal creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private BigDecimal updater;
    @Excel(name = "更新时间")
    private Date updateDate;
    @Excel(name = "社会统一信用代码")
    private String corpcode;
    @Excel(name = "企业名称")
    private String corpname;
    @Excel(name = "注册地区")
    private String areacode;
    @Excel(name = "行业（数据字典）")
    private String industry;
    @Excel(name = "产业（数据字典）")
    private String estate;
    @Excel(name = "企业规模（数据字典）")
    private String scale;
    @Excel(name = "联系人手机号码")
    private String linkcellphone;

}