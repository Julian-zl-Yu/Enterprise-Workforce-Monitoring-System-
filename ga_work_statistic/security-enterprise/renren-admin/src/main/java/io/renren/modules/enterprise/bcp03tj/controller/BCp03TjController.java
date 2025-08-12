package io.renren.modules.enterprise.bcp03tj.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp03.dto.HomePageByYearDTO;
import io.renren.modules.enterprise.bcp03.service.BCp03Service;
import io.renren.modules.enterprise.bcp03tj.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 监管统计查询
 *
 * @author JY
 * @since 1.0.0 2025-08-06
 */
@RestController
@RequestMapping("enterprise/cp03tj")
@Api(tags="监管统计查询")
public class BCp03TjController {
    @Autowired
    private BCp03Service bCp03Service;

    @GetMapping("generalSituation")
    @ApiOperation("总体情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<GeneralSituationDto> generalSituation(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.generalSituation(params);
    }

    @GetMapping("overallAnalysis")
    @ApiOperation("总体分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<OverallAnalysisDTO> overallAnalysis(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.overallAnalysis(params);
    }

    @GetMapping("exportGeneralSituation")
    @ApiOperation("导出总体情况")
    @LogOperation("导出总体情况")
    public void exportGeneralSituation(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportGeneralSituation(params, response);
    }

    @GetMapping("byIndustry")
    @ApiOperation("行业情况分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<ByIndustryDto> byIndustry(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byIndustry(params);
    }

    @GetMapping("exportByIndustry")
    @ApiOperation("导出行业情况")
    @LogOperation("导出行业情况")
    public void exportByIndustry(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByIndustry(params, response);
    }

    @GetMapping("byEstate")
    @ApiOperation("产业情况分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<ByEstateDto> byEstate(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byEstate(params);
    }

    @GetMapping("exportByEstate")
    @ApiOperation("导出产业情况")
    @LogOperation("导出产业情况")
    public void exportByEstate(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByEstate(params, response);
    }

    @GetMapping("byPark")
    @ApiOperation("园区情况分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<ByParkDto> byPark(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byPark(params);
    }

    @GetMapping("exportByPark")
    @ApiOperation("导出园区情况")
    @LogOperation("导出园区情况")
    public void exportByPark(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByPark(params, response);
    }

    @GetMapping("byRegion")
    @ApiOperation("地区情况分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<ByRegionDto> byRegion(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byRegion(params);
    }

    @GetMapping("exportByRegion")
    @ApiOperation("导出地区情况")
    @LogOperation("导出地区情况")
    public void exportByRegion(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByRegion(params, response);
    }

    @GetMapping("byScale")
    @ApiOperation("企业规模情况分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<ByScaleDto> byScale(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byScale(params);
    }

    @GetMapping("exportByScale")
    @ApiOperation("导出企业规模情况")
    @LogOperation("导出企业规模情况")
    public void exportByScale(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByScale(params, response);
    }

    @GetMapping("byLack")
    @ApiOperation("缺工企业情况分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<LackWorkerDto> byLack(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byLack(params);
    }

    @GetMapping("exportByLack")
    @ApiOperation("导出缺工企业情况")
    @LogOperation("导出缺工企业情况")
    public void exportByLack(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByLack(params, response);
    }

    @GetMapping("byYearMonth")
    @ApiOperation("年月统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "查询年（为空查询当年数据），格式yyyy", paramType = "query", dataType="String")
    })
    public Result<ByYearMonthDto> byYearMonth(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.byYearMonth(params);
    }

    @GetMapping("exportByYearMonth")
    @ApiOperation("导出年月统计")
    @LogOperation("导出年月统计")
    public void exportByYearMonth(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        bCp03Service.exportByYearMonth(params, response);
    }

    @GetMapping("homePageByRegion")
    @ApiOperation("首页扇形图——各区县企业数量及占比")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<List> homePageByRegion(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.homePageByRegion(params);
    }

    @GetMapping("homePageByEstate")
    @ApiOperation("首页扇形图——各产业企业数量及占比")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<List> homePageByEstate(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.homePageByEstate(params);
    }

    @GetMapping("homePageByYear")
    @ApiOperation("首页折线图——今年个月用工数量")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "yearMonth", value = "查询年月（为空查询当月数据），格式yyyy-mm", paramType = "query", dataType="String")
    })
    public Result<HomePageByYearDTO> homePageByYear(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.homePageByYear(params);
    }



}