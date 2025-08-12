package io.renren.modules.enterprise.bcp11.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.NumberUtil;
import io.renren.common.utils.Result;
import io.renren.common.utils.aliyun.AilynUtils;
import io.renren.common.utils.aliyun.CompanyDTO;
import io.renren.modules.enterprise.bcp01.dao.BCp01Dao;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp01.service.BCp01Service;
import io.renren.modules.enterprise.bcp03.dao.BCp03Dao;
import io.renren.modules.enterprise.bcp03.entity.BCp03Entity;
import io.renren.modules.enterprise.bcp11.dao.BCp11Dao;
import io.renren.modules.enterprise.bcp11.dto.BCp11DTO;
import io.renren.modules.enterprise.bcp11.entity.BCp11Entity;
import io.renren.modules.enterprise.bcp11.excel.BCp11Excel;
import io.renren.modules.enterprise.bcp11.service.BCp11Service;
import io.renren.modules.enterprise.bcp12.entity.BCp12Entity;
import io.renren.modules.enterprise.bcp12.service.BCp12Service;
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
import io.renren.modules.sys.service.SysParamsService;
import io.renren.modules.sys.service.SysRegionService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量导入企业主表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
@Service
@Transactional
public class BCp11ServiceImpl extends CrudServiceImpl<BCp11Dao, BCp11Entity, BCp11DTO> implements BCp11Service {

    @Autowired
    private BCp01Dao bcp01Dao;

    @Autowired
    private SysRegionService sysRegionService;

    @Autowired
    private BCp12Service bCp12Service;

    @Autowired
    private BCp01Dao bCp01Dao;

    @Autowired
    private SysRegionDao sysRegionDao;

    @Autowired
    private SysDeptDao sysDeptDao;

    @Autowired
    private BCp01Service bCp01Service;

    @Autowired
    private SysParamsService sysParamsService;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleUserDao sysRoleUserDao;

    @Autowired
    private BCp03Dao bCp03Dao;

