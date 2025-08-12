package io.renren.modules.enterprise.bcp03config.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业用工缺工情况配置表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("B_CP03_CONFIG")
public class BCp03ConfigEntity  {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private BigDecimal id;
    /**
     * 开始日
     */
	private String startday;
    /**
     * 结束日
     */
	private String endday;
}