package io.renren.modules.enterprise.bcp03.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 企业用工缺工情况表
 *
 * @author JY 
 * @since 1.0.0 2025-08-06
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("B_CP03")
public class BCp03Entity  {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private Long cp0301;
    /**
     * 企业信息表ID
     */
	private Long cp0101;
    /**
     * 管理人员用工数量
     */
	private Long useController;
    /**
     * 普通工人用工数量
     */
	private Long useOrdWorker;
    /**
     * 技术工人用工数量
     */
	private Long useTechWorker;
    /**
     * 技能工人用工数量
     */
	private Long useSkillWorker;
    /**
     * 管理人员缺工数量
     */
	private Long lackController;
    /**
     * 普通人员缺工数量
     */
	private Long lackOrdWorker;
    /**
     * 技术人员缺工数量
     */
	private Long lackTechWorker;
    /**
     * 技能人员缺工数量
     */
	private Long lackSkillWorker;
	/**
	 * 填报年月
	 */
	private String yearMonth;
	/**
	 * 审核状态（码表 0、未审核 1、审核通过 2、审核不通过）
	 */
	private String examineStatus;

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