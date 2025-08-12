package io.renren.modules.enterprise.bcp03tj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * 总体分析DTO
 */

@Data
public class OverallAnalysisDTO {

    /**
    总体情况——本月监测企业数
     */
    @ApiModelProperty(value = "总体情况——本月监测企业数")
    private Long allCpCountThisMonth_1;
    /**
     总体情况——本月在岗工人数
     */
    @ApiModelProperty(value = "总体情况——本月在岗工人数")
    private Long allWorkerCountThisMonth_1;
    /**
     总体情况——较上月变化工人数
     */
    @ApiModelProperty(value = "总体情况——较上月变化工人数")
    private String difference_1;
    /**
     总体情况——较上月变化工人幅度
     */
    @ApiModelProperty(value = "总体情况——较上月变化工人幅度")
    private String differenceRange_1;
    /**
     总体情况——增员企业数
     */
    @ApiModelProperty(value = "总体情况——增员企业数")
    private Long addWorkerCpCount_1;
    /**
     总体情况——增员占企业幅度
     */
    @ApiModelProperty(value = "总体情况——增员占企业幅度")
    private String addWorkerCpRange_1;
    /**
     总体情况——增员企业工人增加数
     */
    @ApiModelProperty(value = "总体情况——增员企业工人增加数")
    private Long addWorkerCount_1;
    /**
     总体情况——增员企业工人增加幅度
     */
    @ApiModelProperty(value = "总体情况——增员企业工人增加幅度")
    private String addWorkerRange_1;
    /**
     总体情况——减员企业数
     */
    @ApiModelProperty(value = "总体情况——减员企业数")
    private Long reduceWorkerCpCount_1;
    /**
     总体情况——减员占企业幅度
     */
    @ApiModelProperty(value = "总体情况——减员占企业幅度")
    private String reduceWorkerCpRange_1;
    /**
     总体情况——减员企业工人增加数
     */
    @ApiModelProperty(value = "总体情况——减员企业工人增加数")
    private Long reduceWorkerCount_1;
    /**
     总体情况——减员企业工人增加幅度
     */
    @ApiModelProperty(value = "总体情况——减员企业工人增加幅度")
    private String reduceWorkerRange_1;

    /**
     产业情况——第一产业工人数量
     */
    @ApiModelProperty(value = "产业情况——第一产业工人数量")
    private String workerCount_2_1;
    /**
     产业情况——第一产业工人占比
     */
    @ApiModelProperty(value = "产业情况——第一产业工人占比")
    private String workerRange_2_1;

    /**
     产业情况——第一产业工人较上月变化数量
     */
    @ApiModelProperty(value = "产业情况——第一产业工人较上月变化数量")
    private String workerDifference_2_1;
    /**
     产业情况——第一产业工人较上月变化幅度
     */
    @ApiModelProperty(value = "产业情况——第一产业工人较上月变化幅度")
    private String workerDifferenceRange_2_1;
    /**
     产业情况——第一产业工人较年初变化数量
     */
    @ApiModelProperty(value = "产业情况——第一产业工人较年初变化数量")
    private String workerDifferenceBeginYear_2_1;
    /**
     产业情况——第一产业工人较年初变化幅度
     */
    @ApiModelProperty(value = "产业情况——第一产业工人较年初变化幅度")
    private String workerDifferenceRangeBeginYear_2_1;

    /**
     产业情况——第二产业工人数量
     */
    @ApiModelProperty(value = "产业情况——第二产业工人数量")
    private String workerCount_2_2;
    /**
     产业情况——第二产业工人占比
     */
    @ApiModelProperty(value = "产业情况——第二产业工人占比")
    private String workerRange_2_2;

    /**
     产业情况——第二产业工人较上月变化数量
     */
    @ApiModelProperty(value = "产业情况——第二产业工人较上月变化数量")
    private String workerDifference_2_2;
    /**
     产业情况——第二产业工人较上月变化幅度
     */
    @ApiModelProperty(value = "产业情况——第二产业工人较上月变化幅度")
    private String workerDifferenceRange_2_2;
    /**
     产业情况——第二产业工人较年初变化数量
     */
    @ApiModelProperty(value = "产业情况——第二产业工人较年初变化数量")
    private String workerDifferenceBeginYear_2_2;
    /**
     产业情况——第二产业工人较年初变化幅度
     */
    @ApiModelProperty(value = "产业情况——第二产业工人较年初变化幅度")
    private String workerDifferenceRangeBeginYear_2_2;

    /**
     产业情况——第三产业工人数量
     */
    @ApiModelProperty(value = "产业情况——第三产业工人数量")
    private String workerCount_2_3;
    /**
     产业情况——第三产业工人占比
     */
    @ApiModelProperty(value = "总产业情况——第三产业工人占比")
    private String workerRange_2_3;

