package io.renren.modules.enterprise.bcp12.entity;

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
 * 批量导入企业明细表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("B_CP12")
public class BCp12Entity  {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private Long cp1201;
    /**
     * CP11主键ID
     */
	private Long cp1101;
    /**
     * 校验结果(0待校验，1成功、2失败)
     */
	private String result;
    /**
     * 导入失败消息
     */
	private String msg;
    /**
     * 社会统一信用代码
     */
	private String corpcode;
    /**
     * 企业名称
     */
	private String corpname;
    /**
     * 注册地区
     */
	private String areacode;
    /**
     * 行业（数据字典）
     */
	private String industry;
    /**
     * 产业（数据字典）
     */
	private String estate;
    /**
     * 企业规模（数据字典）
     */
	private String scale;
    /**
     * 联系人手机号码
     */
	private String linkcellphone;
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