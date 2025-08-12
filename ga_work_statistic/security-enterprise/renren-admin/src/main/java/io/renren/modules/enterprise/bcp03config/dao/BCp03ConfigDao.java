package io.renren.modules.enterprise.bcp03config.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.enterprise.bcp03config.entity.BCp03ConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业用工缺工情况配置表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Mapper
public interface BCp03ConfigDao extends BaseDao<BCp03ConfigEntity> {
	
}