    /**
     产业情况——第三产业工人较上月变化数量
     */
    @ApiModelProperty(value = "产业情况——第三产业工人较上月变化数量")
    private String workerDifference_2_3;
    /**
     产业情况——第三产业工人较上月变化幅度
     */
    @ApiModelProperty(value = "产业情况——第三产业工人较上月变化幅度")
    private String workerDifferenceRange_2_3;
    /**
     产业情况——第三产业工人较年初变化数量
     */
    @ApiModelProperty(value = "产业情况——第三产业工人较年初变化数量")
    private String workerDifferenceBeginYear_2_3;
    /**
     产业情况——第三产业工人较年初变化幅度
     */
    @ApiModelProperty(value = "产业情况——第三产业工人较年初变化幅度")
    private String workerDifferenceRangeBeginYear_2_3;
    /**
     行业情况——人数增加前五
     */
    @ApiModelProperty(value = "行业情况——人数增加前五")
    private String addWorkerCountTop_3;
    /**
     行业情况——人数增幅前五
     */
    @ApiModelProperty(value = "行业情况——人数增幅前五")
    private String addWorkerRangeTop_3;
    /**
     行业情况——人数减少前五
     */
    @ApiModelProperty(value = "行业情况——人数减少前五")
    private String reduceWorkerCountTop_3;
    /**
     行业情况——人数减幅前五
     */
    @ApiModelProperty(value = "行业情况——人数减幅前五")
    private String reduceWorkerRangeTop_3;
    /**
     地区情况——用工不变地区个数
     */
    @ApiModelProperty(value = "地区情况——用工不变地区个数")
    private String noChangeCount_4;
    /**
     地区情况——用工增加地区个数
     */
    @ApiModelProperty(value = "地区情况——用工增加地区个数")
    private String addChangeCount_4;
    /**
     地区情况——用工增加地区增幅
     */
    @ApiModelProperty(value = "地区情况——用工增加地区增幅")
    private String addChangeRange_4;
    /**
     地区情况——用工减少地区个数
     */
    @ApiModelProperty(value = "地区情况——用工减少地区个数")
    private String reduceChangeCount_4;
    /**
     地区情况——用工减少地区减幅
     */
    @ApiModelProperty(value = "地区情况——用工减少地区减幅")
    private String reduceChangeRange_4;
    /**
     地区情况——用工不变地区个数较年初
     */
    @ApiModelProperty(value = "地区情况——用工不变地区个数较年初")
    private String noChangeCountBeginYear_4;
    /**
     地区情况——用工增加地区个数较年初
     */
    @ApiModelProperty(value = "地区情况——用工增加地区个数较年初")
    private String addChangeCountBeginYear_4;
    /**
     地区情况——用工增加地区增幅较年初
     */
    @ApiModelProperty(value = "地区情况——用工增加地区增幅较年初")
    private String addChangeRangeBeginYear_4;
    /**
     地区情况——用工减少地区个数较年初
     */
    @ApiModelProperty(value = "地区情况——用工减少地区个数较年初")
    private String reduceChangeCountBeginYear_4;
    /**
     地区情况——用工减少地区减幅较年初
     */
    @ApiModelProperty(value = "地区情况——用工减少地区减幅较年初")
    private String reduceChangeRangeBeginYear_4;


    /**
     园区情况——用工不变地区个数
     */
    @ApiModelProperty(value = "园区情况——用工不变地区个数")
    private String noChangeCount_5;
    /**
     园区情况——用工增加地区个数
     */
    @ApiModelProperty(value = "园区情况——用工增加地区个数")
    private String addChangeCount_5;
    /**
     园区情况——用工增加地区增幅
     */
    @ApiModelProperty(value = "园区情况——用工增加地区增幅")
    private String addChangeRange_5;
    /**
     园区情况——用工减少地区个数
     */
    @ApiModelProperty(value = "园区情况——用工减少地区个数")
    private String reduceChangeCount_5;
    /**
     园区情况——用工减少地区减幅
     */
    @ApiModelProperty(value = "园区情况——用工减少地区减幅")
    private String reduceChangeRange_5;
    /**
     园区情况——用工不变地区个数较年初
     */
    @ApiModelProperty(value = "园区情况——用工不变地区个数较年初")
    private String noChangeCountBeginYear_5;
    /**
     园区情况——用工增加地区个数较年初
     */
    @ApiModelProperty(value = "园区情况——用工增加地区个数较年初")
    private String addChangeCountBeginYear_5;
    /**
     园区情况——用工增加地区增幅较年初
     */
    @ApiModelProperty(value = "园区情况——用工增加地区增幅较年初")
    private String addChangeRangeBeginYear_5;
    /**
     园区情况——用工减少地区个数较年初
     */
    @ApiModelProperty(value = "园区情况——用工减少地区个数较年初")
    private String reduceChangeCountBeginYear_5;
    /**
     园区情况——用工减少地区减幅较年初
     */
    @ApiModelProperty(value = "园区情况——用工减少地区减幅较年初")
    private String reduceChangeRangeBeginYear_5;
    /**
     企业情况——大型企业数
     */
    @ApiModelProperty(value = "企业情况——大型企业数")
    private String workerCpCount6_1;
    /**
     企业情况——大型企业用工数
     */
    @ApiModelProperty(value = "企业情况——大型企业用工数")
    private String workerCount6_1;
    /**
     企业情况——大型企业用工比上月变化数
     */
    @ApiModelProperty(value = "企业情况——大型企业用工比上月变化数")
    private String changeWorkerCount6_1;
    /**
     企业情况——大型企业用工比上月变化数
     */
    @ApiModelProperty(value = "企业情况——大型企业用工比上月变化数")
    private String changeWorkerCountBeginYear6_1;
    /**
     企业情况——大型企业用工变化幅度较上月
     */
    @ApiModelProperty(value = "企业情况——大型企业用工变化幅度较上月")
    private String changeWorkerRange6_1;
    /**
     企业情况——大型企业用工变化幅度较年初
     */
    @ApiModelProperty(value = "企业情况——大型企业用工变化幅度较年初")
    private String changeWorkerRangeBeginYear6_1;

