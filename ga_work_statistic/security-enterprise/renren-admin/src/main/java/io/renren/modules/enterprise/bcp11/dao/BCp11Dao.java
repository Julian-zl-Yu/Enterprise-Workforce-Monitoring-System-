package io.renren.modules.enterprise.bcp11.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.enterprise.bcp11.entity.BCp11Entity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批量导入企业主表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
@Mapper
public interface BCp11Dao extends BaseDao<BCp11Entity> {

    List<String> selectDictData(@Param("dictType") String dictType);
}