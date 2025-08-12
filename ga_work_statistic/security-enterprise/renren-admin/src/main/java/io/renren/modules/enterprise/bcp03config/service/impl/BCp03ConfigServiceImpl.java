package io.renren.modules.enterprise.bcp03config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.enterprise.bcp03config.dao.BCp03ConfigDao;
import io.renren.modules.enterprise.bcp03config.dto.BCp03ConfigDTO;
import io.renren.modules.enterprise.bcp03config.entity.BCp03ConfigEntity;
import io.renren.modules.enterprise.bcp03config.service.BCp03ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 企业用工缺工情况配置表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@Service
public class BCp03ConfigServiceImpl extends CrudServiceImpl<BCp03ConfigDao, BCp03ConfigEntity, BCp03ConfigDTO> implements BCp03ConfigService {

    @Override
    public QueryWrapper<BCp03ConfigEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<BCp03ConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}