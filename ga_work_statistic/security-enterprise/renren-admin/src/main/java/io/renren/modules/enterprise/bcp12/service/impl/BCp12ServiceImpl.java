package io.renren.modules.enterprise.bcp12.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp12.dao.BCp12Dao;
import io.renren.modules.enterprise.bcp12.dto.BCp12DTO;
import io.renren.modules.enterprise.bcp12.entity.BCp12Entity;
import io.renren.modules.enterprise.bcp12.service.BCp12Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 批量导入企业明细表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
@Service
@Transactional
public class BCp12ServiceImpl extends CrudServiceImpl<BCp12Dao, BCp12Entity, BCp12DTO> implements BCp12Service {

    @Override
    public QueryWrapper<BCp12Entity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<BCp12Entity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<BCp12Entity> selectList(String cp1101) {
        return baseDao.selectList(new QueryWrapper<BCp12Entity>().eq("CP1101",cp1101));
    }

    @Override
    public PageData<BCp12DTO> repage(Map<String, Object> params) {
        Object cp1101obj = params.get("cp1101");
        Long cp1101 = null;
        if(ObjectUtil.isNotNull(cp1101obj)){
            cp1101 = Long.valueOf(cp1101obj.toString());
        }else{
            throw new RenException("cp1101无法获取");
        }
        IPage<BCp12Entity> page = getPage(params, "", false);
        List<BCp12Entity> cp12EntityList = baseDao.selectList(new QueryWrapper<BCp12Entity>().eq("cp1101", cp1101));
        page.setTotal(cp12EntityList.size());
        return getPageData(cp12EntityList, page.getTotal(), BCp12DTO.class);
    }
}