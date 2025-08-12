package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeneralSituationDto {

    @ApiModelProperty(value = "企业类型")
    private String corpType;

    @ApiModelProperty(value = "监测企业数")
    private Long changeWorkerCpCount;

    @ApiModelProperty(value = "较上月变化人数")
    private Long changeWorkerCount;

    @ApiModelProperty(value = "较上月变化幅度")
    private String changeWorkerRange;

    @ApiModelProperty(value = "较年初变化人数")
    private Long changeWorkerCountCompareBeginYear;

    @ApiModelProperty(value = "较年初变化幅度")
    private String changeWorkerRangeCompareBeginYear;

    @ApiModelProperty(value = "在岗工人数")
    private Long allWorkerCount;

    @ApiModelProperty(value = "企业数")
    private Long allCpCount;

    @ApiModelProperty(value = "表格数据")
    List<GeneralSituationDto> list;
}
