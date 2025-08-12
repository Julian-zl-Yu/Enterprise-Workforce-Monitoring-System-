package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YearMonthDto {

    @ApiModelProperty(value = "调查期")
    private String yearMonth;

    @ApiModelProperty(value = "工人总数")
    private Long allUseWorker;

}
