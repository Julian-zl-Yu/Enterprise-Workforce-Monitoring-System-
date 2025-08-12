package io.renren.modules.enterprise.bcp02.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.enterprise.bcp02.dto.BCp02DTO;
import io.renren.modules.enterprise.bcp02.entity.BCp02Entity;

import java.util.Map;

/**
 * 修改企业基础信息审核表
 *
 * @author JY 
 * @since 1.0.0 2025-08-03
 */
public interface BCp02Service extends CrudService<BCp02Entity, BCp02DTO> {

    PageData<BCp02DTO> pageList(Map<String, Object> params);
}