package io.renren.modules.enterprise.bcp02.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.enterprise.bcp02.dto.BCp02DTO;
import io.renren.modules.enterprise.bcp02.entity.BCp02Entity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 修改企业基础信息审核表
 *
 * @author JY 
 * @since 1.0.0 2025-08-03
 */
@Mapper
public interface BCp02Dao extends BaseDao<BCp02Entity> {

    List<BCp02DTO> getList(Map<String, Object> params);
}