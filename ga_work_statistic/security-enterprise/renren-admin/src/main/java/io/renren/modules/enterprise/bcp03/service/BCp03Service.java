package io.renren.modules.enterprise.bcp03.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp03.dto.BCp03DTO;
import io.renren.modules.enterprise.bcp03.dto.HomePageByYearDTO;
import io.renren.modules.enterprise.bcp03.entity.BCp03Entity;
import io.renren.modules.enterprise.bcp03echart.dto.BCp03EchartDTO;
import io.renren.modules.enterprise.bcp03tj.dto.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 企业用工缺工情况表
 *
 * @author JY 
 * @since 1.0.0 2025-08-06
 */
public interface BCp03Service extends CrudService<BCp03Entity, BCp03DTO> {

    List<BCp03DTO> listByYM(BCp03DTO dto);

    PageData<BCp03DTO> pageList(Map<String, Object> params);

    Result<BCp03EchartDTO> echart(Map<String, Object> params);

    Result<GeneralSituationDto> generalSituation(Map<String, Object> params);

    Result<ByIndustryDto> byIndustry(Map<String, Object> params);

    Result<ByEstateDto> byEstate(Map<String, Object> params);

    Result<ByRegionDto> byRegion(Map<String, Object> params);

    BCp03DTO getInfo(Long id);

    void exportGeneralSituation(Map<String, Object> params, HttpServletResponse response);

    Result<ByScaleDto> byScale(Map<String, Object> params);

    Result<LackWorkerDto> byLack(Map<String, Object> params);

    Result<ByYearMonthDto> byYearMonth(Map<String, Object> params);

    Result canAdd();

    Result<ByParkDto> byPark(Map<String, Object> params);

    void exportByScale(Map<String, Object> params, HttpServletResponse response);

    void exportByIndustry(Map<String, Object> params, HttpServletResponse response);

    void exportByRegion(Map<String, Object> params, HttpServletResponse response);

    void exportByEstate(Map<String, Object> params, HttpServletResponse response);

    void exportByLack(Map<String, Object> params, HttpServletResponse response);

    void exportByYearMonth(Map<String, Object> params, HttpServletResponse response);

    void exportByPark(Map<String, Object> params, HttpServletResponse response);

    Result<OverallAnalysisDTO> overallAnalysis(Map<String, Object> params);

    Result<List> homePageByRegion(Map<String, Object> params);

    Long hasData(Long deptId);

    Result<List> homePageByEstate(Map<String, Object> params);

    Result<HomePageByYearDTO> homePageByYear(Map<String, Object> params);
}