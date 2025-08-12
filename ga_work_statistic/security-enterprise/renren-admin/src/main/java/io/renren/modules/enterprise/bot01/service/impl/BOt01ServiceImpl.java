package io.renren.modules.enterprise.bot01.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.FilesUploadUtils;
import io.renren.modules.enterprise.bot01.dao.BOt01Dao;
import io.renren.modules.enterprise.bot01.dto.BOt01DTO;
import io.renren.modules.enterprise.bot01.entity.BOt01Entity;
import io.renren.modules.enterprise.bot01.service.BOt01Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 附件数据表
 *
 * @author CJF
 * @since 1.0.0 2025-08-05
 */
@Service
public class BOt01ServiceImpl extends CrudServiceImpl<BOt01Dao, BOt01Entity, BOt01DTO> implements BOt01Service {

    @Override
    public QueryWrapper<BOt01Entity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<BOt01Entity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public Long upload(MultipartFile file, String fileType, String viewType) {
        String saveFileName = FilesUploadUtils.uploadFile(file);
        BOt01Entity ot01Entity = new BOt01Entity();
        ot01Entity.setName(FileUtil.getName(saveFileName));
        ot01Entity.setBusitype(fileType);
        ot01Entity.setUrl(saveFileName);
        ot01Entity.setViewType(viewType);
        ot01Entity.setOriginalName(file.getOriginalFilename());
        ot01Entity.setWhether("1");
        baseDao.insert(ot01Entity);
        return ot01Entity.getOt0101();
    }

    @Override
    public List<BOt01Entity> listByMap(Long cp0101) {
        return baseDao.selectList(new QueryWrapper<BOt01Entity>().eq("BUSITYPE","01").eq("BUSISYSNO",cp0101));
    }
}