package io.renren.modules.enterprise.bcp11.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 批量导入企业主表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
@Data
@ApiModel(value = "批量导入企业主表")
public class BCp11DTO implements Serializable {
    private static final long serialVersionUID = 1L;

		@ApiModelProperty(value = "主键ID")
	private BigDecimal cp1101;
		@ApiModelProperty(value = "导入时间")
	private Date impdate;
		@ApiModelProperty(value = "导入条数")
	private String importnumber;
		@ApiModelProperty(value = "成功条数")
	private String successnumber;
		@ApiModelProperty(value = "成功条数")
	private String failnumber;
		@ApiModelProperty(value = "操作员ID")
	private BigDecimal userId;
		@ApiModelProperty(value = "是否完成保存（写入cp01，创建账号）")
	private String result;
				
}