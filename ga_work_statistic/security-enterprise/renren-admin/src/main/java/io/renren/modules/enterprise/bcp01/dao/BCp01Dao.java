package io.renren.modules.enterprise.bcp01.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 企业基础信息
 *
 * @author JY 
 * @since 1.0.0 2025-07-29
 */
@Mapper
public interface BCp01Dao extends BaseDao<BCp01Entity> {

    List<BCp01DTO> getList(Map<String, Object> params);

}