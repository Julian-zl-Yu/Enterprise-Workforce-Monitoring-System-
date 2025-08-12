package io.renren.modules.enterprise.bcp01.service;


import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp02.entity.BCp02Entity;

import java.util.Map;

/**
 * 企业基础信息
 *
 * @author JY 
 * @since 1.0.0 2025-07-29
 */
public interface BCp01Service extends CrudService<BCp01Entity, BCp01DTO> {

    Result saveCp01Info(BCp01DTO dto);

    PageData<BCp01DTO> pageList(Map<String, Object> params);

    BCp01Entity getUserCpInfo(Long deptid);

    void updateApply(BCp02Entity bCp02Entity);

    String getPidList(Long pid);
}