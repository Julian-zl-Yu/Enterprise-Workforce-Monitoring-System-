package io.renren.modules.enterprise.bot01.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 附件数据表
 *
 * @author CJF
 * @since 1.0.0 2025-08-05
 */
@Data
public class BOt01Excel {
    @Excel(name = "主键ID")
    private BigDecimal ot0101;
    @Excel(name = "业务类型(dic)")
    private String busitype;
    @Excel(name = "业务编号")
    private BigDecimal busisysno;
    @Excel(name = "附件名称")
    private String name;
    @Excel(name = "附件路径")
    private String url;
    @Excel(name = "附件类型(前端处理展示信息)")
    private String viewType;
    @Excel(name = "附件原始名称")
    private String originalName;
    @Excel(name = "是否可用(删除标志)")
    private String whether;
    @Excel(name = "创建者")
    private BigDecimal creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private BigDecimal updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}