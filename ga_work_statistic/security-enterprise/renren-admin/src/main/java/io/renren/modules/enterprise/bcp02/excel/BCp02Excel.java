package io.renren.modules.enterprise.bcp02.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改企业基础信息审核表
 *
 * @author JY 
 * @since 1.0.0 2025-08-03
 */
@Data
public class BCp02Excel {
    @Excel(name = "主键")
    private BigDecimal cp0201;
    @Excel(name = "cp01表主键")
    private BigDecimal cp0101;
    @Excel(name = "社会统一信用代码")
    private String corpcode;
    @Excel(name = "企业名称")
    private String corpname;
    @Excel(name = "单位性质")
    private String organizationtype;
    @Excel(name = "注册地区")
    private String areacode;
    @Excel(name = "企业营业地址")
    private String address;
    @Excel(name = "邮政编码")
    private String zipcode;
    @Excel(name = "法定代表人姓名")
    private String legalman;
    @Excel(name = "法定代表人证件类型")
    private String idcardtype;
    @Excel(name = "法定代表人证件号码")
    private String legalmanidcardnumber;
    @Excel(name = "注册资本(万元)")
    private BigDecimal regcapital;
    @Excel(name = "注册资本币种")
    private String capitalcurrencytype;
    @Excel(name = "成立日期")
    private Date establishdate;
    @Excel(name = "办公电话")
    private String officephone;
    @Excel(name = "传真号码")
    private String faxnumber;
    @Excel(name = "联系人姓名")
    private String linkman;
    @Excel(name = "联系人办公电话")
    private String linkphone;
    @Excel(name = "联系人手机号码")
    private String linkcellphone;
    @Excel(name = "企业联系邮箱")
    private String email;
    @Excel(name = "企业网址")
    private String website;
    @Excel(name = "企业经营状态")
    private String businessstatus;
    @Excel(name = "经营范围")
    private String entscope;
    @Excel(name = "登记机关")
    private String regdept;
    @Excel(name = "企业备注")
    private String memo;
    @Excel(name = "创建者")
    private BigDecimal creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private BigDecimal updater;
    @Excel(name = "更新时间")
    private Date updateDate;
    @Excel(name = "机构ID")
    private BigDecimal deptId;
    @Excel(name = "审核状态（0、待审核 1、审核通过 2、审核不通过）")
    private String cp02Status;
    @Excel(name = "审核不通过原因")
    private String reason;

}