package io.renren.modules.enterprise.bcp01.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.CommonUtils;
import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp01.dao.BCp01Dao;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp01.service.BCp01Service;
import io.renren.modules.enterprise.bcp02.entity.BCp02Entity;
import io.renren.modules.enterprise.bcp02.service.BCp02Service;
import io.renren.modules.enterprise.bcp03.dao.BCp03Dao;
import io.renren.modules.enterprise.bcp03.entity.BCp03Entity;
import io.renren.modules.message.service.SysSmsService;
import io.renren.modules.security.password.PasswordUtils;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.dao.SysRegionDao;
import io.renren.modules.sys.dao.SysRoleUserDao;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysRegionEntity;
import io.renren.modules.sys.entity.SysRoleUserEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysParamsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 企业基础信息
 *
 * @author JY
 * @since 1.0.0 2025-07-29
 */
@Transactional
@Service
public class BCp01ServiceImpl extends CrudServiceImpl<BCp01Dao, BCp01Entity, BCp01DTO> implements BCp01Service {

    @Autowired
    private BCp01Dao bcp01Dao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysDeptDao sysDeptDao;
    @Autowired
    private SysParamsService sysParamsService;
    @Autowired
    private SysSmsService sysSmsService;
    @Autowired
    private SysRoleUserDao sysRoleUserDao;
    @Autowired
    private BCp02Service bCp02Service;
    @Autowired
    private SysRegionDao sysRegionDao;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private BCp03Dao bCp03Dao;

    @Override
    public QueryWrapper<BCp01Entity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        QueryWrapper<BCp01Entity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        return wrapper;
    }

    /**
     * 获取所有上级部门ID
     * @param pid 上级ID
     */
    public String getPidList(Long pid){
        //顶级部门，无上级部门
        if(Constant.DEPT_ROOT.equals(pid)){
            return Constant.DEPT_ROOT + "";
        }

        //所有部门的id、pid列表
        List<SysDeptEntity> deptList = sysDeptDao.getIdAndPidList();

        //list转map
        Map<Long, SysDeptEntity> map = new HashMap<>(deptList.size());
        for(SysDeptEntity entity : deptList){
            map.put(entity.getId(), entity);
        }

        //递归查询所有上级部门ID列表
        List<Long> pidList = new ArrayList<>();
        getPidTree(pid, map, pidList);

        return StringUtils.join(pidList, ",");
    }

    private void getPidTree(Long pid, Map<Long, SysDeptEntity> map, List<Long> pidList) {
        //顶级部门，无上级部门
        if(Constant.DEPT_ROOT.equals(pid)){
            return ;
        }

        //上级部门存在
        SysDeptEntity parent = map.get(pid);
        if(parent != null){
            getPidTree(parent.getPid(), map, pidList);
        }

        pidList.add(pid);
    }


    @Override
    public Result saveCp01Info(BCp01DTO dto) {
        Result result = new Result();
        String corpcode = dto.getCorpcode();
        String mobile = dto.getMobile();
        List<BCp01Entity> Cp01List = bcp01Dao.selectList(new QueryWrapper<BCp01Entity>().eq("corpcode", corpcode));
        List<SysUserEntity> mobileList = sysUserDao.selectList(new QueryWrapper<SysUserEntity>().eq("mobile", mobile));
        if(Cp01List!=null && Cp01List.size()>0){
            return result.error("该信用代码的企业已被注册，请勿重复注册");
        }else if(mobileList!=null && mobileList.size()>0){
            return result.error("该手机号码已被注册，请更换手机号重新注册");
        }else{
            //创建机构数据
            SysRegionEntity sysRegionEntity = sysRegionDao.selectById(dto.getAreacode());
            String name = sysRegionEntity.getName();
            SysDeptEntity pDept = sysDeptDao.selectOne(new QueryWrapper<SysDeptEntity>().eq("NAME", name));
            SysDeptEntity sysDeptEntity = new SysDeptEntity();
            sysDeptEntity.setName(dto.getCorpname());
            sysDeptEntity.setPid(pDept.getId());
            sysDeptEntity.setPids(getPidList(pDept.getId()));
            sysDeptEntity.setOrgtype("1");
            sysDeptDao.insert(sysDeptEntity);
            //创建用户数据
            SysUserEntity sysUserEntity = new SysUserEntity();
            sysUserEntity.setDeptId(sysDeptEntity.getId());
            sysUserEntity.setMobile(dto.getMobile());//联系电话
            sysUserEntity.setUsername(corpcode.substring(corpcode.length()-8));
            sysUserEntity.setRealName(dto.getLinkman());//联系人
            sysUserEntity.setSuperAdmin(0);
            sysUserEntity.setStatus(1);
            sysUserEntity.setPassword(PasswordUtils.encode(sysParamsService.getValue(Constant.PASSWORD)));
            sysUserDao.insert(sysUserEntity);
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setUserId(sysUserEntity.getId());
            sysRoleUserEntity.setRoleId(1598227117234376706L);
            sysRoleUserDao.insert(sysRoleUserEntity);
            //创建企业数据
            BCp01Entity bCp01Entity = new BCp01Entity();
            BeanUtils.copyProperties(dto,bCp01Entity);
            bCp01Entity.setDeptId(sysDeptEntity.getId());
            bCp01Entity.setLinkcellphone(dto.getMobile());
            bCp01Entity.setAblestatus(Constant.ABLESTATUSDEF);
            bcp01Dao.insert(bCp01Entity);
            result.setMsg("操作成功,请查看手机短信中的账号和密码！");
            result.ok("");
            //创建注册当年的年初数据
            BCp03Entity cp03Entity = new BCp03Entity();
            cp03Entity.setCp0101(bCp01Entity.getCp0101());
            cp03Entity.setExamineStatus("0");
            cp03Entity.setLackController(0L);
            cp03Entity.setLackOrdWorker(0L);
            cp03Entity.setLackSkillWorker(0L);
            cp03Entity.setLackTechWorker(0L);
            cp03Entity.setUseController(0L);
            cp03Entity.setUseOrdWorker(0L);
            cp03Entity.setUseSkillWorker(0L);
            cp03Entity.setUseTechWorker(0L);
            String ym = DateUtil.format(new Date(), "yyyy-MM");
            String firstMonth = ym.substring(0, 4) + "-01";
            cp03Entity.setYearMonth(firstMonth);
            bCp03Dao.insert(cp03Entity);
            //直接页面提示
            result.setMsg("注册成功，账号："+sysUserEntity.getUsername()+",密码："+Constant.PW);
            //发送账号密码到用户手机短信
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("username", sysUserEntity.getUsername());
//            jsonObject.put("password", Constant.PW);
//            sysSmsService.send("1001", dto.getMobile(), jsonObject.toJSONString());
        }
        return result;
    }

    @Override
    public PageData<BCp01DTO> pageList(Map<String, Object> params) {
        Long deptId = SecurityUser.getDeptId();
//        Long deptId = (Long) params.get("deptId");
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
        IPage<BCp01Entity> page = getPage(params, "", false);
//        Page<BCp01Entity> p = new Page<>(1,10);
        List<BCp01DTO> list = baseDao.getList(params);
        return getPageData(list, page.getTotal(), BCp01DTO.class);
    }

    @Override
    public BCp01Entity getUserCpInfo(Long deptId) {
        return baseDao.selectOne(new QueryWrapper<BCp01Entity>().eq("dept_id",deptId));
    }

    @Override
    public void updateApply(BCp02Entity bCp02Entity) {
        bCp02Service.insert(bCp02Entity);
    }
}