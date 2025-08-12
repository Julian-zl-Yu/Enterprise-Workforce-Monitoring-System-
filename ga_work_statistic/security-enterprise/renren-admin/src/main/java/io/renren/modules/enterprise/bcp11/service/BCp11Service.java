package io.renren.modules.enterprise.bcp11.service;

import io.renren.common.service.CrudService;
import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp11.dto.BCp11DTO;
import io.renren.modules.enterprise.bcp11.entity.BCp11Entity;
import io.renren.modules.enterprise.bcp11.excel.BCp11Excel;

import java.util.List;

/**
 * 批量导入企业主表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
public interface BCp11Service extends CrudService<BCp11Entity, BCp11DTO> {



    Result saveData(String cp1101);

    Result importCp(List<BCp11Excel> collect);
}