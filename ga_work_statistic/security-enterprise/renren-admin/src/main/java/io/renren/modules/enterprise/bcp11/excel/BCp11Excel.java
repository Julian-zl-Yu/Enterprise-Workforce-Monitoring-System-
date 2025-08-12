package io.renren.modules.enterprise.bcp11.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批量导入企业主表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
public class BCp11Excel {
    @Excel(name = "主键ID")
    private Long cp1101;
    @Excel(name = "导入时间")
    private Date impdate;
    @Excel(name = "导入条数")
    private String importnumber;
    @Excel(name = "成功条数")
    private String successnumber;
    @Excel(name = "成功条数")
    private String failnumber;
    @Excel(name = "操作员ID")
    private Long userId;
    @Excel(name = "是否完成保存（写入cp01，创建账号）")
    private String result;
    @Excel(name = "创建者")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;
    @Excel(name = "社会统一信用代码")
    private String corpcode;
    @Excel(name = "企业名称")
    private String corpname;
    @Excel(name = "注册地区")
    private String areacode;
    @Excel(name = "所属行业")
    private String industry;
    @Excel(name = "所属产业")
    private String estate;
    @Excel(name = "企业规模")
    private String scale;
    @Excel(name = "联系人电话（可作登录账号）")
    private String linkcellphone;

}