package io.renren.modules.enterprise.bcp02.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.CommonUtils;
import io.renren.modules.enterprise.bcp02.dao.BCp02Dao;
import io.renren.modules.enterprise.bcp02.dto.BCp02DTO;
import io.renren.modules.enterprise.bcp02.entity.BCp02Entity;
import io.renren.modules.enterprise.bcp02.service.BCp02Service;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 修改企业基础信息审核表
 *
 * @author JY 
 * @since 1.0.0 2025-08-03
 */
@Transactional
@Service
public class BCp02ServiceImpl extends CrudServiceImpl<BCp02Dao, BCp02Entity, BCp02DTO> implements BCp02Service {

    @Autowired
    private SysDeptDao sysDeptDao;

    @Override
    public QueryWrapper<BCp02Entity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<BCp02Entity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public PageData<BCp02DTO> pageList(Map<String, Object> params) {
        Long deptId = SecurityUser.getDeptId();
        SysDeptEntity deptEntity = sysDeptDao.getById(deptId);
        String orgtype = deptEntity.getOrgtype();
        if("1".equals(orgtype)){
            //企业
            Long cp0101 = CommonUtils.userCpInfo().getCp0101();
            params.put("deptIdSelf",deptId);
        }else{
            //监管
            params.put("deptId",deptId);
        }
        IPage<BCp02Entity> page = getPage(params, "", false);
        params.put("CREATE_DATE","desc");
        List<BCp02DTO> list = baseDao.getList(params);
        return getPageData(list, page.getTotal(), BCp02DTO.class);
    }
}