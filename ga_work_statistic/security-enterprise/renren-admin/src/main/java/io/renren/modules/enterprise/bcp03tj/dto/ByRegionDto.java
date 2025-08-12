package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ByRegionDto {

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "监测企业数")
    private Long cpCount;

    @ApiModelProperty(value = "上月工人数")
    private Long preWorkerCount;

    @ApiModelProperty(value = "当月工人数")
    private Long nowWorkerCount;

    @ApiModelProperty(value = "年初工人数")
    private Long beginWorkerCount;

    @ApiModelProperty(value = "比上月变化工人数")
    private Long changeWorkerCount;

    @ApiModelProperty(value = "比上月变化工人幅度")
    private String changeWorkerRange;

    @ApiModelProperty(value = "比年初变化工人数")
    private Long changeWorkerCountCompareBeginYear;

    @ApiModelProperty(value = "比年初变化工人幅度")
    private String changeWorkerRangeCompareBeginYear;

    @ApiModelProperty(value = "列表")
    List<ByRegionDto> list;
}
