package io.renren.modules.enterprise.bcp03.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业用工缺工情况表
 *
 * @author JY
 * @since 1.0.0 2025-08-06
 */
@Data
@ApiModel(value = "企业用工缺工情况表")
public class BCp03DTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long cp0301;
    @ApiModelProperty(value = "企业信息表ID")
    private Long cp0101;
    @ApiModelProperty(value = "企业名称")
    private String corpname;
    @ApiModelProperty(value = "注册地区")
    private String areacode;
    @ApiModelProperty(value = "管理人员用工数量")
    private Long useController;
    @ApiModelProperty(value = "普通工人用工数量")
    private Long useOrdWorker;
    @ApiModelProperty(value = "技术工人用工数量")
    private Long useTechWorker;
    @ApiModelProperty(value = "技能工人用工数量")
    private Long useSkillWorker;
    @ApiModelProperty(value = "管理人员缺工数量")
    private Long lackController;
    @ApiModelProperty(value = "普通人员缺工数量")
    private Long lackOrdWorker;
    @ApiModelProperty(value = "技术人员缺工数量")
    private Long lackTechWorker;
    @ApiModelProperty(value = "技能人员缺工数量")
    private Long lackSkillWorker;
    @ApiModelProperty(value = "缺工总数")
    private Long allLackWorker;
    @ApiModelProperty(value = "用工总数")
    private Long allUseWorker;
	@ApiModelProperty(value = "填报年月")
	private String yearMonth;
    @ApiModelProperty(value = "审核状态（码表 0、未审核 1、审核通过 2、审核不通过）")
    private String examineStatus;
    @ApiModelProperty(value = "行业（数据字典）")
    private String industry;
    @ApiModelProperty(value = "产业（数据字典）")
    private String estate;
    @ApiModelProperty(value = "企业规模（数据字典）")
    private String scale;
}