package io.renren.modules.enterprise.bcp03config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 企业用工缺工情况配置表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
@ApiModel(value = "企业用工缺工情况配置表")
public class BCp03ConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

		@ApiModelProperty(value = "主键ID")
	private BigDecimal id;
		@ApiModelProperty(value = "开始日")
	private String startday;
		@ApiModelProperty(value = "结束日")
	private String endday;

}