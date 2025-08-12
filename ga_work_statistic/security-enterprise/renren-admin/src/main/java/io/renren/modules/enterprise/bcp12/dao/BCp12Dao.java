package io.renren.modules.enterprise.bcp12.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.enterprise.bcp12.entity.BCp12Entity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 批量导入企业明细表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
@Mapper
public interface BCp12Dao extends BaseDao<BCp12Entity> {
	
}