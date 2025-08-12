package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LackWorkerDto {

    @ApiModelProperty(value = "项目类别一")
    private String category1;

    @ApiModelProperty(value = "项目类别二")
    private String category2;

    @ApiModelProperty(value = "监测企业数")
    private Long cpCount;

    @ApiModelProperty(value = "本期缺工企业数")
    private Long nowLackCpCount;

    @ApiModelProperty(value = "本期缺工人数")
    private Long nowLackWorkerCount;

    @ApiModelProperty(value = "本期缺工企业占比")
    private String nowLackCpCountRange;

    @ApiModelProperty(value = "上期缺工企业数")
    private Long preLackCpCount;

    @ApiModelProperty(value = "上期缺工人数")
    private Long preLackWorkerCount;

    @ApiModelProperty(value = "上期缺工企业占比")
    private String preLackCpCountRange;

    @ApiModelProperty(value = "年初缺工企业数")
    private Long beginLackCpCount;

    @ApiModelProperty(value = "年初缺工人数")
    private Long beginLackWorkerCount;

    @ApiModelProperty(value = "年初缺工企业占比")
    private String beginLackCpCountRange;

    @ApiModelProperty(value = "数据列表")
    private List<LackWorkerDto> list;
}
