package io.renren.modules.enterprise.bot01.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 附件数据表
 *
 * @author CJF
 * @since 1.0.0 2025-08-05
 */
@Data
@ApiModel(value = "附件数据表")
public class BOt01DTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long ot0101;
    @ApiModelProperty(value = "业务类型(dic)")
    private String busitype;
    @ApiModelProperty(value = "业务编号")
    private Long busisysno;
    @ApiModelProperty(value = "附件名称")
    private String name;
    @ApiModelProperty(value = "附件路径")
    private String url;
    @ApiModelProperty(value = "附件类型(前端处理展示信息)")
    private String viewType;
    @ApiModelProperty(value = "附件原始名称")
    private String originalName;
    @ApiModelProperty(value = "是否可用(删除标志)")
    private String whether;

}