    @Override
    public QueryWrapper<BCp11Entity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<BCp11Entity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public Result importCp(List<BCp11Excel> collect) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Integer org = collect.size();
        Map<String, Object> map = checkDate(collect);
        List<BCp11Excel> list = (List<BCp11Excel>) map.get("collect");
        Integer after = list.size();
        Integer bad = org - after;
        //插入CP11
        BCp11Entity bCp11Entity = new BCp11Entity();
        bCp11Entity.setImpdate(new Date());
        bCp11Entity.setImportnumber(org.toString());
        bCp11Entity.setSuccessnumber(after.toString());
        bCp11Entity.setFailnumber(bad.toString());
        bCp11Entity.setUserId(SecurityUser.getUserId());
        bCp11Entity.setResult("0");
        baseDao.insert(bCp11Entity);
        //插入CP12
        for (BCp11Excel itemExcel :
                list) {
            BCp12Entity bcp12Entity = new BCp12Entity();
            bcp12Entity.setCp1101(bCp11Entity.getCp1101());
            bcp12Entity.setResult("0");
            bcp12Entity.setCorpcode(itemExcel.getCorpcode());
            bcp12Entity.setCorpname(itemExcel.getCorpname());
            bcp12Entity.setAreacode(itemExcel.getAreacode());
            bcp12Entity.setIndustry(itemExcel.getIndustry());
            bcp12Entity.setEstate(itemExcel.getEstate());
            bcp12Entity.setScale(itemExcel.getScale());
            bcp12Entity.setLinkcellphone(itemExcel.getLinkcellphone());
            bCp12Service.insert(bcp12Entity);
        }
        Result result = new Result();
        result.ok(bCp11Entity.getCp1101().toString());
        result.setMsg("一共校验" + org + "条数据，丢弃不合格" + bad + "条");
        return result;
    }

    @Override
    public Result saveData(String cp1101) {

        Long bad = 0L;
        Long success = 0L;
        Result result = new Result();
        //查询cp12
        List<BCp12Entity> cp12EntityList = bCp12Service.selectList(cp1101);
        for (BCp12Entity item :
                cp12EntityList) {
            String itr = "1";//默认结果成功
            //查询重复
            BCp01Entity bCp01Entity = bCp01Dao.selectOne(new QueryWrapper<BCp01Entity>().eq("CORPCODE", item.getCorpcode()));
            SysUserEntity userEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("MOBILE",item.getLinkcellphone()));
            if (bCp01Entity == null && userEntity==null) {
                //创建机构数据
                SysRegionEntity sysRegionEntity = sysRegionDao.selectById(item.getAreacode());
                String name = sysRegionEntity.getName();
                SysDeptEntity pDept = sysDeptDao.selectOne(new QueryWrapper<SysDeptEntity>().eq("NAME", name));
                SysDeptEntity sysDeptEntity = new SysDeptEntity();
                sysDeptEntity.setName(item.getCorpname());
                sysDeptEntity.setPid(pDept.getId());
                sysDeptEntity.setPids(bCp01Service.getPidList(pDept.getId()));
                sysDeptEntity.setOrgtype("1");
                sysDeptDao.insert(sysDeptEntity);
                //创建用户数据
                SysUserEntity sysUserEntity = new SysUserEntity();
                sysUserEntity.setDeptId(sysDeptEntity.getId());
                sysUserEntity.setMobile(item.getLinkcellphone());//联系电话
                String corpcode = item.getCorpcode();
                sysUserEntity.setUsername(corpcode.substring(corpcode.length()-8));
                sysUserEntity.setRealName("导入企业");//联系人
                sysUserEntity.setSuperAdmin(0);
                sysUserEntity.setStatus(1);
                sysUserEntity.setPassword(PasswordUtils.encode(sysParamsService.getValue(Constant.PASSWORD)));
                sysUserDao.insert(sysUserEntity);
                SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
                sysRoleUserEntity.setUserId(sysUserEntity.getId());
                sysRoleUserEntity.setRoleId(1598227117234376706L);
                sysRoleUserDao.insert(sysRoleUserEntity);
                //创建企业数据
                CompanyDTO companyDTO = AilynUtils.getApiStore(item.getCorpcode());
                BCp01DTO cp01DTO = new BCp01DTO();
                if (companyDTO != null) {
                    //统一社会信用代码
                    cp01DTO.setCorpcode(companyDTO.getCreditCode());
                    //公司名称
                    cp01DTO.setCorpname(companyDTO.getCompanyName());
                    //企业营业地址
                    cp01DTO.setAddress(companyDTO.getAddress());
                    //法人
                    cp01DTO.setLegalman(companyDTO.getFaRen());
                    //联系人
                    cp01DTO.setLinkman(companyDTO.getFaRen());
                    //注册资本
                    if (!"".equals(companyDTO.getRegMoney()) && companyDTO.getRegMoney() != null) {
                        cp01DTO.setRegcapital(new Double(NumberUtil.getNumber(companyDTO.getRegMoney())));
                    }
                    //注册资本币种
                    cp01DTO.setCapitalcurrencytype("1");
                    //成立日期
                    if (!"".equals(companyDTO.getChkDate()) && companyDTO.getChkDate() != null) {
                        cp01DTO.setEstablishdate(DateUtil.parse(companyDTO.getChkDate(), "yyyy-MM-dd"));
                    }

                    //联系人办公电话
                    cp01DTO.setLinkphone(companyDTO.getPhone());
                    //邮箱
                    cp01DTO.setEmail(companyDTO.getEmail());
                    //网址
                    cp01DTO.setWebsite(companyDTO.getWebSite());
                    //经营状态
                    cp01DTO.setBusinessstatus("1");
                    //经营范围
                    cp01DTO.setEntscope(companyDTO.getBussinessDes());
                    //登记机关
                    cp01DTO.setRegdept(companyDTO.getRegOrgName());
                    bCp01Entity = new BCp01Entity();
                    BeanUtils.copyProperties(cp01DTO, bCp01Entity);

                } else {
                    //天眼查未查询到数据
                    bCp01Entity = new BCp01Entity();
                    bCp01Entity.setCorpcode(item.getCorpcode());
                    bCp01Entity.setCorpname(item.getCorpname());


                }
                bCp01Entity.setAreacode(item.getAreacode());
                bCp01Entity.setIndustry(item.getIndustry());
                bCp01Entity.setEstate(item.getEstate());
                bCp01Entity.setScale(item.getScale());
                bCp01Entity.setDeptId(sysDeptEntity.getId());
                bCp01Entity.setLinkcellphone(item.getLinkcellphone());
                bcp01Dao.insert(bCp01Entity);
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
                item.setMsg("导入成功");
                success++;
            }else if(bCp01Entity!= null ){
                itr = "0";
                bad++;
                item.setMsg("企业已经存在");
                item.setResult(itr);
                bCp12Service.updateById(item);
                continue;
            }else{
                //已存在企业
                itr = "0";
                bad++;
                item.setMsg("电话已被占用");
                item.setResult(itr);
                bCp12Service.updateById(item);
                continue;
            }
            item.setResult(itr);
            bCp12Service.updateById(item);
        }//for循环结束
        BCp11Entity bCp11Entity = baseDao.selectById(cp1101);
        bCp11Entity.setFailnumber(bad.toString());
        bCp11Entity.setSuccessnumber(success.toString());
        baseDao.updateById(bCp11Entity);
        result.setCode(0);
        result.setMsg("导入完成，成功"+success+"条，失败"+bad+"条");
        return result;
    }


    private Map<String, Object> checkDate(List<BCp11Excel> collect) {
        for (int i = collect.size() - 1; i >= 0; i--) {
            BCp11Excel excel = collect.get(i);
            //超长验证和非空验证
            if (excel.getCorpcode() != null) {
                excel.setCorpcode(excel.getCorpcode().trim());
                if (excel.getCorpcode().length() > 18) {
                    collect.remove(i);
                    System.out.println("社会统一信用代码不合规，大于18位:"+excel.getCorpname());
                    continue;
                }
            } else {
                collect.remove(i);
                System.out.println("社会统一信用代码为空");
                continue;
            }
            if (excel.getCorpname() != null) {
                excel.setCorpname(excel.getCorpname().trim());
            } else {
                collect.remove(i);
                System.out.println("企业名称不合规");
                continue;
            }
            if (excel.getAreacode() != null) {
                String areacode = excel.getAreacode();
                //查询自定义广安区划
                List<String> areacodeIds = sysRegionService.selectIds();
                if (!areacodeIds.contains(areacode)) {
                    collect.remove(i);
                    System.out.println("区划数据不合规：" + excel.getCorpname());
                    continue;
                }
            } else {
                collect.remove(i);
                continue;
            }
            if (excel.getIndustry() != null) {
                String industry = excel.getIndustry();
                List<String> industrys = baseDao.selectDictData("INDUSTRY");
                if (!industrys.contains(industry)) {
                    collect.remove(i);
                    System.out.println("行业数据不合规：" + excel.getCorpname());
                    continue;
                }
            } else {
                collect.remove(i);
                continue;
            }
            if (excel.getEstate() != null) {
                String estate = excel.getEstate();
                List<String> estates = baseDao.selectDictData("ESTATE");
                if (!estates.contains(estate)) {
                    collect.remove(i);
                    System.out.println("产业数据不合规：" + excel.getCorpname());
                    continue;
                }
            } else {
                collect.remove(i);
                continue;
            }
            if (excel.getScale() != null) {
                String scale = excel.getScale();
                List<String> scales = baseDao.selectDictData("SCALE");
                if (!scales.contains(scale)) {
                    collect.remove(i);
                    System.out.println("企业规模数据不合规：" + excel.getCorpname());
                    continue;
                }
            } else {
                collect.remove(i);
                continue;
            }
            if (excel.getLinkcellphone() != null) {
                excel.setLinkcellphone(excel.getLinkcellphone().trim());
                if (excel.getLinkcellphone().length() > 11) {
                    collect.remove(i);
                    System.out.println("联系电话数据不合规：" + excel.getCorpname());
                    continue;
                }
            } else {
                collect.remove(i);
                continue;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("collect", collect);
        return map;
    }
}