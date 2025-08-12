package io.renren.modules.enterprise.bcp02.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 修改企业基础信息审核表
 *
 * @author JY 
 * @since 1.0.0 2025-08-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("B_CP02")
public class BCp02Entity  {
	private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
	private Long cp0201;
    /**
     * cp01表主键
     */
	private Long cp0101;
    /**
     * 社会统一信用代码
     */
	private String corpcode;
    /**
     * 企业名称
     */
	private String corpname;
    /**
     * 单位性质
     */
	private String organizationtype;
    /**
     * 注册地区
     */
	private String areacode;
    /**
     * 企业营业地址
     */
	private String address;
    /**
     * 邮政编码
     */
	private String zipcode;
    /**
     * 法定代表人姓名
     */
	private String legalman;
    /**
     * 法定代表人证件类型
     */
	private String idcardtype;
    /**
     * 法定代表人证件号码
     */
	private String legalmanidcardnumber;
    /**
     * 注册资本(万元)
     */
	private Double regcapital;
    /**
     * 注册资本币种
     */
	private String capitalcurrencytype;
    /**
     * 成立日期
     */
	private Date establishdate;
    /**
     * 办公电话
     */
	private String officephone;
    /**
     * 传真号码
     */
	private String faxnumber;
    /**
     * 联系人姓名
     */
	private String linkman;
    /**
     * 联系人办公电话
     */
	private String linkphone;
    /**
     * 联系人手机号码
     */
	private String linkcellphone;
    /**
     * 企业联系邮箱
     */
	private String email;
    /**
     * 企业网址
     */
	private String website;
    /**
     * 企业经营状态
     */
	private String businessstatus;
    /**
     * 经营范围
     */
	private String entscope;
    /**
     * 登记机关
     */
	private String regdept;
    /**
     * 企业备注
     */
	private String memo;
    /**
     * 机构ID
     */
	private Long deptId;
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
     * 审核状态（0、待审核 1、审核通过 2、审核不通过）
     */
	private String cp02Status;
    /**
     * 审核不通过原因
     */
	private String reason;
	/**
	 * 企业启用状态（数据字典）
	 */
	private String ablestatus;
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