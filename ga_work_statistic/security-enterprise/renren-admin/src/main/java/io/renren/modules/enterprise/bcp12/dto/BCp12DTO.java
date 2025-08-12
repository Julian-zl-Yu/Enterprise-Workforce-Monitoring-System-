package io.renren.modules.enterprise.bcp12.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 批量导入企业明细表
 *
 * @author JY
 * @since 1.0.0 2025-08-08
 */
@Data
@ApiModel(value = "批量导入企业明细表")
public class BCp12DTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long cp1201;
    @ApiModelProperty(value = "CP11主键ID")
    private Long cp1101;
    @ApiModelProperty(value = "校验结果(0待校验，1成功、2失败)")
    private String result;
    @ApiModelProperty(value = "导入失败消息")
    private String msg;
    @ApiModelProperty(value = "社会统一信用代码")
    private String corpcode;
    @ApiModelProperty(value = "企业名称")
    private String corpname;
    @ApiModelProperty(value = "注册地区")
    private String areacode;
    @ApiModelProperty(value = "行业（数据字典）")
    private String industry;
    @ApiModelProperty(value = "产业（数据字典）")
    private String estate;
    @ApiModelProperty(value = "企业规模（数据字典）")
    private String scale;
    @ApiModelProperty(value = "联系人手机号码")
    private String linkcellphone;

}