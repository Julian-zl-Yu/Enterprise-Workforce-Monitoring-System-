package io.renren.common.utils.aliyun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业工商信息
 *
 * @Author huangxi
 * @Date 2020-08-20 15:19
 */
@Data
@ApiModel(value = "企业工商信息")
public class CompanyDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "曾用名")
    private String companyNameold;

    @ApiModelProperty(value = "公司名称-英文")
    private String companyNameEn;

    @ApiModelProperty(value = "信用代码")
    private String creditCode;

    @ApiModelProperty(value = "组织机构代码")
    private String companyCode;

    @ApiModelProperty(value = "工商注册号")
    private String regNumber;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxNumber;

    @ApiModelProperty(value = "企业类型")
    private String regType;

    @ApiModelProperty(value = "登记机关")
    private String regOrgName;

    @ApiModelProperty(value = "法人")
    private String faRen;

    @ApiModelProperty(value = "法人类型")
    private String companyType;

    @ApiModelProperty(value = "企业状态")
    private String businessStatus;

    @ApiModelProperty(value = "核准时间")
    private String chkDate;

    @ApiModelProperty(value = "成立日期")
    private String issueTime;

    @ApiModelProperty(value = "营业开始日期")
    private String regDate;

    @ApiModelProperty(value = "营业结束日期")
    private String bussiness;

    @ApiModelProperty(value = "注销日期")
    private String cancelDate;

    @ApiModelProperty(value = "注册资本")
    private String regMoney;

    @ApiModelProperty(value = "注册地址")
    private String address;

    @ApiModelProperty(value = "经营范围")
    private String bussinessDes;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "电话列表")
    private String phonelist;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "邮箱列表")
    private String emaillist;

    @ApiModelProperty(value = "公司网址")
    private String webSite;

    @ApiModelProperty(value = "网址列表")
    private String webSitelist;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "所属行业")
    private String industry;
}
