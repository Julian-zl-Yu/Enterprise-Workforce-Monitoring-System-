package io.renren.modules.enterprise.bcp12.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.enterprise.bcp12.dto.BCp12DTO;
import io.renren.modules.enterprise.bcp12.entity.BCp12Entity;

import java.util.List;
import java.util.Map;

/**
 * 批量导入企业明细表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
public interface BCp12Service extends CrudService<BCp12Entity, BCp12DTO> {

    List<BCp12Entity> selectList(String cp1101);

    PageData<BCp12DTO> repage(Map<String, Object> params);
}