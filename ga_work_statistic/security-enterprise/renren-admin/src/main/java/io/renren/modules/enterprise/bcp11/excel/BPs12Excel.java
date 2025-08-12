package io.renren.modules.enterprise.bcp11.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 人员导入记录表
 *
 * @author hx 
 * @since 1.0.0 2021-05-18
 */
@Data
public class BPs12Excel {
    @Excel(name = "主键id")
    private Long ps1201;
    @Excel(name = "工人id")
    private Long ps0201;
    @Excel(name = "导入类型 1、工人基础信息 2、头像信息")
    private String imptype;
    @Excel(name = "校验结果(1成功、2失败)")
    private String result;
    @Excel(name = "导入失败消息")
    private String msg;
    @Excel(name = "创建者")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;
    @Excel(name = "头像路径")
    private String imgurl;
    @Excel(name = "主表id")
    private Long ps1101;
    @Excel(name = "姓名")
    private String personName;
    @Excel(name = "人员类型")
    private String personType;
    @Excel(name = "身份证号码")
    private String idcardnumber;
    @Excel(name = "民族")
    private String nation;
    @Excel(name = "性别")
    private String gender;
    @Excel(name = "进场时间",importFormat = "yyyy/MM/dd HH:mm:ss")
    private String startdate;
    @Excel(name = "住址")
    private String address;
    @Excel(name = "序号")
    private String rownumber;
    @Excel(name = "手机号码")
    private String cellphone;
    @Excel(name = "出生日期")
    private String birthday;
    @Excel(name = "银行卡号码")
    private String payrollbankcardnumber;
    @Excel(name = "工资卡银行代码")
    private String payrolltopbankcode;
    @Excel(name = "工资卡开户行名称")
    private String payrollbankname;

}