package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ByYearMonthDto {

    @ApiModelProperty(value = "调查期")
    private String yearMonth;

    @ApiModelProperty(value = "上期工人数")
    private Long preWorkerCount;

    @ApiModelProperty(value = "本期工人数")
    private Long nowWorkerCount;

    @ApiModelProperty(value = "年初工人数")
    private Long beginWorkerCount;

    @ApiModelProperty(value = "比上期变化工人数")
    private Long changeWorkerCount;

    @ApiModelProperty(value = "比上期变化工人幅度")
    private String changeWorkerRange;

    @ApiModelProperty(value = "比年初变化工人数")
    private Long changeWorkerCountCompareBeginYear;

    @ApiModelProperty(value = "比年初变化工人幅度")
    private String changeWorkerRangeCompareBeginYear;

    @ApiModelProperty(value = "数据列表")
    private List<ByYearMonthDto> list;
}
