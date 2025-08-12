package io.renren.modules.enterprise.bcp11.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批量导入企业主表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("B_CP11")
public class BCp11Entity  {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId
	private Long cp1101;
    /**
     * 导入时间
     */
	private Date impdate;
    /**
     * 导入条数
     */
	private String importnumber;
    /**
     * 成功条数
     */
	private String successnumber;
    /**
     * 成功条数
     */
	private String failnumber;
    /**
     * 操作员ID
     */
	private Long userId;
    /**
     * 是否完成保存（写入cp01，创建账号）
     */
	private String result;
	/**
     * 创建者
     */
	@TableField(fill = FieldFill.INSERT)
	private Long  creator;
	/**
     * 创建时间
     */
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}