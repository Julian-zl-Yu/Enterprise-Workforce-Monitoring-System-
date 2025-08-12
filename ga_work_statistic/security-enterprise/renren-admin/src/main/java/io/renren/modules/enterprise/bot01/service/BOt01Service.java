package io.renren.modules.enterprise.bot01.service;

import io.renren.common.service.CrudService;
import io.renren.modules.enterprise.bot01.dto.BOt01DTO;
import io.renren.modules.enterprise.bot01.entity.BOt01Entity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 附件数据表
 *
 * @author CJF
 * @since 1.0.0 2025-08-05
 */
public interface BOt01Service extends CrudService<BOt01Entity, BOt01DTO> {

    Long upload(MultipartFile file, String fileType, String viewType);

    List<BOt01Entity> listByMap(Long cp0101);
}