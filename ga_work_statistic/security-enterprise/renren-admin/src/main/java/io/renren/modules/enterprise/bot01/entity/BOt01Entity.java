package io.renren.modules.enterprise.bot01.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 附件数据表
 *
 * @author CJF
 * @since 1.0.0 2025-08-05
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("B_OT01")
public class BOt01Entity  {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private Long ot0101;
    /**
     * 业务类型(dic)
     */
	private String busitype;
    /**
     * 业务编号
     */
	private Long busisysno;
    /**
     * 附件名称
     */
	private String name;
    /**
     * 附件路径
     */
	private String url;
    /**
     * 附件类型(前端处理展示信息)
     */
	private String viewType;
    /**
     * 附件原始名称
     */
	private String originalName;
    /**
     * 是否可用(删除标志)
     */
	private String whether;
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