package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ByEstateDto {

    @ApiModelProperty(value = "产业")
    private String estate;

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

    @ApiModelProperty(value = "数据列表")
    List<ByEstateDto> list;
}
