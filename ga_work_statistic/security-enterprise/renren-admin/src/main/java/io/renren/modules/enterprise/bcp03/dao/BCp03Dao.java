package io.renren.modules.enterprise.bcp03.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.enterprise.bcp03.dto.BCp03DTO;
import io.renren.modules.enterprise.bcp03.dto.HomePageByRegionDTO;
import io.renren.modules.enterprise.bcp03.entity.BCp03Entity;
import io.renren.modules.enterprise.bcp03tj.dto.ByIndustryDto;
import io.renren.modules.enterprise.bcp03tj.dto.YearMonthDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业用工缺工情况表
 *
 * @author JY 
 * @since 1.0.0 2025-08-06
 */
@Mapper
public interface BCp03Dao extends BaseDao<BCp03Entity> {

    List<BCp03DTO> listByYM(BCp03DTO dto);

    List<BCp03DTO> getList(Map<String, Object> params);

    List<BCp03DTO> echart(Map<String, Object> params);

    List<BCp03DTO> getThisMonthData(Map<String, Object> params);

    List<String> selectIndustryList();

    BCp03DTO getInfo(@Param("id")Long id);

    List<BCp03DTO> getLackWorkerData(Map<String, Object> params);

    List<YearMonthDto> selectYearMonthData(Map<String, Object> params);



    Long hasData(@Param("yearMonth")String yearMonth, @Param("cp0101")Long cp0101);

    List<HomePageByRegionDTO> homePageByRegion(Map<String, Object> params);

    List<HomePageByRegionDTO> homePageByEstate(Map<String, Object> params);

    List<HomePageByRegionDTO> homePageByYear(Map<String, Object> params);

    List<ByIndustryDto> selectByIndustry(Map<String, Object> params);
}