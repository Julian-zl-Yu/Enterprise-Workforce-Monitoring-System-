package io.renren.modules.enterprise.bcp01.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.modules.enterprise.bot01.dto.BOt01DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
import java.util.List;

/**
 * 企业基础信息
 *
 * @author JY
 * @since 1.0.0 2025-07-29
 */
@Data
@ApiModel(value = "企业基础信息")
public class BCp01DTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long cp0101;
    @ApiModelProperty(value = "社会统一信用代码")
    private String corpcode;
    @ApiModelProperty(value = "企业名称")
    private String corpname;
    @ApiModelProperty(value = "单位性质")
    private String organizationtype;
    @ApiModelProperty(value = "注册地区")
    private String areacode;
    @ApiModelProperty(value = "企业营业地址")
    private String address;
    @ApiModelProperty(value = "邮政编码")
    private String zipcode;
    @ApiModelProperty(value = "法定代表人姓名")
    private String legalman;
    @ApiModelProperty(value = "法定代表人证件类型")
    private String idcardtype;
    @ApiModelProperty(value = "法定代表人证件号码")
    private String legalmanidcardnumber;
    @ApiModelProperty(value = "注册资本(万元)")
    private Double regcapital;
    @ApiModelProperty(value = "注册资本币种")
    private String capitalcurrencytype;
    @ApiModelProperty(value = "成立日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date establishdate;
    @ApiModelProperty(value = "办公电话")
    private String officephone;
    @ApiModelProperty(value = "传真号码")
    private String faxnumber;
    @ApiModelProperty(value = "联系人姓名")
    private String linkman;
    @ApiModelProperty(value = "联系人办公电话")
    private String linkphone;
    @ApiModelProperty(value = "联系人手机号码")
    private String linkcellphone;
    @ApiModelProperty(value = "企业联系邮箱")
    private String email;
    @ApiModelProperty(value = "企业网址")
    private String website;
    @ApiModelProperty(value = "企业经营状态")
    private String businessstatus;
    @ApiModelProperty(value = "经营范围")
    private String entscope;
    @ApiModelProperty(value = "登记机关")
    private String regdept;
    @ApiModelProperty(value = "企业备注")
    private String memo;
    @ApiModelProperty(value = "机构ID")
    private Long deptId;
    @ApiModelProperty(value = "用户电话")
    private String mobile;
    @ApiModelProperty(value = "附件", required = false)
    @Valid
    private List<BOt01DTO> ot01DTOList;
    @ApiModelProperty(value = "行业（数据字典）")
    private String industry;
    @ApiModelProperty(value = "产业（数据字典）")
    private String estate;
    @ApiModelProperty(value = "企业规模（数据字典）")
    private String scale;
    @ApiModelProperty(value = "企业启用状态（数据字典）")
    private String ablestatus;
}