    /**
     企业情况——中型企业数
     */
    @ApiModelProperty(value = "企业情况——中型企业数")
    private String workerCpCount6_2;
    /**
     企业情况——中型企业用工数
     */
    @ApiModelProperty(value = "企业情况——中型企业用工数")
    private String workerCount6_2;
    /**
     企业情况——中型企业用工比上月变化数
     */
    @ApiModelProperty(value = "企业情况——中型企业用工比上月变化数")
    private String changeWorkerCount6_2;
    /**
     企业情况——中型企业用工比年初变化数
     */
    @ApiModelProperty(value = "企业情况——中型企业用工比年初变化数")
    private String changeWorkerCountBeginYear6_2;
    /**
     企业情况——中型企业用工变化幅度较上月
     */
    @ApiModelProperty(value = "企业情况——中型企业用工变化幅度较上月")
    private String changeWorkerRange6_2;
    /**
     企业情况——中型企业用工变化幅度较年初
     */
    @ApiModelProperty(value = "企业情况——中型企业用工变化幅度较年初")
    private String changeWorkerRangeBeginYear6_2;

    /**
     企业情况——小型企业数
     */
    @ApiModelProperty(value = "企业情况——小型企业数")
    private String workerCpCount6_3;
    /**
     企业情况——小型企业用工数
     */
    @ApiModelProperty(value = "企业情况——小型企业用工数")
    private String workerCount6_3;
    /**
     企业情况——小型企业用工比上月变化数
     */
    @ApiModelProperty(value = "企业情况——小型企业用工比上月变化数")
    private String changeWorkerCount6_3;
    /**
     企业情况——小型型企业用工比年初变化数
     */
    @ApiModelProperty(value = "企业情况——小型型企业用工比年初变化数")
    private String changeWorkerCountBeginYear6_3;
    /**
     企业情况——小型企业用工变化幅度较上月
     */
    @ApiModelProperty(value = "企业情况——小型企业用工变化幅度较上月")
    private String changeWorkerRange6_3;
    /**
     企业情况——小型企业用工变化幅度较年初
     */
    @ApiModelProperty(value = "企业情况——小型企业用工变化幅度较年初")
    private String changeWorkerRangeBeginYear6_3;

    /**
     企业情况——微型企业数
     */
    @ApiModelProperty(value = "企业情况——微型企业数")
    private String workerCpCount6_4;
    /**
     企业情况——微型企业用工数
     */
    @ApiModelProperty(value = "企业情况——微型企业用工数")
    private String workerCount6_4;
    /**
     企业情况——微型企业用工比上月变化数
     */
    @ApiModelProperty(value = "企业情况——微型企业用工比上月变化数")
    private String changeWorkerCount6_4;
    /**
     企业情况——微型企业用工比上月变化数
     */
    @ApiModelProperty(value = "企业情况——微型企业用工比年初变化数")
    private String changeWorkerCountBeginYear6_4;
    /**
     企业情况——微型企业用工变化幅度较上月
     */
    @ApiModelProperty(value = "企业情况——微型企业用工变化幅度较上月")
    private String changeWorkerRange6_4;
    /**
     企业情况——微型企业用工变化幅度较年初
     */
    @ApiModelProperty(value = "企业情况——微型企业用工变化幅度较年初")
    private String changeWorkerRangeBeginYear6_4;
    @ApiModelProperty(value = "缺工情况——本期缺工企业数")
    private String nowLackCpCount_7;
    @ApiModelProperty(value = "缺工情况——本期缺工工人数")
    private String nowLackWorkerCount_7;
    @ApiModelProperty(value = "缺工情况——比上期变化企业数")
    private Long differenceLackCpCount_7;
    @ApiModelProperty(value = "缺工情况——比上期变化工人数")
    private Long differenceLackWorkerCount_7;
    @ApiModelProperty(value = "缺工情况——比年初变化企业数")
    private Long differenceLackCpCountBeginYear_7;
    @ApiModelProperty(value = "缺工情况——比年初变化工人数")
    private Long differenceLackWorkerCountBeginYear_7;
    @ApiModelProperty(value = "缺工情况——缺工地区数")
    private Long lackByRegion_7;
    @ApiModelProperty(value = "缺工情况——缺工行业数")
    private Long lackByIndustry_7;

}
