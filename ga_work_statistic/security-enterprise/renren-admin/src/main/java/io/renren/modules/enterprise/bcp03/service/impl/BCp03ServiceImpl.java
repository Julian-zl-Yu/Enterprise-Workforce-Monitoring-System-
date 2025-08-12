package io.renren.modules.enterprise.bcp03.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.CommonUtils;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp01.dao.BCp01Dao;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp03.dao.BCp03Dao;
import io.renren.modules.enterprise.bcp03.dto.BCp03DTO;
import io.renren.modules.enterprise.bcp03.dto.HomePageByRegionDTO;
import io.renren.modules.enterprise.bcp03.dto.HomePageByYearDTO;
import io.renren.modules.enterprise.bcp03.entity.BCp03Entity;
import io.renren.modules.enterprise.bcp03.service.BCp03Service;
import io.renren.modules.enterprise.bcp03config.entity.BCp03ConfigEntity;
import io.renren.modules.enterprise.bcp03config.service.BCp03ConfigService;
import io.renren.modules.enterprise.bcp03echart.dto.BCp03EchartDTO;
import io.renren.modules.enterprise.bcp03tj.dto.*;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysParamsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 企业用工缺工情况表
 *
 * @author JY
 * @since 1.0.0 2025-08-06
 */
@Service
public class BCp03ServiceImpl extends CrudServiceImpl<BCp03Dao, BCp03Entity, BCp03DTO> implements BCp03Service {

    @Autowired
    private SysDeptDao sysDeptDao;

    @Autowired
    private BCp03ConfigService bCp03ConfigService;

    @Autowired
    private SysParamsService sysParamsService;

    private static JSONObject jsonObject;

    @Autowired
    private BCp01Dao cp01Dao;

    @PostConstruct
    public void initPath() {
        jsonObject = sysParamsService.getValueObject(Constant.CP03TJ_URL, JSONObject.class);
    }

    @Override
    public QueryWrapper<BCp03Entity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<BCp03Entity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<BCp03DTO> listByYM(BCp03DTO dto) {
        return baseDao.listByYM(dto);
    }

    @Override
    public PageData<BCp03DTO> pageList(Map<String, Object> params) {
        Long deptId = SecurityUser.getDeptId();
        SysDeptEntity deptEntity = sysDeptDao.getById(deptId);
        String orgtype = deptEntity.getOrgtype();
        if ("1".equals(orgtype)) {
            //企业
            Long cp0101 = CommonUtils.userCpInfo().getCp0101();
            params.put("deptIdSelf", deptId);
        } else {
            //监管
            params.put("deptId", deptId);
        }
        IPage<BCp03Entity> page = getPage(params, "", false);
        params.put("CREATE_DATE", "desc");
        List<BCp03DTO> list = baseDao.getList(params);
        return getPageData(list, page.getTotal(), BCp03DTO.class);
    }

    @Override
    public Result<BCp03EchartDTO> echart(Map<String, Object> params) {
        Long cp0101 = CommonUtils.userCpInfo().getCp0101();
        params.put("cp0101", cp0101);
        BCp03EchartDTO dto = new BCp03EchartDTO();
        List<String> yearMonth = new ArrayList<>();
        List<Long> useController = new ArrayList<>();
        List<Long> useOrdWorker = new ArrayList<>();
        List<Long> useTechWorker = new ArrayList<>();
        List<Long> useSkillWorker = new ArrayList<>();
        List<Long> lackController = new ArrayList<>();
        List<Long> lackOrdWorker = new ArrayList<>();
        List<Long> lackTechWorker = new ArrayList<>();
        List<Long> lackSkillWorker = new ArrayList<>();
        List<BCp03DTO> list = baseDao.echart(params);
        for (BCp03DTO item :
                list) {
            yearMonth.add(item.getYearMonth());
            useController.add(item.getUseController());
            useOrdWorker.add(item.getUseOrdWorker());
            useTechWorker.add(item.getUseTechWorker());
            useSkillWorker.add(item.getUseSkillWorker());
            lackController.add(item.getLackController());
            lackOrdWorker.add(item.getLackOrdWorker());
            lackTechWorker.add(item.getLackTechWorker());
            lackSkillWorker.add(item.getLackSkillWorker());
        }
        dto.setYearMonth(yearMonth);
        dto.setUseController(useController);
        dto.setUseOrdWorker(useOrdWorker);
        dto.setUseTechWorker(useTechWorker);
        dto.setUseSkillWorker(useSkillWorker);
        dto.setLackController(lackController);
        dto.setLackOrdWorker(lackOrdWorker);
        dto.setLackTechWorker(lackTechWorker);
        dto.setLackSkillWorker(lackSkillWorker);
        return new Result<BCp03EchartDTO>().ok(dto);
    }

    @Override
    public Result<GeneralSituationDto> generalSituation(Map<String, Object> params) {
        Long allCpCount = 0L;
        Long allWorkerCount = 0L;
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        //查询当月情况
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);
        List<BCp03DTO> thisMonth = baseDao.getThisMonthData(params);
        //查询上月情况
        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
        String preYm = DateUtil.format(newDate, "yyyy-MM");
        params.put("ym", preYm);
        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
        //查询当年第一月的情况
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("ym", beginYm);
        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
        Long unChangedCpCount = 0L;//较上月用工人数不变企业数量
        Long addWorkerCpCount = 0L;//较上月用工人数增加企业数量
        Long reduceWorkerCpCount = 0L;//较上月用工人数减少企业数量
        Long addWorkerCount = 0L;//较上月增加工人数
        Long reduceWorkerCount = 0L;//较上月减少工人数
        String addWorkerRange = "";//较上月增加工人幅度
        String reduceWorkerRange = "";//较上月减少工人幅度
        Long addWorkerCountCompareBeginYear = 0L;//较年初增加工人数
        Long reduceWorkerCountCompareBeginYear = 0L;//较年初减少工人数
        String addWorkerRangeCompareBeginYear = "";//较年初增加工人幅度
        String reduceWorkerRangeCompareBeginYear = "";//较年初减少工人幅度
        Double thisMonthAllWorkerCount = 0D;//本月在岗总人数
        //比较上月情况
        for (BCp03DTO thisItem :
                thisMonth) {
            Long cp0101 = thisItem.getCp0101();
            thisMonthAllWorkerCount += thisItem.getAllUseWorker();
            for (BCp03DTO preItem :
                    preMonth) {
                Long preCp0101 = preItem.getCp0101();
                if (cp0101.equals(preCp0101)) {
                    //企业数++
                    allCpCount++;
                    //在岗总工人增加
                    allWorkerCount += thisItem.getAllUseWorker();
                    //计算用工总数偏离
                    Long thisAllUseWorker = thisItem.getAllUseWorker();
                    Long preAllUseWorker = preItem.getAllUseWorker();
                    long r1 = thisAllUseWorker - preAllUseWorker;
                    if (r1 == 0) {
                        unChangedCpCount++;
                    } else if (r1 > 0) {
                        addWorkerCpCount++;
                        addWorkerCount += r1;
                    } else {
                        reduceWorkerCpCount++;
                        reduceWorkerCount -= r1;
                    }
                }
            }
            //比较年初的情况
            for (BCp03DTO beginItem :
                    beginMonth) {
                Long beginCp0101 = beginItem.getCp0101();
                if (cp0101.equals(beginCp0101)) {
                    //计算用工总数偏离
                    Long thisAllUseWorker = thisItem.getAllUseWorker();
                    Long beginAllUseWorker = beginItem.getAllUseWorker();
                    long r2 = thisAllUseWorker - beginAllUseWorker;
                    if (r2 == 0) {

                    } else if (r2 > 0) {
                        addWorkerCountCompareBeginYear += r2;
                    } else {
                        reduceWorkerCountCompareBeginYear -= r2;
                    }
                }
            }
        }
        addWorkerRange = Math.round(addWorkerCount * 100.00D / thisMonthAllWorkerCount) + "%";
        reduceWorkerRange = Math.round(reduceWorkerCount * 100.00D / thisMonthAllWorkerCount) + "%";
        addWorkerRangeCompareBeginYear = Math.round(addWorkerCountCompareBeginYear * 100.00D / thisMonthAllWorkerCount) + "%";
        reduceWorkerRangeCompareBeginYear = Math.round(reduceWorkerCountCompareBeginYear * 100.00D / thisMonthAllWorkerCount) + "%";
        GeneralSituationDto item1 = new GeneralSituationDto();
        GeneralSituationDto item2 = new GeneralSituationDto();
        GeneralSituationDto item3 = new GeneralSituationDto();
        GeneralSituationDto item4 = new GeneralSituationDto();
        item1.setCorpType("未变化企业");
        item2.setCorpType("减员企业");
        item3.setCorpType("增员企业");
        item4.setCorpType("总计");
        item1.setChangeWorkerCpCount(unChangedCpCount);
        item2.setChangeWorkerCpCount(reduceWorkerCpCount);
        item3.setChangeWorkerCpCount(addWorkerCpCount);
        item4.setChangeWorkerCpCount(unChangedCpCount + reduceWorkerCpCount + addWorkerCpCount);
        item1.setChangeWorkerCount(0L);
        item2.setChangeWorkerCount(reduceWorkerCount);
        item3.setChangeWorkerCount(addWorkerCount);
        item4.setChangeWorkerCount(addWorkerCount - reduceWorkerCount);
        item1.setChangeWorkerRange("0%");
        item2.setChangeWorkerRange(reduceWorkerRange);
        item3.setChangeWorkerRange(addWorkerRange);
        item4.setChangeWorkerRange(Math.round((addWorkerCount - reduceWorkerCount) * 100.00D / thisMonthAllWorkerCount) + "%");
        item1.setChangeWorkerCountCompareBeginYear(0L);
        item2.setChangeWorkerCountCompareBeginYear(reduceWorkerCountCompareBeginYear);
        item3.setChangeWorkerCountCompareBeginYear(addWorkerCountCompareBeginYear);
        item4.setChangeWorkerCountCompareBeginYear(addWorkerCountCompareBeginYear - reduceWorkerCountCompareBeginYear);
        item1.setChangeWorkerRangeCompareBeginYear("0%");
        item2.setChangeWorkerRangeCompareBeginYear(reduceWorkerRangeCompareBeginYear);
        item3.setChangeWorkerRangeCompareBeginYear(addWorkerRangeCompareBeginYear);
        item4.setChangeWorkerRangeCompareBeginYear(Math.round((addWorkerCountCompareBeginYear - reduceWorkerCountCompareBeginYear) * 100.00D / thisMonthAllWorkerCount) + "%");
        List<GeneralSituationDto> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        Result<GeneralSituationDto> result = new Result();
        GeneralSituationDto dto = new GeneralSituationDto();
        dto.setAllCpCount(allCpCount);
        dto.setAllWorkerCount(allWorkerCount);
        dto.setList(list);
        result.ok(dto);
        return result;
    }

    @Override
    public Result<ByIndustryDto> byIndustry(Map<String, Object> params) {
        //查询当月情况
//        UserDetail user = SecurityUser.getUser();
//        Long deptId = user.getDeptId();
//        params.put("deptId", deptId.toString());
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);//"yyyy-MM"
        DateTime dt = DateUtil.parse(ym+"-01");
        DateTime lastDt = DateUtil.offset(dt, DateField.MONTH, -1);
        String lastYm = DateUtil.format(lastDt, "yyyy-MM");
        params.put("lastYm",lastYm);
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("beginYm",beginYm);
        //根据前端传来的ym 得到上个月的年月和当年第一个月的年月
//        List<BCp03DTO> thisMonth = baseDao.getThisMonthData(params);
//        //查询上月情况
//        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
//        String preYm = DateUtil.format(newDate, "yyyy-MM");
//        params.put("ym", preYm);
//        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
//        //查询当年第一月的情况
//        String beginYm = ym.substring(0, 4) + "-01";
//        params.put("ym", beginYm);
//        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
//        //定义各行业监测企业数
//        Long cpCount1 = 0L;
//        Long cpCount2 = 0L;
//        Long cpCount3 = 0L;
//        Long cpCount4 = 0L;
//        Long cpCount5 = 0L;
//        Long cpCount6 = 0L;
//        Long cpCount7 = 0L;
//        Long cpCount8 = 0L;
//        Long cpCount9 = 0L;
//        Long cpCount10 = 0L;
//        Long cpCount11 = 0L;
//        Long cpCount12 = 0L;
//        Long cpCount13 = 0L;
//        Long cpCount14 = 0L;
//        Long cpCount15 = 0L;
//        Long cpCount16 = 0L;
//        Long cpCount17 = 0L;
//        //定义上月用工人数
//        Long preWorkerCount1 = 0L;
//        Long preWorkerCount2 = 0L;
//        Long preWorkerCount3 = 0L;
//        Long preWorkerCount4 = 0L;
//        Long preWorkerCount5 = 0L;
//        Long preWorkerCount6 = 0L;
//        Long preWorkerCount7 = 0L;
//        Long preWorkerCount8 = 0L;
//        Long preWorkerCount9 = 0L;
//        Long preWorkerCount10 = 0L;
//        Long preWorkerCount11 = 0L;
//        Long preWorkerCount12 = 0L;
//        Long preWorkerCount13 = 0L;
//        Long preWorkerCount14 = 0L;
//        Long preWorkerCount15 = 0L;
//        Long preWorkerCount16 = 0L;
//        Long preWorkerCount17 = 0L;
//        //定义本月用工人数
//        Long nowWorkerCount1 = 0L;
//        Long nowWorkerCount2 = 0L;
//        Long nowWorkerCount3 = 0L;
//        Long nowWorkerCount4 = 0L;
//        Long nowWorkerCount5 = 0L;
//        Long nowWorkerCount6 = 0L;
//        Long nowWorkerCount7 = 0L;
//        Long nowWorkerCount8 = 0L;
//        Long nowWorkerCount9 = 0L;
//        Long nowWorkerCount10 = 0L;
//        Long nowWorkerCount11 = 0L;
//        Long nowWorkerCount12 = 0L;
//        Long nowWorkerCount13 = 0L;
//        Long nowWorkerCount14 = 0L;
//        Long nowWorkerCount15 = 0L;
//        Long nowWorkerCount16 = 0L;
//        Long nowWorkerCount17 = 0L;
//        //定义年初用工人数
//        Long beginWorkerCount1 = 0L;
//        Long beginWorkerCount2 = 0L;
//        Long beginWorkerCount3 = 0L;
//        Long beginWorkerCount4 = 0L;
//        Long beginWorkerCount5 = 0L;
//        Long beginWorkerCount6 = 0L;
//        Long beginWorkerCount7 = 0L;
//        Long beginWorkerCount8 = 0L;
//        Long beginWorkerCount9 = 0L;
//        Long beginWorkerCount10 = 0L;
//        Long beginWorkerCount11 = 0L;
//        Long beginWorkerCount12 = 0L;
//        Long beginWorkerCount13 = 0L;
//        Long beginWorkerCount14 = 0L;
//        Long beginWorkerCount15 = 0L;
//        Long beginWorkerCount16 = 0L;
//        Long beginWorkerCount17 = 0L;
//        for (BCp03DTO nowItem : thisMonth) {
//            Long thisCp0101 = nowItem.getCp0101();
//            String thisIndustry = nowItem.getIndustry();
//            //计算监测企业数,计算当月总工人数
//            switch (thisIndustry) {
//                case "1":
//                    cpCount1++;
//                    nowWorkerCount1 += nowItem.getAllUseWorker();
//                    break;
//                case "2":
//                    cpCount2++;
//                    nowWorkerCount2 += nowItem.getAllUseWorker();
//                    break;
//                case "3":
//                    cpCount3++;
//                    nowWorkerCount3 += nowItem.getAllUseWorker();
//                    break;
//                case "4":
//                    cpCount4++;
//                    nowWorkerCount4 += nowItem.getAllUseWorker();
//                    break;
//                case "5":
//                    cpCount5++;
//                    nowWorkerCount5 += nowItem.getAllUseWorker();
//                    break;
//                case "6":
//                    cpCount6++;
//                    nowWorkerCount6 += nowItem.getAllUseWorker();
//                    break;
//                case "7":
//                    cpCount7++;
//                    nowWorkerCount7 += nowItem.getAllUseWorker();
//                    break;
//                case "8":
//                    cpCount8++;
//                    nowWorkerCount8 += nowItem.getAllUseWorker();
//                    break;
//                case "9":
//                    cpCount9++;
//                    nowWorkerCount9 += nowItem.getAllUseWorker();
//                    break;
//                case "10":
//                    cpCount10++;
//                    nowWorkerCount10 += nowItem.getAllUseWorker();
//                    break;
//                case "11":
//                    cpCount11++;
//                    nowWorkerCount11 += nowItem.getAllUseWorker();
//                    break;
//                case "12":
//                    cpCount12++;
//                    nowWorkerCount12 += nowItem.getAllUseWorker();
//                    break;
//                case "13":
//                    cpCount13++;
//                    nowWorkerCount13 += nowItem.getAllUseWorker();
//                    break;
//                case "14":
//                    cpCount14++;
//                    nowWorkerCount14 += nowItem.getAllUseWorker();
//                    break;
//                case "15":
//                    cpCount15++;
//                    nowWorkerCount15 += nowItem.getAllUseWorker();
//                    break;
//                case "16":
//                    cpCount16++;
//                    nowWorkerCount16 += nowItem.getAllUseWorker();
//                    break;
//                case "17":
//                    cpCount17++;
//                    nowWorkerCount17 += nowItem.getAllUseWorker();
//                    break;
//            }
//        }//外层循环结束
//        //和上月比较
//        for (BCp03DTO preItem : preMonth) {
//            Long preCp0101 = preItem.getCp0101();
//            String preIndustry = preItem.getIndustry();
//            //计算上月总工人数
//            switch (preIndustry) {
//                case "1":
//                    preWorkerCount1 += preItem.getAllUseWorker();
//                    break;
//                case "2":
//                    preWorkerCount2 += preItem.getAllUseWorker();
//                    break;
//                case "3":
//                    preWorkerCount3 += preItem.getAllUseWorker();
//                    break;
//                case "4":
//                    preWorkerCount4 += preItem.getAllUseWorker();
//                    break;
//                case "5":
//                    preWorkerCount5 += preItem.getAllUseWorker();
//                    break;
//                case "6":
//                    preWorkerCount6 += preItem.getAllUseWorker();
//                    break;
//                case "7":
//                    preWorkerCount7 += preItem.getAllUseWorker();
//                    break;
//                case "8":
//                    preWorkerCount8 += preItem.getAllUseWorker();
//                    break;
//                case "9":
//                    preWorkerCount9 += preItem.getAllUseWorker();
//                    break;
//                case "10":
//                    preWorkerCount10 += preItem.getAllUseWorker();
//                    break;
//                case "11":
//                    preWorkerCount11 += preItem.getAllUseWorker();
//                    break;
//                case "12":
//                    preWorkerCount12 += preItem.getAllUseWorker();
//                    break;
//                case "13":
//                    preWorkerCount13 += preItem.getAllUseWorker();
//                    break;
//                case "14":
//                    preWorkerCount14 += preItem.getAllUseWorker();
//                    break;
//                case "15":
//                    preWorkerCount15 += preItem.getAllUseWorker();
//                    break;
//                case "16":
//                    preWorkerCount16 += preItem.getAllUseWorker();
//                    break;
//                case "17":
//                    preWorkerCount17 += preItem.getAllUseWorker();
//                    break;
//            }
//        }
//        //和年初比较
//        for (BCp03DTO beginItem : beginMonth) {
//            Long beginCp0101 = beginItem.getCp0101();
//            String beginIndustry = beginItem.getIndustry();
//            //计算年初总工人数
//            switch (beginIndustry) {
//                case "1":
//                    beginWorkerCount1 += beginItem.getAllUseWorker();
//                    break;
//                case "2":
//                    beginWorkerCount2 += beginItem.getAllUseWorker();
//                    break;
//                case "3":
//                    beginWorkerCount3 += beginItem.getAllUseWorker();
//                    break;
//                case "4":
//                    beginWorkerCount4 += beginItem.getAllUseWorker();
//                    break;
//                case "5":
//                    beginWorkerCount5 += beginItem.getAllUseWorker();
//                    break;
//                case "6":
//                    beginWorkerCount6 += beginItem.getAllUseWorker();
//                    break;
//                case "7":
//                    beginWorkerCount7 += beginItem.getAllUseWorker();
//                    break;
//                case "8":
//                    beginWorkerCount8 += beginItem.getAllUseWorker();
//                    break;
//                case "9":
//                    beginWorkerCount9 += beginItem.getAllUseWorker();
//                    break;
//                case "10":
//                    beginWorkerCount10 += beginItem.getAllUseWorker();
//                    break;
//                case "11":
//                    beginWorkerCount11 += beginItem.getAllUseWorker();
//                    break;
//                case "12":
//                    beginWorkerCount12 += beginItem.getAllUseWorker();
//                    break;
//                case "13":
//                    beginWorkerCount13 += beginItem.getAllUseWorker();
//                    break;
//                case "14":
//                    beginWorkerCount14 += beginItem.getAllUseWorker();
//                    break;
//                case "15":
//                    beginWorkerCount15 += beginItem.getAllUseWorker();
//                    break;
//                case "16":
//                    beginWorkerCount16 += beginItem.getAllUseWorker();
//                    break;
//                case "17":
//                    beginWorkerCount17 += beginItem.getAllUseWorker();
//                    break;
//            }
//        }
//        //构造数据
//        ByIndustryDto dto1 = new ByIndustryDto();
//        ByIndustryDto dto2 = new ByIndustryDto();
//        ByIndustryDto dto3 = new ByIndustryDto();
//        ByIndustryDto dto4 = new ByIndustryDto();
//        ByIndustryDto dto5 = new ByIndustryDto();
//        ByIndustryDto dto6 = new ByIndustryDto();
//        ByIndustryDto dto7 = new ByIndustryDto();
//        ByIndustryDto dto8 = new ByIndustryDto();
//        ByIndustryDto dto9 = new ByIndustryDto();
//        ByIndustryDto dto10 = new ByIndustryDto();
//        ByIndustryDto dto11 = new ByIndustryDto();
//        ByIndustryDto dto12 = new ByIndustryDto();
//        ByIndustryDto dto13 = new ByIndustryDto();
//        ByIndustryDto dto14 = new ByIndustryDto();
//        ByIndustryDto dto15 = new ByIndustryDto();
//        ByIndustryDto dto16 = new ByIndustryDto();
//        ByIndustryDto dto17 = new ByIndustryDto();
//        ByIndustryDto dto18 = new ByIndustryDto();
//        //查询行业码表
//        List<String> industryList = baseDao.selectIndustryList();
//        dto1.setIndustry(industryList.get(0));
//        dto2.setIndustry(industryList.get(1));
//        dto3.setIndustry(industryList.get(2));
//        dto4.setIndustry(industryList.get(3));
//        dto5.setIndustry(industryList.get(4));
//        dto6.setIndustry(industryList.get(5));
//        dto7.setIndustry(industryList.get(6));
//        dto8.setIndustry(industryList.get(7));
//        dto9.setIndustry(industryList.get(8));
//        dto10.setIndustry(industryList.get(9));
//        dto11.setIndustry(industryList.get(10));
//        dto12.setIndustry(industryList.get(11));
//        dto13.setIndustry(industryList.get(12));
//        dto14.setIndustry(industryList.get(13));
//        dto15.setIndustry(industryList.get(14));
//        dto16.setIndustry(industryList.get(15));
//        dto17.setIndustry(industryList.get(16));
//        dto18.setIndustry("总计");
//        //设置监测企业数
//        dto1.setCpCount(cpCount1);
//        dto2.setCpCount(cpCount2);
//        dto3.setCpCount(cpCount3);
//        dto4.setCpCount(cpCount4);
//        dto5.setCpCount(cpCount5);
//        dto6.setCpCount(cpCount6);
//        dto7.setCpCount(cpCount7);
//        dto8.setCpCount(cpCount8);
//        dto9.setCpCount(cpCount9);
//        dto10.setCpCount(cpCount10);
//        dto11.setCpCount(cpCount11);
//        dto12.setCpCount(cpCount12);
//        dto13.setCpCount(cpCount13);
//        dto14.setCpCount(cpCount14);
//        dto15.setCpCount(cpCount15);
//        dto16.setCpCount(cpCount16);
//        dto17.setCpCount(cpCount17);
//        dto18.setCpCount(cpCount1 + cpCount2 + cpCount3 + cpCount4 + cpCount5 + cpCount6 + cpCount7 + cpCount8 + cpCount9 + cpCount10 + cpCount11 + cpCount12 + cpCount13 + cpCount14 + cpCount15 + cpCount16 + cpCount17);
//        //设置上期工人数
//        dto1.setPreWorkerCount(preWorkerCount1);
//        dto2.setPreWorkerCount(preWorkerCount2);
//        dto3.setPreWorkerCount(preWorkerCount3);
//        dto4.setPreWorkerCount(preWorkerCount4);
//        dto5.setPreWorkerCount(preWorkerCount5);
//        dto6.setPreWorkerCount(preWorkerCount6);
//        dto7.setPreWorkerCount(preWorkerCount7);
//        dto8.setPreWorkerCount(preWorkerCount8);
//        dto9.setPreWorkerCount(preWorkerCount9);
//        dto10.setPreWorkerCount(preWorkerCount10);
//        dto11.setPreWorkerCount(preWorkerCount11);
//        dto12.setPreWorkerCount(preWorkerCount12);
//        dto13.setPreWorkerCount(preWorkerCount13);
//        dto14.setPreWorkerCount(preWorkerCount14);
//        dto15.setPreWorkerCount(preWorkerCount15);
//        dto16.setPreWorkerCount(preWorkerCount16);
//        dto17.setPreWorkerCount(preWorkerCount17);
//        dto18.setPreWorkerCount(preWorkerCount1 + preWorkerCount2 + preWorkerCount3 + preWorkerCount4 + preWorkerCount5 + preWorkerCount6 + preWorkerCount7 + preWorkerCount8 + preWorkerCount9 + preWorkerCount10 + preWorkerCount11 + preWorkerCount12 + preWorkerCount13 + preWorkerCount14 + preWorkerCount15 + preWorkerCount16 + preWorkerCount17);
//        //设置本期工人数
//        dto1.setNowWorkerCount(nowWorkerCount1);
//        dto2.setNowWorkerCount(nowWorkerCount2);
//        dto3.setNowWorkerCount(nowWorkerCount3);
//        dto4.setNowWorkerCount(nowWorkerCount4);
//        dto5.setNowWorkerCount(nowWorkerCount5);
//        dto6.setNowWorkerCount(nowWorkerCount6);
//        dto7.setNowWorkerCount(nowWorkerCount7);
//        dto8.setNowWorkerCount(nowWorkerCount8);
//        dto9.setNowWorkerCount(nowWorkerCount9);
//        dto10.setNowWorkerCount(nowWorkerCount10);
//        dto11.setNowWorkerCount(nowWorkerCount11);
//        dto12.setNowWorkerCount(nowWorkerCount12);
//        dto13.setNowWorkerCount(nowWorkerCount13);
//        dto14.setNowWorkerCount(nowWorkerCount14);
//        dto15.setNowWorkerCount(nowWorkerCount15);
//        dto16.setNowWorkerCount(nowWorkerCount16);
//        dto17.setNowWorkerCount(nowWorkerCount17);
//        dto18.setNowWorkerCount(nowWorkerCount1 + nowWorkerCount2 + nowWorkerCount3 + nowWorkerCount4 + nowWorkerCount5 + nowWorkerCount6 + nowWorkerCount7 + nowWorkerCount8 + nowWorkerCount9 + nowWorkerCount10 + nowWorkerCount11 + nowWorkerCount12 + nowWorkerCount13 + nowWorkerCount14 + nowWorkerCount15 + nowWorkerCount16 + nowWorkerCount17);
//        //设置对比上期变化人数
//        dto1.setChangeWorkerCount(nowWorkerCount1 - preWorkerCount1);
//        dto2.setChangeWorkerCount(nowWorkerCount2 - preWorkerCount2);
//        dto3.setChangeWorkerCount(nowWorkerCount3 - preWorkerCount3);
//        dto4.setChangeWorkerCount(nowWorkerCount4 - preWorkerCount4);
//        dto5.setChangeWorkerCount(nowWorkerCount5 - preWorkerCount5);
//        dto6.setChangeWorkerCount(nowWorkerCount6 - preWorkerCount6);
//        dto7.setChangeWorkerCount(nowWorkerCount7 - preWorkerCount7);
//        dto8.setChangeWorkerCount(nowWorkerCount8 - preWorkerCount8);
//        dto9.setChangeWorkerCount(nowWorkerCount9 - preWorkerCount9);
//        dto10.setChangeWorkerCount(nowWorkerCount10 - preWorkerCount10);
//        dto11.setChangeWorkerCount(nowWorkerCount11 - preWorkerCount11);
//        dto12.setChangeWorkerCount(nowWorkerCount12 - preWorkerCount12);
//        dto13.setChangeWorkerCount(nowWorkerCount13 - preWorkerCount13);
//        dto14.setChangeWorkerCount(nowWorkerCount14 - preWorkerCount14);
//        dto15.setChangeWorkerCount(nowWorkerCount15 - preWorkerCount15);
//        dto16.setChangeWorkerCount(nowWorkerCount16 - preWorkerCount16);
//        dto17.setChangeWorkerCount(nowWorkerCount17 - preWorkerCount17);
//        dto18.setChangeWorkerCount(dto18.getNowWorkerCount() - dto18.getPreWorkerCount());
//        //设置对比上期变化幅度
//        dto1.setChangeWorkerRange(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - preWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
//        dto2.setChangeWorkerRange(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - preWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
//        dto3.setChangeWorkerRange(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - preWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
//        dto4.setChangeWorkerRange(nowWorkerCount4 == 0L ? "0%" : Math.round((nowWorkerCount4 - preWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
//        dto5.setChangeWorkerRange(nowWorkerCount5 == 0L ? "0%" : Math.round((nowWorkerCount5 - preWorkerCount5) * 100.00D / nowWorkerCount5) + "%");
//        dto6.setChangeWorkerRange(nowWorkerCount6 == 0L ? "0%" : Math.round((nowWorkerCount6 - preWorkerCount6) * 100.00D / nowWorkerCount6) + "%");
//        dto7.setChangeWorkerRange(nowWorkerCount7 == 0L ? "0%" : Math.round((nowWorkerCount7 - preWorkerCount7) * 100.00D / nowWorkerCount7) + "%");
//        dto8.setChangeWorkerRange(nowWorkerCount8 == 0L ? "0%" : Math.round((nowWorkerCount8 - preWorkerCount8) * 100.00D / nowWorkerCount8) + "%");
//        dto9.setChangeWorkerRange(nowWorkerCount9 == 0L ? "0%" : Math.round((nowWorkerCount9 - preWorkerCount9) * 100.00D / nowWorkerCount9) + "%");
//        dto10.setChangeWorkerRange(nowWorkerCount10 == 0L ? "0%" : Math.round((nowWorkerCount10 - preWorkerCount10) * 100.00D / nowWorkerCount10) + "%");
//        dto11.setChangeWorkerRange(nowWorkerCount11 == 0L ? "0%" : Math.round((nowWorkerCount11 - preWorkerCount11) * 100.00D / nowWorkerCount11) + "%");
//        dto12.setChangeWorkerRange(nowWorkerCount12 == 0L ? "0%" : Math.round((nowWorkerCount12 - preWorkerCount12) * 100.00D / nowWorkerCount12) + "%");
//        dto13.setChangeWorkerRange(nowWorkerCount13 == 0L ? "0%" : Math.round((nowWorkerCount13 - preWorkerCount13) * 100.00D / nowWorkerCount13) + "%");
//        dto14.setChangeWorkerRange(nowWorkerCount14 == 0L ? "0%" : Math.round((nowWorkerCount14 - preWorkerCount14) * 100.00D / nowWorkerCount14) + "%");
//        dto15.setChangeWorkerRange(nowWorkerCount15 == 0L ? "0%" : Math.round((nowWorkerCount15 - preWorkerCount15) * 100.00D / nowWorkerCount15) + "%");
//        dto16.setChangeWorkerRange(nowWorkerCount16 == 0L ? "0%" : Math.round((nowWorkerCount16 - preWorkerCount16) * 100.00D / nowWorkerCount16) + "%");
//        dto17.setChangeWorkerRange(nowWorkerCount17 == 0L ? "0%" : Math.round((nowWorkerCount17 - preWorkerCount17) * 100.00D / nowWorkerCount17) + "%");
//        dto18.setChangeWorkerRange(dto18.getNowWorkerCount() == 0L ? "0%" : Math.round((dto18.getNowWorkerCount() - dto18.getPreWorkerCount()) * 100.00D / dto18.getNowWorkerCount()) + "%");
//        //设置年初人数总数
//        dto18.setBeginWorkerCount(beginWorkerCount1 + beginWorkerCount2 + beginWorkerCount3 + beginWorkerCount4 + beginWorkerCount5 + beginWorkerCount6 + beginWorkerCount7 + beginWorkerCount8 + beginWorkerCount9 + beginWorkerCount10 + beginWorkerCount11 + beginWorkerCount12 + beginWorkerCount13 + beginWorkerCount14 + beginWorkerCount15 + beginWorkerCount16 + beginWorkerCount17);
//        //设置对比年初变化人数
//        dto1.setChangeWorkerCountCompareBeginYear(nowWorkerCount1 - beginWorkerCount1);
//        dto2.setChangeWorkerCountCompareBeginYear(nowWorkerCount2 - beginWorkerCount2);
//        dto3.setChangeWorkerCountCompareBeginYear(nowWorkerCount3 - beginWorkerCount3);
//        dto4.setChangeWorkerCountCompareBeginYear(nowWorkerCount4 - beginWorkerCount4);
//        dto5.setChangeWorkerCountCompareBeginYear(nowWorkerCount5 - beginWorkerCount5);
//        dto6.setChangeWorkerCountCompareBeginYear(nowWorkerCount6 - beginWorkerCount6);
//        dto7.setChangeWorkerCountCompareBeginYear(nowWorkerCount7 - beginWorkerCount7);
//        dto8.setChangeWorkerCountCompareBeginYear(nowWorkerCount8 - beginWorkerCount8);
//        dto9.setChangeWorkerCountCompareBeginYear(nowWorkerCount9 - beginWorkerCount9);
//        dto10.setChangeWorkerCountCompareBeginYear(nowWorkerCount10 - beginWorkerCount10);
//        dto11.setChangeWorkerCountCompareBeginYear(nowWorkerCount11 - beginWorkerCount11);
//        dto12.setChangeWorkerCountCompareBeginYear(nowWorkerCount12 - beginWorkerCount12);
//        dto13.setChangeWorkerCountCompareBeginYear(nowWorkerCount13 - beginWorkerCount13);
//        dto14.setChangeWorkerCountCompareBeginYear(nowWorkerCount14 - beginWorkerCount14);
//        dto15.setChangeWorkerCountCompareBeginYear(nowWorkerCount15 - beginWorkerCount15);
//        dto16.setChangeWorkerCountCompareBeginYear(nowWorkerCount16 - beginWorkerCount16);
//        dto17.setChangeWorkerCountCompareBeginYear(nowWorkerCount17 - beginWorkerCount17);
//        dto18.setChangeWorkerCountCompareBeginYear(dto18.getNowWorkerCount() - dto18.getBeginWorkerCount());
//        //设置对比年初变化幅度
//        dto1.setChangeWorkerRangeCompareBeginYear(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - beginWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
//        dto2.setChangeWorkerRangeCompareBeginYear(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - beginWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
//        dto3.setChangeWorkerRangeCompareBeginYear(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - beginWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
//        dto4.setChangeWorkerRangeCompareBeginYear(nowWorkerCount4 == 0L ? "0%" : Math.round((nowWorkerCount4 - beginWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
//        dto5.setChangeWorkerRangeCompareBeginYear(nowWorkerCount5 == 0L ? "0%" : Math.round((nowWorkerCount5 - beginWorkerCount5) * 100.00D / nowWorkerCount5) + "%");
//        dto6.setChangeWorkerRangeCompareBeginYear(nowWorkerCount6 == 0L ? "0%" : Math.round((nowWorkerCount6 - beginWorkerCount6) * 100.00D / nowWorkerCount6) + "%");
//        dto7.setChangeWorkerRangeCompareBeginYear(nowWorkerCount7 == 0L ? "0%" : Math.round((nowWorkerCount7 - beginWorkerCount7) * 100.00D / nowWorkerCount7) + "%");
//        dto8.setChangeWorkerRangeCompareBeginYear(nowWorkerCount8 == 0L ? "0%" : Math.round((nowWorkerCount8 - beginWorkerCount8) * 100.00D / nowWorkerCount8) + "%");
//        dto9.setChangeWorkerRangeCompareBeginYear(nowWorkerCount9 == 0L ? "0%" : Math.round((nowWorkerCount9 - beginWorkerCount9) * 100.00D / nowWorkerCount9) + "%");
//        dto10.setChangeWorkerRangeCompareBeginYear(nowWorkerCount10 == 0L ? "0%" : Math.round((nowWorkerCount10 - beginWorkerCount10) * 100.00D / nowWorkerCount10) + "%");
//        dto11.setChangeWorkerRangeCompareBeginYear(nowWorkerCount11 == 0L ? "0%" : Math.round((nowWorkerCount11 - beginWorkerCount11) * 100.00D / nowWorkerCount11) + "%");
//        dto12.setChangeWorkerRangeCompareBeginYear(nowWorkerCount12 == 0L ? "0%" : Math.round((nowWorkerCount12 - beginWorkerCount12) * 100.00D / nowWorkerCount12) + "%");
//        dto13.setChangeWorkerRangeCompareBeginYear(nowWorkerCount13 == 0L ? "0%" : Math.round((nowWorkerCount13 - beginWorkerCount13) * 100.00D / nowWorkerCount13) + "%");
//        dto14.setChangeWorkerRangeCompareBeginYear(nowWorkerCount14 == 0L ? "0%" : Math.round((nowWorkerCount14 - beginWorkerCount14) * 100.00D / nowWorkerCount14) + "%");
//        dto15.setChangeWorkerRangeCompareBeginYear(nowWorkerCount15 == 0L ? "0%" : Math.round((nowWorkerCount15 - beginWorkerCount15) * 100.00D / nowWorkerCount15) + "%");
//        dto16.setChangeWorkerRangeCompareBeginYear(nowWorkerCount16 == 0L ? "0%" : Math.round((nowWorkerCount16 - beginWorkerCount16) * 100.00D / nowWorkerCount16) + "%");
//        dto17.setChangeWorkerRangeCompareBeginYear(nowWorkerCount17 == 0L ? "0%" : Math.round((nowWorkerCount17 - beginWorkerCount17) * 100.00D / nowWorkerCount17) + "%");
//        dto18.setChangeWorkerRangeCompareBeginYear(dto18.getNowWorkerCount() == 0L ? "0%" : Math.round((dto18.getNowWorkerCount() - dto18.getBeginWorkerCount()) * 100.00D / dto18.getNowWorkerCount()) + "%");
        List<ByIndustryDto> list = baseDao.selectByIndustry(params);
//        list.add(dto1);
//        list.add(dto2);
//        list.add(dto4);
//        list.add(dto5);
//        list.add(dto6);
//        list.add(dto7);
//        list.add(dto8);
//        list.add(dto9);
//        list.add(dto10);
//        list.add(dto11);
//        list.add(dto12);
//        list.add(dto13);
//        list.add(dto14);
//        list.add(dto15);
//        list.add(dto16);
//        list.add(dto17);
//        list.add(dto18);
        ByIndustryDto dto = new ByIndustryDto();
        dto.setList(list);
        Result<ByIndustryDto> result = new Result<>();
        result.ok(dto);
        return result;
    }

    @Override
    public Result<ByEstateDto> byEstate(Map<String, Object> params) {
        //查询当月情况
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);
        List<BCp03DTO> thisMonth = baseDao.getThisMonthData(params);
        //查询上月情况
        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
        String preYm = DateUtil.format(newDate, "yyyy-MM");
        params.put("ym", preYm);
        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
        //查询当年第一月的情况
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("ym", beginYm);
        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
        //定义各行业监测企业数
        Long cpCount1 = 0L;
        Long cpCount2 = 0L;
        Long cpCount3 = 0L;
        //定义上月用工人数
        Long preWorkerCount1 = 0L;
        Long preWorkerCount2 = 0L;
        Long preWorkerCount3 = 0L;
        //定义本月用工人数
        Long nowWorkerCount1 = 0L;
        Long nowWorkerCount2 = 0L;
        Long nowWorkerCount3 = 0L;
        //定义年初用工人数
        Long beginWorkerCount1 = 0L;
        Long beginWorkerCount2 = 0L;
        Long beginWorkerCount3 = 0L;
        for (BCp03DTO thisItem :
                thisMonth) {
            Long thisCp0101 = thisItem.getCp0101();
            String thisItemEstate = thisItem.getEstate();
            //计算监测企业数,计算当月总工人数
            switch (thisItemEstate) {
                case "1":
                    cpCount1++;
                    nowWorkerCount1 += thisItem.getAllUseWorker();
                    break;
                case "2":
                    cpCount2++;
                    nowWorkerCount2 += thisItem.getAllUseWorker();
                    break;
                case "3":
                    cpCount3++;
                    nowWorkerCount3 += thisItem.getAllUseWorker();
                    break;
            }
        }
        //上月循环
        for (BCp03DTO preItem :
                preMonth) {
            String preItemEstate = preItem.getEstate();
            //计算上月总工人数
            switch (preItemEstate) {
                case "1":
                    preWorkerCount1 += preItem.getAllUseWorker();
                    break;
                case "2":
                    preWorkerCount2 += preItem.getAllUseWorker();
                    break;
                case "3":
                    preWorkerCount3 += preItem.getAllUseWorker();
                    break;
            }
        }
        //年初循环
        for (BCp03DTO beginItem :
                beginMonth) {
            String beginItemEstate = beginItem.getEstate();
            //计算年初总工人数
            switch (beginItemEstate) {
                case "1":
                    beginWorkerCount1 += beginItem.getAllUseWorker();
                    break;
                case "2":
                    beginWorkerCount2 += beginItem.getAllUseWorker();
                    break;
                case "3":
                    beginWorkerCount3 += beginItem.getAllUseWorker();
                    break;
            }
        }
        //构造数据
        ByEstateDto byEstateDto1 = new ByEstateDto();
        ByEstateDto byEstateDto2 = new ByEstateDto();
        ByEstateDto byEstateDto3 = new ByEstateDto();
        ByEstateDto byEstateDto4 = new ByEstateDto();
        byEstateDto1.setEstate("第一产业");
        byEstateDto2.setEstate("第二产业");
        byEstateDto3.setEstate("第三产业");
        byEstateDto4.setEstate("总计");
        byEstateDto1.setCpCount(cpCount1);
        byEstateDto2.setCpCount(cpCount2);
        byEstateDto3.setCpCount(cpCount3);
        byEstateDto4.setCpCount(cpCount1 + cpCount2 + cpCount3);
        byEstateDto1.setNowWorkerCount(nowWorkerCount1);
        byEstateDto2.setNowWorkerCount(nowWorkerCount2);
        byEstateDto3.setNowWorkerCount(nowWorkerCount3);
        byEstateDto4.setNowWorkerCount(nowWorkerCount1 + nowWorkerCount2 + nowWorkerCount3);
        byEstateDto1.setPreWorkerCount(preWorkerCount1);
        byEstateDto2.setPreWorkerCount(preWorkerCount2);
        byEstateDto3.setPreWorkerCount(preWorkerCount3);
        byEstateDto4.setPreWorkerCount(preWorkerCount1 + preWorkerCount2 + preWorkerCount3);
        byEstateDto1.setChangeWorkerCount(nowWorkerCount1 - preWorkerCount1);
        byEstateDto2.setChangeWorkerCount(nowWorkerCount2 - preWorkerCount2);
        byEstateDto3.setChangeWorkerCount(nowWorkerCount3 - preWorkerCount3);
        byEstateDto4.setChangeWorkerCount(byEstateDto4.getNowWorkerCount() - byEstateDto4.getPreWorkerCount());
        byEstateDto1.setChangeWorkerRange(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - preWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byEstateDto2.setChangeWorkerRange(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - preWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byEstateDto3.setChangeWorkerRange(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - preWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byEstateDto4.setChangeWorkerRange(byEstateDto4.getNowWorkerCount() == 0L ? "0%" : Math.round((byEstateDto4.getNowWorkerCount() - byEstateDto4.getPreWorkerCount()) * 100.00D / byEstateDto4.getNowWorkerCount()) + "%");
        byEstateDto1.setChangeWorkerCountCompareBeginYear(nowWorkerCount1 - beginWorkerCount1);
        byEstateDto2.setChangeWorkerCountCompareBeginYear(nowWorkerCount2 - beginWorkerCount2);
        byEstateDto3.setChangeWorkerCountCompareBeginYear(nowWorkerCount3 - beginWorkerCount3);
        //年初工人数
        byEstateDto4.setBeginWorkerCount(beginWorkerCount1 + beginWorkerCount2 + beginWorkerCount3);
        byEstateDto4.setChangeWorkerCountCompareBeginYear(byEstateDto4.getNowWorkerCount() - byEstateDto4.getBeginWorkerCount());
        byEstateDto1.setChangeWorkerRangeCompareBeginYear(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - beginWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byEstateDto2.setChangeWorkerRangeCompareBeginYear(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - beginWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byEstateDto3.setChangeWorkerRangeCompareBeginYear(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - beginWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byEstateDto4.setChangeWorkerRangeCompareBeginYear(byEstateDto4.getNowWorkerCount() == 0L ? "0%" : Math.round((byEstateDto4.getNowWorkerCount() - byEstateDto4.getBeginWorkerCount()) * 100.00D / byEstateDto4.getNowWorkerCount()) + "%");
        List<ByEstateDto> list = new ArrayList<>();
        list.add(byEstateDto1);
        list.add(byEstateDto2);
        list.add(byEstateDto3);
        list.add(byEstateDto4);
        ByEstateDto dto = new ByEstateDto();
        dto.setList(list);
        Result<ByEstateDto> result = new Result();
        result.ok(dto);
        return result;
    }

    @Override
    public Result<ByRegionDto> byRegion(Map<String, Object> params) {
        //查询当月情况
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);
        List<BCp03DTO> thisMonth = baseDao.getThisMonthData(params);
        //查询上月情况
        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
        String preYm = DateUtil.format(newDate, "yyyy-MM");
        params.put("ym", preYm);
        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
        //查询当年第一月的情况
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("ym", beginYm);
        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
        //定义各行业监测企业数
        Long cpCount1 = 0L;
        Long cpCount2 = 0L;
        Long cpCount3 = 0L;
        Long cpCount4 = 0L;
        Long cpCount5 = 0L;
        Long cpCount6 = 0L;
        Long cpCount7 = 0L;
        //定义上月用工人数
        Long preWorkerCount1 = 0L;
        Long preWorkerCount2 = 0L;
        Long preWorkerCount3 = 0L;
        Long preWorkerCount4 = 0L;
        Long preWorkerCount5 = 0L;
        Long preWorkerCount6 = 0L;
        Long preWorkerCount7 = 0L;
        //定义本月用工人数
        Long nowWorkerCount1 = 0L;
        Long nowWorkerCount2 = 0L;
        Long nowWorkerCount3 = 0L;
        Long nowWorkerCount4 = 0L;
        Long nowWorkerCount5 = 0L;
        Long nowWorkerCount6 = 0L;
        Long nowWorkerCount7 = 0L;
        //定义年初用工人数
        Long beginWorkerCount1 = 0L;
        Long beginWorkerCount2 = 0L;
        Long beginWorkerCount3 = 0L;
        Long beginWorkerCount4 = 0L;
        Long beginWorkerCount5 = 0L;
        Long beginWorkerCount6 = 0L;
        Long beginWorkerCount7 = 0L;
        for (BCp03DTO thisItem :
                thisMonth) {
            String thisItemAreacode = thisItem.getAreacode();
            thisItemAreacode = thisItemAreacode.substring(0, 6);
            //计算监测企业数,计算当月总工人数
            switch (thisItemAreacode) {
                case "511602"://广安区
                    cpCount1++;
                    nowWorkerCount1 += thisItem.getAllUseWorker();
                    break;
                case "511603"://前锋区
                    cpCount2++;
                    nowWorkerCount2 += thisItem.getAllUseWorker();
                    break;
                case "511621"://岳池县
                    cpCount3++;
                    nowWorkerCount3 += thisItem.getAllUseWorker();
                    break;
                case "511622"://武胜县
                    cpCount4++;
                    nowWorkerCount4 += thisItem.getAllUseWorker();
                    break;
                case "511623"://邻水县
                    cpCount5++;
                    nowWorkerCount5 += thisItem.getAllUseWorker();
                    break;
                case "511681"://华蓥市
                    cpCount6++;
                    nowWorkerCount6 += thisItem.getAllUseWorker();
                    break;
                case "511699"://暂定经开区
                    cpCount7++;
                    nowWorkerCount7 += thisItem.getAllUseWorker();
                    break;
            }


        }
        //上月循环
        for (BCp03DTO preItem :
                preMonth) {
            String preItemAreacode = preItem.getAreacode();
            preItemAreacode = preItemAreacode.substring(0, 6);
            //计算上月总工人数
            switch (preItemAreacode) {
                case "511602"://广安区
                    preWorkerCount1 += preItem.getAllUseWorker();
                    break;
                case "511603"://前锋区
                    preWorkerCount2 += preItem.getAllUseWorker();
                    break;
                case "511621"://岳池县
                    preWorkerCount3 += preItem.getAllUseWorker();
                    break;
                case "511622"://武胜县
                    preWorkerCount4 += preItem.getAllUseWorker();
                    break;
                case "511623"://邻水县
                    preWorkerCount5 += preItem.getAllUseWorker();
                    break;
                case "511681"://华蓥市
                    preWorkerCount6 += preItem.getAllUseWorker();
                    break;
                case "511699"://暂定经开区
                    preWorkerCount7 += preItem.getAllUseWorker();
                    break;
            }
        }
        //年初循环
        for (BCp03DTO beginItem :
                beginMonth) {
            String beginItemAreacode = beginItem.getAreacode();
            beginItemAreacode = beginItemAreacode.substring(0, 6);
            //计算年初总工人数
            switch (beginItemAreacode) {
                case "511602"://广安区
                    beginWorkerCount1 += beginItem.getAllUseWorker();
                    break;
                case "511603"://前锋区
                    beginWorkerCount2 += beginItem.getAllUseWorker();
                    break;
                case "511621"://岳池县
                    beginWorkerCount3 += beginItem.getAllUseWorker();
                    break;
                case "511622"://武胜县
                    beginWorkerCount4 += beginItem.getAllUseWorker();
                    break;
                case "511623"://邻水县
                    beginWorkerCount5 += beginItem.getAllUseWorker();
                    break;
                case "511681"://华蓥市
                    beginWorkerCount6 += beginItem.getAllUseWorker();
                    break;
                case "511699"://暂定经开区
                    beginWorkerCount7 += beginItem.getAllUseWorker();
                    break;
            }
        }
        //构造数据
        ByRegionDto byRegionDto1 = new ByRegionDto();
        ByRegionDto byRegionDto2 = new ByRegionDto();
        ByRegionDto byRegionDto3 = new ByRegionDto();
        ByRegionDto byRegionDto4 = new ByRegionDto();
        ByRegionDto byRegionDto5 = new ByRegionDto();
        ByRegionDto byRegionDto6 = new ByRegionDto();
        ByRegionDto byRegionDto7 = new ByRegionDto();
        ByRegionDto byRegionDto8 = new ByRegionDto();
        //园区
        byRegionDto1.setRegion("广安区");
        byRegionDto2.setRegion("前锋区");
        byRegionDto3.setRegion("岳池县");
        byRegionDto4.setRegion("武胜县");
        byRegionDto5.setRegion("邻水县");
        byRegionDto6.setRegion("华蓥市");
        byRegionDto7.setRegion("经开区");
        byRegionDto8.setRegion("总计");
        //监测企业数
        byRegionDto1.setCpCount(cpCount1);
        byRegionDto2.setCpCount(cpCount2);
        byRegionDto3.setCpCount(cpCount3);
        byRegionDto4.setCpCount(cpCount4);
        byRegionDto5.setCpCount(cpCount5);
        byRegionDto6.setCpCount(cpCount6);
        byRegionDto7.setCpCount(cpCount7);
        byRegionDto8.setCpCount(cpCount1 + cpCount2 + cpCount3 + cpCount4 + cpCount5 + cpCount6 + cpCount7);
        //当月工人数
        byRegionDto1.setNowWorkerCount(nowWorkerCount1);
        byRegionDto2.setNowWorkerCount(nowWorkerCount2);
        byRegionDto3.setNowWorkerCount(nowWorkerCount3);
        byRegionDto4.setNowWorkerCount(nowWorkerCount4);
        byRegionDto5.setNowWorkerCount(nowWorkerCount5);
        byRegionDto6.setNowWorkerCount(nowWorkerCount6);
        byRegionDto7.setNowWorkerCount(nowWorkerCount7);
        byRegionDto8.setNowWorkerCount(nowWorkerCount1 + nowWorkerCount2 + nowWorkerCount3 + nowWorkerCount4 + nowWorkerCount5 + nowWorkerCount6 + nowWorkerCount7);
        //上月工人数
        byRegionDto1.setPreWorkerCount(preWorkerCount1);
        byRegionDto2.setPreWorkerCount(preWorkerCount2);
        byRegionDto3.setPreWorkerCount(preWorkerCount3);
        byRegionDto4.setPreWorkerCount(preWorkerCount4);
        byRegionDto5.setPreWorkerCount(preWorkerCount5);
        byRegionDto6.setPreWorkerCount(preWorkerCount6);
        byRegionDto7.setPreWorkerCount(preWorkerCount7);
        byRegionDto8.setPreWorkerCount(preWorkerCount1 + preWorkerCount2 + preWorkerCount3 + preWorkerCount4 + preWorkerCount5 + preWorkerCount6 + preWorkerCount7);
        //年初工人数
        byRegionDto1.setBeginWorkerCount(beginWorkerCount1);
        byRegionDto2.setBeginWorkerCount(beginWorkerCount2);
        byRegionDto3.setBeginWorkerCount(beginWorkerCount3);
        byRegionDto4.setBeginWorkerCount(beginWorkerCount4);
        byRegionDto5.setBeginWorkerCount(beginWorkerCount5);
        byRegionDto6.setBeginWorkerCount(beginWorkerCount6);
        byRegionDto7.setBeginWorkerCount(beginWorkerCount7);
        byRegionDto8.setBeginWorkerCount(beginWorkerCount1 + beginWorkerCount2 + beginWorkerCount3 + beginWorkerCount4 + beginWorkerCount5 + beginWorkerCount6 + beginWorkerCount7);
        //比上月变化工人数
        byRegionDto1.setChangeWorkerCount(nowWorkerCount1 - preWorkerCount1);
        byRegionDto2.setChangeWorkerCount(nowWorkerCount2 - preWorkerCount2);
        byRegionDto3.setChangeWorkerCount(nowWorkerCount3 - preWorkerCount3);
        byRegionDto4.setChangeWorkerCount(nowWorkerCount4 - preWorkerCount4);
        byRegionDto5.setChangeWorkerCount(nowWorkerCount5 - preWorkerCount5);
        byRegionDto6.setChangeWorkerCount(nowWorkerCount6 - preWorkerCount6);
        byRegionDto7.setChangeWorkerCount(nowWorkerCount7 - preWorkerCount7);
        byRegionDto8.setChangeWorkerCount(byRegionDto8.getNowWorkerCount() - byRegionDto8.getPreWorkerCount());
        //比上月变化幅度
        byRegionDto1.setChangeWorkerRange(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - preWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byRegionDto2.setChangeWorkerRange(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - preWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byRegionDto3.setChangeWorkerRange(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - preWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byRegionDto4.setChangeWorkerRange(nowWorkerCount4 == 0L ? "0%" : Math.round((nowWorkerCount4 - preWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
        byRegionDto5.setChangeWorkerRange(nowWorkerCount5 == 0L ? "0%" : Math.round((nowWorkerCount5 - preWorkerCount5) * 100.00D / nowWorkerCount5) + "%");
        byRegionDto6.setChangeWorkerRange(nowWorkerCount6 == 0L ? "0%" : Math.round((nowWorkerCount6 - preWorkerCount6) * 100.00D / nowWorkerCount6) + "%");
        byRegionDto7.setChangeWorkerRange(nowWorkerCount7 == 0L ? "0%" : Math.round((nowWorkerCount7 - preWorkerCount7) * 100.00D / nowWorkerCount7) + "%");
        byRegionDto8.setChangeWorkerRange(byRegionDto8.getNowWorkerCount() == 0L ? "0%" : Math.round((byRegionDto8.getNowWorkerCount() - byRegionDto8.getPreWorkerCount()) * 100.00D / byRegionDto8.getNowWorkerCount()) + "%");
        //比年初变化工人数
        byRegionDto1.setChangeWorkerCountCompareBeginYear(nowWorkerCount1 - beginWorkerCount1);
        byRegionDto2.setChangeWorkerCountCompareBeginYear(nowWorkerCount2 - beginWorkerCount2);
        byRegionDto3.setChangeWorkerCountCompareBeginYear(nowWorkerCount3 - beginWorkerCount3);
        byRegionDto4.setChangeWorkerCountCompareBeginYear(nowWorkerCount4 - beginWorkerCount4);
        byRegionDto5.setChangeWorkerCountCompareBeginYear(nowWorkerCount5 - beginWorkerCount5);
        byRegionDto6.setChangeWorkerCountCompareBeginYear(nowWorkerCount6 - beginWorkerCount6);
        byRegionDto7.setChangeWorkerCountCompareBeginYear(nowWorkerCount7 - beginWorkerCount7);
        byRegionDto8.setChangeWorkerCountCompareBeginYear(byRegionDto8.getNowWorkerCount() - byRegionDto8.getBeginWorkerCount());
        //比年初变化幅度
        byRegionDto1.setChangeWorkerRangeCompareBeginYear(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - beginWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byRegionDto2.setChangeWorkerRangeCompareBeginYear(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - beginWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byRegionDto3.setChangeWorkerRangeCompareBeginYear(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - beginWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byRegionDto4.setChangeWorkerRangeCompareBeginYear(nowWorkerCount4 == 0L ? "0%" : Math.round((nowWorkerCount4 - beginWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
        byRegionDto5.setChangeWorkerRangeCompareBeginYear(nowWorkerCount5 == 0L ? "0%" : Math.round((nowWorkerCount5 - beginWorkerCount5) * 100.00D / nowWorkerCount5) + "%");
        byRegionDto6.setChangeWorkerRangeCompareBeginYear(nowWorkerCount6 == 0L ? "0%" : Math.round((nowWorkerCount6 - beginWorkerCount6) * 100.00D / nowWorkerCount6) + "%");
        byRegionDto7.setChangeWorkerRangeCompareBeginYear(nowWorkerCount7 == 0L ? "0%" : Math.round((nowWorkerCount7 - beginWorkerCount7) * 100.00D / nowWorkerCount7) + "%");
        byRegionDto8.setChangeWorkerRangeCompareBeginYear(byRegionDto8.getNowWorkerCount() == 0L ? "0%" : Math.round((byRegionDto8.getNowWorkerCount() - byRegionDto8.getBeginWorkerCount()) * 100.00D / byRegionDto8.getNowWorkerCount()) + "%");
        List<ByRegionDto> list = new ArrayList<>();
        list.add(byRegionDto1);
        list.add(byRegionDto2);
        list.add(byRegionDto3);
        list.add(byRegionDto4);
        list.add(byRegionDto5);
        list.add(byRegionDto6);
        list.add(byRegionDto7);
        list.add(byRegionDto8);
        ByRegionDto dto = new ByRegionDto();
        dto.setList(list);
        Result<ByRegionDto> result = new Result<>();
        result.ok(dto);
        return result;
    }

    @Override
    public BCp03DTO getInfo(Long id) {
        return baseDao.getInfo(id);
    }

    @Override
    public void exportGeneralSituation(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("generalSituation");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<GeneralSituationDto> generalSituationDtoResult = generalSituation(params);
        List<GeneralSituationDto> list = generalSituationDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "总体情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByScale(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byScale");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<ByScaleDto> byScaleDtoResult = byScale(params);
        List<ByScaleDto> list = byScaleDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "企业规模情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByIndustry(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byIndustry");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<ByIndustryDto> byIndustryDtoResult = byIndustry(params);
        List<ByIndustryDto> list = byIndustryDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "行业规模情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByRegion(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byRegion");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<ByRegionDto> byRegionDtoResult = byRegion(params);
        List<ByRegionDto> list = byRegionDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "地区情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByEstate(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byEstate");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<ByEstateDto> byEstateDtoResult = byEstate(params);
        List<ByEstateDto> list = byEstateDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "产业情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByLack(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byLack");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<LackWorkerDto> lackWorkerDtoResult = byLack(params);
        List<LackWorkerDto> list = lackWorkerDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "缺工企业情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByYearMonth(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byYearMonth");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<ByYearMonthDto> byYearMonthDtoResult = byYearMonth(params);
        List<ByYearMonthDto> list = byYearMonthDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "用工年月情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportByPark(Map<String, Object> params, HttpServletResponse response) {
        String url = jsonObject.getString("byPark");
        //模板文件
        TemplateExportParams exportParams = new TemplateExportParams(url);
        //data数据
        Result<ByParkDto> byParkDtoResult = byPark(params);
        List<ByParkDto> list = byParkDtoResult.getData().getList();
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("yearmonth", ym);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, data);
        String exportFileName = "园区情况—" + DateUtil.now();
        try {
            ExcelUtils.export(response, workbook, exportFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result<OverallAnalysisDTO> overallAnalysis(Map<String, Object> params) {
        OverallAnalysisDTO resultDTO = new OverallAnalysisDTO();
        //总体情况
        Result<GeneralSituationDto> generalSituationDtoResult = generalSituation(params);
        GeneralSituationDto data1 = generalSituationDtoResult.getData();
        List<GeneralSituationDto> list1 = data1.getList();
        GeneralSituationDto item4_1 = list1.get(3);//总计行
        GeneralSituationDto item2_1 = list1.get(1);//减员行
        GeneralSituationDto item3_1 = list1.get(2);//增员行
        resultDTO.setAllCpCountThisMonth_1(data1.getAllCpCount());
        resultDTO.setAllWorkerCountThisMonth_1(data1.getAllWorkerCount());
        String changeWorkerCount = item4_1.getChangeWorkerCount().toString();
        int i = changeWorkerCount.indexOf("-");
        if(i==-1){
            changeWorkerCount = "增加"+changeWorkerCount;
        }else{
            changeWorkerCount = "减少"+changeWorkerCount;
        }
        resultDTO.setDifference_1(changeWorkerCount);
        resultDTO.setDifferenceRange_1(Math.round(item4_1.getChangeWorkerCount()* 100.00D/data1.getAllWorkerCount())+"%");
        resultDTO.setAddWorkerCpCount_1(item3_1.getChangeWorkerCpCount());
        resultDTO.setAddWorkerCpRange_1(Math.round(item3_1.getChangeWorkerCpCount() *100.00D/data1.getAllCpCount())+"%");
        resultDTO.setAddWorkerCount_1(item3_1.getChangeWorkerCount());
        resultDTO.setAddWorkerRange_1(item3_1.getChangeWorkerRange());
        resultDTO.setReduceWorkerCpCount_1(item2_1.getChangeWorkerCpCount());
        resultDTO.setReduceWorkerCpRange_1(Math.round(item2_1.getChangeWorkerCpCount() *100.00D/data1.getAllCpCount())+"%");
        resultDTO.setReduceWorkerCount_1(item2_1.getChangeWorkerCount());
        resultDTO.setReduceWorkerRange_1(item2_1.getChangeWorkerRange());
        //产业情况
        Result<ByEstateDto> byEstateDtoResult = byEstate(params);
        ByEstateDto data = byEstateDtoResult.getData();
        List<ByEstateDto> list2 = data.getList();
        ByEstateDto byEstateDto1 = list2.get(0);//第一产业行
        ByEstateDto byEstateDto2 = list2.get(1);//第二产业行
        ByEstateDto byEstateDto3 = list2.get(2);//第三产业行
        ByEstateDto byEstateDto4 = list2.get(3);//产业总计
        resultDTO.setWorkerCount_2_1(byEstateDto1.getNowWorkerCount().toString());
        resultDTO.setWorkerRange_2_1(Math.round(byEstateDto1.getNowWorkerCount() *100.00D /byEstateDto4.getNowWorkerCount())+"%");
        String workerDifference_2_1 = byEstateDto1.getChangeWorkerCount().toString();
        workerDifference_2_1 = workerDifference_2_1.indexOf("-")==-1?"增加"+workerDifference_2_1:"减少"+workerDifference_2_1.replace("-","");
        resultDTO.setWorkerDifference_2_1(workerDifference_2_1);
        resultDTO.setWorkerDifferenceRange_2_1(byEstateDto1.getChangeWorkerRange());
        String workerDifferenceBeginYear_2_1 = byEstateDto1.getChangeWorkerCountCompareBeginYear().toString();
        workerDifferenceBeginYear_2_1 = workerDifferenceBeginYear_2_1.indexOf("-")==-1?"增加"+workerDifferenceBeginYear_2_1:"减少"+workerDifferenceBeginYear_2_1.replace("-","");
        resultDTO.setWorkerDifferenceBeginYear_2_1(workerDifferenceBeginYear_2_1);
        resultDTO.setWorkerDifferenceRangeBeginYear_2_1(byEstateDto1.getChangeWorkerRangeCompareBeginYear());
        resultDTO.setWorkerCount_2_2(byEstateDto2.getNowWorkerCount().toString());
        resultDTO.setWorkerRange_2_2(Math.round(byEstateDto2.getNowWorkerCount() *100.00D/byEstateDto4.getNowWorkerCount())+"%");
        String workerDifference_2_2 = byEstateDto2.getChangeWorkerCount().toString();
        workerDifference_2_2 = workerDifference_2_2.indexOf("-")==-1?"增加"+workerDifference_2_2:"减少"+workerDifference_2_2.replace("-","");
        resultDTO.setWorkerDifference_2_2(workerDifference_2_2);
        resultDTO.setWorkerDifferenceRange_2_2(byEstateDto2.getChangeWorkerRange());
        String workerDifferenceBeginYear_2_2 = byEstateDto2.getChangeWorkerCountCompareBeginYear().toString();
        workerDifferenceBeginYear_2_2 = workerDifferenceBeginYear_2_2.indexOf("-")==-1?"增加"+workerDifferenceBeginYear_2_2:"减少"+workerDifferenceBeginYear_2_2.replace("-","");
        resultDTO.setWorkerDifferenceBeginYear_2_2(workerDifferenceBeginYear_2_2);
        resultDTO.setWorkerDifferenceRangeBeginYear_2_2(byEstateDto2.getChangeWorkerRangeCompareBeginYear());
        resultDTO.setWorkerCount_2_3(byEstateDto3.getNowWorkerCount().toString());
        resultDTO.setWorkerRange_2_3(Math.round(byEstateDto3.getNowWorkerCount() *100.00D/byEstateDto4.getNowWorkerCount())+"%");
        String workerDifference_2_3 = byEstateDto3.getChangeWorkerCount().toString();
        workerDifference_2_3 = workerDifference_2_3.indexOf("-")==-1?"增加"+workerDifference_2_3:"减少"+workerDifference_2_3.replace("-","");
        resultDTO.setWorkerDifference_2_3(workerDifference_2_3);
        resultDTO.setWorkerDifferenceRange_2_3(byEstateDto3.getChangeWorkerRange());
        String workerDifferenceBeginYear_2_3 = byEstateDto3.getChangeWorkerCountCompareBeginYear().toString();
        workerDifferenceBeginYear_2_3 = workerDifferenceBeginYear_2_3.indexOf("-")==-1?"增加"+workerDifferenceBeginYear_2_3:"减少"+workerDifferenceBeginYear_2_3.replace("-","");
        resultDTO.setWorkerDifferenceBeginYear_2_3(workerDifferenceBeginYear_2_3);
        resultDTO.setWorkerDifferenceRangeBeginYear_2_3(byEstateDto3.getChangeWorkerRangeCompareBeginYear());
        Result<OverallAnalysisDTO> result = new Result();
        //行业情况
        Result<ByIndustryDto> byIndustryDtoResult = byIndustry(params);
        ByIndustryDto data3 = byIndustryDtoResult.getData();
        List<ByIndustryDto> list = data3.getList();
        for (ByIndustryDto item :
                list) {
            if("总计".equals(item.getIndustry())){
                list.remove(item);
                break;
            }
        }
        Collections.sort(list, new Comparator<ByIndustryDto>() {
            @Override
            public int compare(ByIndustryDto o1, ByIndustryDto o2) {
                if(o1 instanceof ByIndustryDto && o2 instanceof ByIndustryDto){
                    ByIndustryDto dto1 = (ByIndustryDto)o1;
                    ByIndustryDto dto2 = (ByIndustryDto)o2;
                    return Integer.valueOf(o1.getChangeWorkerCount().toString())-Integer.valueOf(o2.getChangeWorkerCount().toString());
                }
                return 0;
            }
        });
        //构造增加和减少前五
        String reduceWorkerCountTop_3 = "";
        String addWorkerCountTop_3 = "";
        String reduceWorkerRangeTop_3 = "";
        String addWorkerRangeTop_3 = "";
        for(int i1=0;i1<5;i1++){
            ByIndustryDto byIndustryDto = list.get(i1);
            Long changeWorkerCount1 = byIndustryDto.getChangeWorkerCount();
            if(changeWorkerCount1>=0){
                reduceWorkerCountTop_3="".equals(reduceWorkerCountTop_3)?"无":reduceWorkerCountTop_3;
                break;
            }
            reduceWorkerCountTop_3+=byIndustryDto.getIndustry()+changeWorkerCount1+"人，";
        }
        for(int i1=list.size()-1;i1>=list.size()-5;i1--){
            ByIndustryDto byIndustryDto = list.get(i1);
            Long changeWorkerCount1 = byIndustryDto.getChangeWorkerCount();
            if(changeWorkerCount1<=0){
                addWorkerCountTop_3="".equals(addWorkerCountTop_3)?"无":addWorkerCountTop_3;
                break;
            }
            addWorkerCountTop_3+=byIndustryDto.getIndustry()+changeWorkerCount1+"人，";
        }
        resultDTO.setAddWorkerCountTop_3(addWorkerCountTop_3);
        resultDTO.setReduceWorkerCountTop_3(reduceWorkerCountTop_3);
        Collections.sort(list, new Comparator<ByIndustryDto>() {
            @Override
            public int compare(ByIndustryDto o1, ByIndustryDto o2) {
                if(o1 instanceof ByIndustryDto && o2 instanceof ByIndustryDto){
                    ByIndustryDto dto1 = (ByIndustryDto)o1;
                    ByIndustryDto dto2 = (ByIndustryDto)o2;
                    return Integer.valueOf(o1.getChangeWorkerRange().toString().replace("%",""))-Integer.valueOf(o2.getChangeWorkerRange().toString().replace("%",""));
                }
                return 0;
            }
        });
        for(int i1=0;i1<5;i1++){
            ByIndustryDto byIndustryDto = list.get(i1);
            String changeWorkerRange1 = byIndustryDto.getChangeWorkerRange();
            if(changeWorkerRange1.indexOf("-")==-1){
                reduceWorkerRangeTop_3="".equals(reduceWorkerRangeTop_3)?"无":reduceWorkerRangeTop_3;
                break;
            }
            reduceWorkerRangeTop_3+=byIndustryDto.getIndustry()+byIndustryDto.getChangeWorkerRange()+"，";
        }
        for(int i1=list.size()-1;i1>=list.size()-5;i1--){
            ByIndustryDto byIndustryDto = list.get(i1);
            String changeWorkerRange2 = byIndustryDto.getChangeWorkerRange();
            if(changeWorkerRange2.indexOf("-")!=-1){
                addWorkerRangeTop_3="".equals(addWorkerRangeTop_3)?"无":addWorkerRangeTop_3;
                break;
            }
            addWorkerRangeTop_3+=byIndustryDto.getIndustry()+byIndustryDto.getChangeWorkerRange()+"，";
        }
        resultDTO.setAddWorkerRangeTop_3(addWorkerRangeTop_3);
        resultDTO.setReduceWorkerRangeTop_3(reduceWorkerRangeTop_3);
        //地区情况
        Result<ByRegionDto> result1 = byRegion(params);
        ByRegionDto data4 = result1.getData();
        List<ByRegionDto> list4 = data4.getList();
        ByIndustryDto byIndustryDto7 = list.get(7);//总计行
        list4.remove(7);
        Long noChangeCount_4 = 0L;
        Long addChangeCount_4 = 0L;
        Long reduceChangeCount_4 = 0L;
        Double addChangeRange_4 = 0D;
        Double reduceChangeRange_4 = 0D;
        Long noChangeCountBeginYear_4 = 0L;
        Long addChangeCountBeginYear_4 = 0L;
        Long reduceChangeCountBeginYear_4 = 0L;
        Double addChangeRangeBeginYear_4 = 0D;
        Double reduceChangeRangeBeginYear_4 = 0D;
        for (ByRegionDto item :
                list4) {
            if("0".equals(item.getChangeWorkerCount().toString())){
                noChangeCount_4++;
            }else if(item.getChangeWorkerCount()>0){
                addChangeCount_4++;
                addChangeRange_4+=Double.valueOf(item.getChangeWorkerRange().replace("%",""));
                addChangeRange_4 = addChangeRange_4/2;
            }else{
                reduceChangeCount_4++;
                reduceChangeRange_4+=Double.valueOf(item.getChangeWorkerRange().replace("%",""));
                reduceChangeRange_4 = reduceChangeRange_4/2;
            }
            if("0".equals(item.getChangeWorkerCountCompareBeginYear().toString())){
                noChangeCountBeginYear_4++;

            }else if(item.getChangeWorkerCountCompareBeginYear()>0){
                addChangeCountBeginYear_4++;
                addChangeRangeBeginYear_4+=Double.valueOf(item.getChangeWorkerRangeCompareBeginYear().replace("%",""));
                addChangeRangeBeginYear_4 = addChangeRangeBeginYear_4/2;
            }else{
                reduceChangeCountBeginYear_4++;
                reduceChangeRangeBeginYear_4+=Double.valueOf(item.getChangeWorkerRangeCompareBeginYear().replace("%",""));
                reduceChangeRangeBeginYear_4 = reduceChangeRangeBeginYear_4/2;
            }
        }
        resultDTO.setNoChangeCount_4(noChangeCount_4.toString());
        resultDTO.setAddChangeCount_4(addChangeCount_4.toString());
        resultDTO.setAddChangeRange_4(addChangeRange_4+"%");
        resultDTO.setReduceChangeCount_4(reduceChangeCount_4.toString());
        resultDTO.setReduceChangeRange_4(reduceChangeRange_4+"%");
        resultDTO.setNoChangeCountBeginYear_4(noChangeCountBeginYear_4.toString());
        resultDTO.setAddChangeCountBeginYear_4(addChangeCountBeginYear_4.toString());
        resultDTO.setAddChangeRangeBeginYear_4(addChangeRangeBeginYear_4+"%");
        resultDTO.setReduceChangeCountBeginYear_4(reduceChangeCountBeginYear_4.toString());
        resultDTO.setReduceChangeRangeBeginYear_4(reduceChangeRangeBeginYear_4+"%");
        //园区情况
        Result<ByParkDto> result5 = byPark(params);
        ByParkDto data5 = result5.getData();
        List<ByParkDto> list5 = data5.getList();
        Long noChangeCount_5 = 0L;
        Long addChangeCount_5 = 0L;
        Long reduceChangeCount_5 = 0L;
        Double addChangeRange_5 = 0D;
        Double reduceChangeRange_5 = 0D;
        Long noChangeCountBeginYear_5 = 0L;
        Long addChangeCountBeginYear_5 = 0L;
        Long reduceChangeCountBeginYear_5 = 0L;
        Double addChangeRangeBeginYear_5 = 0D;
        Double reduceChangeRangeBeginYear_5 = 0D;
        for (ByParkDto item :
                list5) {
            if("0".equals(item.getChangeWorkerCount().toString())){
                noChangeCount_5++;
            }else if(item.getChangeWorkerCount()>0){
                addChangeCount_5++;
                addChangeRange_5+=Double.valueOf(item.getChangeWorkerRange().replace("%",""));
                addChangeRange_5 = addChangeRange_5/2;
            }else{
                reduceChangeCount_5++;
                reduceChangeRange_5+=Double.valueOf(item.getChangeWorkerRange().replace("%",""));
                reduceChangeRange_5 = reduceChangeRange_5/2;
            }
            if("0".equals(item.getChangeWorkerCountCompareBeginYear().toString())){
                noChangeCountBeginYear_5++;

            }else if(item.getChangeWorkerCountCompareBeginYear()>0){
                addChangeCountBeginYear_5++;
                addChangeRangeBeginYear_5+=Double.valueOf(item.getChangeWorkerRangeCompareBeginYear().replace("%",""));
                addChangeRangeBeginYear_5 = addChangeRangeBeginYear_5/2;
            }else{
                reduceChangeCountBeginYear_5++;
                reduceChangeRangeBeginYear_5+=Double.valueOf(item.getChangeWorkerRangeCompareBeginYear().replace("%",""));
                reduceChangeRangeBeginYear_5 = reduceChangeRangeBeginYear_5/2;
            }
        }
        resultDTO.setNoChangeCount_5(noChangeCount_5.toString());
        resultDTO.setAddChangeCount_5(addChangeCount_5.toString());
        resultDTO.setAddChangeRange_5(addChangeRange_5+"%");
        resultDTO.setReduceChangeCount_5(reduceChangeCount_5.toString());
        resultDTO.setReduceChangeRange_5(reduceChangeRange_5+"%");
        resultDTO.setNoChangeCountBeginYear_5(noChangeCountBeginYear_5.toString());
        resultDTO.setAddChangeCountBeginYear_5(addChangeCountBeginYear_5.toString());
        resultDTO.setAddChangeRangeBeginYear_5(addChangeRangeBeginYear_5+"%");
        resultDTO.setReduceChangeCountBeginYear_5(reduceChangeCountBeginYear_5.toString());
        resultDTO.setReduceChangeRangeBeginYear_5(reduceChangeRangeBeginYear_5+"%");
        //企业规模情况
        Result<ByScaleDto> byScaleDtoResult = byScale(params);
        ByScaleDto data6 = byScaleDtoResult.getData();
        List<ByScaleDto> list6 = data6.getList();
        ByScaleDto byScaleDto1 = list6.get(0);//大型
        ByScaleDto byScaleDto2 = list6.get(1);//中型
        ByScaleDto byScaleDto3 = list6.get(2);//小型
        ByScaleDto byScaleDto4 = list6.get(3);//微型
        resultDTO.setWorkerCpCount6_1(byScaleDto1.getCpCount().toString());
        resultDTO.setWorkerCount6_1(byScaleDto1.getNowWorkerCount().toString());
        resultDTO.setChangeWorkerCount6_1(byScaleDto1.getChangeWorkerCount().toString());
        resultDTO.setChangeWorkerRange6_1(byScaleDto1.getChangeWorkerRange());
        resultDTO.setChangeWorkerCountBeginYear6_1(byScaleDto1.getChangeWorkerCountCompareBeginYear().toString());
        resultDTO.setChangeWorkerRangeBeginYear6_1(byScaleDto1.getChangeWorkerRangeCompareBeginYear());

        resultDTO.setWorkerCpCount6_2(byScaleDto2.getCpCount().toString());
        resultDTO.setWorkerCount6_2(byScaleDto2.getNowWorkerCount().toString());
        resultDTO.setChangeWorkerCount6_2(byScaleDto2.getChangeWorkerCount().toString());
        resultDTO.setChangeWorkerRange6_2(byScaleDto2.getChangeWorkerRange());
        resultDTO.setChangeWorkerCountBeginYear6_2(byScaleDto2.getChangeWorkerCountCompareBeginYear().toString());
        resultDTO.setChangeWorkerRangeBeginYear6_2(byScaleDto2.getChangeWorkerRangeCompareBeginYear());

        resultDTO.setWorkerCpCount6_3(byScaleDto3.getCpCount().toString());
        resultDTO.setWorkerCount6_3(byScaleDto3.getNowWorkerCount().toString());
        resultDTO.setChangeWorkerCount6_3(byScaleDto3.getChangeWorkerCount().toString());
        resultDTO.setChangeWorkerRange6_3(byScaleDto3.getChangeWorkerRange());
        resultDTO.setChangeWorkerCountBeginYear6_3(byScaleDto3.getChangeWorkerCountCompareBeginYear().toString());
        resultDTO.setChangeWorkerRangeBeginYear6_3(byScaleDto3.getChangeWorkerRangeCompareBeginYear());

        resultDTO.setWorkerCpCount6_4(byScaleDto4.getCpCount().toString());
        resultDTO.setWorkerCount6_4(byScaleDto4.getNowWorkerCount().toString());
        resultDTO.setChangeWorkerCount6_4(byScaleDto4.getChangeWorkerCount().toString());
        resultDTO.setChangeWorkerRange6_4(byScaleDto4.getChangeWorkerRange());
        resultDTO.setChangeWorkerCountBeginYear6_4(byScaleDto4.getChangeWorkerCountCompareBeginYear().toString());
        resultDTO.setChangeWorkerRangeBeginYear6_4(byScaleDto4.getChangeWorkerRangeCompareBeginYear());

        //缺工情况
        Result<LackWorkerDto> result7 = byLack(params);
        LackWorkerDto data7 = result7.getData();
        List<LackWorkerDto> list7 = data7.getList();
        LackWorkerDto lackWorkerDto4 = list7.get(3);//总计行
        resultDTO.setNowLackCpCount_7(lackWorkerDto4.getNowLackCpCount().toString());
        resultDTO.setNowLackWorkerCount_7(lackWorkerDto4.getNowLackWorkerCount().toString());
//        resultDTO.setPreLackCpCount_7(lackWorkerDto4.getPreLackCpCount());
//        resultDTO.setPreLackWorkerCount_7(lackWorkerDto4.getPreLackWorkerCount());
        resultDTO.setDifferenceLackCpCount_7(lackWorkerDto4.getNowLackCpCount()-lackWorkerDto4.getPreLackCpCount());
        resultDTO.setDifferenceLackWorkerCount_7(lackWorkerDto4.getNowLackWorkerCount()-lackWorkerDto4.getPreLackWorkerCount());
        resultDTO.setDifferenceLackCpCountBeginYear_7(lackWorkerDto4.getNowLackCpCount()-lackWorkerDto4.getBeginLackCpCount());
        resultDTO.setDifferenceLackWorkerCountBeginYear_7(lackWorkerDto4.getNowLackWorkerCount()-lackWorkerDto4.getBeginLackWorkerCount());
        Long lackByRegion_7 = 0L;
        Long lackByIndustry_7 = 0L;
        for (LackWorkerDto item
                :list7
             ) {
             if("按行业".equals(item.getCategory1()) && item.getNowLackCpCount()>0){
                 lackByIndustry_7++;
            }else if("按地区".equals(item.getCategory1()) && item.getNowLackCpCount()>0){
                 lackByRegion_7++;
            }
        }
        //统计缺工地区
        resultDTO.setLackByRegion_7(lackByRegion_7);
        //统计缺工行业
        resultDTO.setLackByIndustry_7(lackByIndustry_7);
        result.ok(resultDTO);
        return result;
    }

    //首页扇形图——各区县企业数量及占比
    @Override
    public Result<List> homePageByRegion(Map<String, Object> params) {
        Long deptId = SecurityUser.getUser().getDeptId();
        params.put("deptId",deptId);
        List<HomePageByRegionDTO> list = baseDao.homePageByRegion(params);
        return new Result<List>().ok(list);
    }

    @Override
    public Long hasData(Long deptId) {
        BCp01Entity bCp01Entity = cp01Dao.selectOne(new QueryWrapper<BCp01Entity>().eq("DEPT_ID", deptId));
        String yearMonth = DateUtil.format(DateUtil.date(), "yyyy-MM");
        return baseDao.hasData(yearMonth,bCp01Entity.getCp0101());
    }

    @Override
    public Result<List> homePageByEstate(Map<String, Object> params) {
        Long deptId = SecurityUser.getUser().getDeptId();
        params.put("deptId",deptId);
        List<HomePageByRegionDTO> list = baseDao.homePageByEstate(params);
        return new Result<List>().ok(list);
    }

    @Override
    public Result<HomePageByYearDTO> homePageByYear(Map<String, Object> params) {
        Long deptId = SecurityUser.getUser().getDeptId();
        params.put("deptId",deptId);
        params.put("year",DateUtil.format(DateUtil.date(),"yyyy"));
        List<HomePageByRegionDTO> list = baseDao.homePageByYear(params);
        List<String> xAxis = new ArrayList<>();
        List<String> series = new ArrayList<>();
        for (HomePageByRegionDTO item :
                list) {
            xAxis.add(item.getName());
            series.add(item.getValue());
        }
        HomePageByYearDTO homePageByYearDTO = new HomePageByYearDTO();
        homePageByYearDTO.setXAxis(xAxis);
        homePageByYearDTO.setSeries(series);
        return new Result<HomePageByYearDTO>().ok(homePageByYearDTO);
    }

    //企业规模
    @Override
    public Result<ByScaleDto> byScale(Map<String, Object> params) {
        //查询当月情况
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);
        List<BCp03DTO> thisMonth = baseDao.getThisMonthData(params);
        //查询上月情况
        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
        String preYm = DateUtil.format(newDate, "yyyy-MM");
        params.put("ym", preYm);
        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
        //查询当年第一月的情况
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("ym", beginYm);
        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
        //定义各行业监测企业数
        Long cpCount1 = 0L;
        Long cpCount2 = 0L;
        Long cpCount3 = 0L;
        Long cpCount4 = 0L;
        //定义上月用工人数
        Long preWorkerCount1 = 0L;
        Long preWorkerCount2 = 0L;
        Long preWorkerCount3 = 0L;
        Long preWorkerCount4 = 0L;
        //定义本月用工人数
        Long nowWorkerCount1 = 0L;
        Long nowWorkerCount2 = 0L;
        Long nowWorkerCount3 = 0L;
        Long nowWorkerCount4 = 0L;
        //定义年初用工人数
        Long beginWorkerCount1 = 0L;
        Long beginWorkerCount2 = 0L;
        Long beginWorkerCount3 = 0L;
        Long beginWorkerCount4 = 0L;
        for (BCp03DTO thisItem :
                thisMonth) {
            String thisItemScale = thisItem.getScale();
            switch (thisItemScale) {
                case "1":
                    cpCount1++;
                    nowWorkerCount1 += thisItem.getAllUseWorker();
                    break;
                case "2":
                    cpCount2++;
                    nowWorkerCount2 += thisItem.getAllUseWorker();
                    break;
                case "3":
                    cpCount3++;
                    nowWorkerCount3 += thisItem.getAllUseWorker();
                    break;
                case "4":
                    cpCount4++;
                    nowWorkerCount4 += thisItem.getAllUseWorker();
                    break;
            }
        }
        //上月循环
        for (BCp03DTO preItem :
                preMonth) {
            String preItemScale = preItem.getScale();
            //计算上月总工人数
            switch (preItemScale) {
                case "1":
                    preWorkerCount1 += preItem.getAllUseWorker();
                    break;
                case "2":
                    preWorkerCount2 += preItem.getAllUseWorker();
                    break;
                case "3":
                    preWorkerCount3 += preItem.getAllUseWorker();
                    break;
                case "4":
                    preWorkerCount4 += preItem.getAllUseWorker();
                    break;
            }
        }
        //年初循环
        for (BCp03DTO beginItem :
                beginMonth) {
            String beginItemScale = beginItem.getScale();
            //计算年初总工人数
            switch (beginItemScale) {
                case "1":
                    beginWorkerCount1 += beginItem.getAllUseWorker();
                    break;
                case "2":
                    beginWorkerCount2 += beginItem.getAllUseWorker();
                    break;
                case "3":
                    beginWorkerCount3 += beginItem.getAllUseWorker();
                    break;
                case "4":
                    beginWorkerCount4 += beginItem.getAllUseWorker();
                    break;
            }
        }
        //构造数据
        ByScaleDto byScaleDto1 = new ByScaleDto();
        ByScaleDto byScaleDto2 = new ByScaleDto();
        ByScaleDto byScaleDto3 = new ByScaleDto();
        ByScaleDto byScaleDto4 = new ByScaleDto();
        ByScaleDto byScaleDto5 = new ByScaleDto();
        byScaleDto1.setScale("大型企业");
        byScaleDto2.setScale("中型企业");
        byScaleDto3.setScale("小型企业");
        byScaleDto4.setScale("微型企业");
        byScaleDto5.setScale("总计");
        byScaleDto1.setCpCount(cpCount1);
        byScaleDto2.setCpCount(cpCount2);
        byScaleDto3.setCpCount(cpCount3);
        byScaleDto4.setCpCount(cpCount4);
        byScaleDto5.setCpCount(cpCount1 + cpCount2 + cpCount3 + cpCount4);
        byScaleDto1.setNowWorkerCount(nowWorkerCount1);
        byScaleDto2.setNowWorkerCount(nowWorkerCount2);
        byScaleDto3.setNowWorkerCount(nowWorkerCount3);
        byScaleDto4.setNowWorkerCount(nowWorkerCount4);
        byScaleDto5.setNowWorkerCount(nowWorkerCount1 + nowWorkerCount2 + nowWorkerCount3 + nowWorkerCount4);
        byScaleDto1.setPreWorkerCount(preWorkerCount1);
        byScaleDto2.setPreWorkerCount(preWorkerCount2);
        byScaleDto3.setPreWorkerCount(preWorkerCount3);
        byScaleDto4.setPreWorkerCount(preWorkerCount4);
        byScaleDto5.setPreWorkerCount(preWorkerCount1 + preWorkerCount2 + preWorkerCount3 + preWorkerCount4);
        byScaleDto1.setChangeWorkerCount(nowWorkerCount1 - preWorkerCount1);
        byScaleDto2.setChangeWorkerCount(nowWorkerCount2 - preWorkerCount2);
        byScaleDto3.setChangeWorkerCount(nowWorkerCount3 - preWorkerCount3);
        byScaleDto4.setChangeWorkerCount(nowWorkerCount4 - preWorkerCount4);
        byScaleDto5.setChangeWorkerCount(byScaleDto5.getNowWorkerCount() - byScaleDto5.getPreWorkerCount());
        byScaleDto1.setChangeWorkerRange(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - preWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byScaleDto2.setChangeWorkerRange(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - preWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byScaleDto3.setChangeWorkerRange(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - preWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byScaleDto4.setChangeWorkerRange(nowWorkerCount4 == 0L ? "0%" : Math.round((nowWorkerCount4 - preWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
        byScaleDto5.setChangeWorkerRange(byScaleDto5.getNowWorkerCount() == 0L ? "0%" : Math.round((Math.round(byScaleDto5.getNowWorkerCount() - byScaleDto5.getPreWorkerCount()) * 100.00D / byScaleDto5.getNowWorkerCount())) + "%");
        byScaleDto1.setChangeWorkerCountCompareBeginYear(nowWorkerCount1 - beginWorkerCount1);
        byScaleDto2.setChangeWorkerCountCompareBeginYear(nowWorkerCount2 - beginWorkerCount2);
        byScaleDto3.setChangeWorkerCountCompareBeginYear(nowWorkerCount3 - beginWorkerCount3);
        byScaleDto4.setChangeWorkerCountCompareBeginYear(nowWorkerCount4 - beginWorkerCount4);
        //年初人数
        byScaleDto5.setBeginWorkerCount(beginWorkerCount1 + beginWorkerCount2 + beginWorkerCount3 + beginWorkerCount4);
        byScaleDto5.setChangeWorkerCountCompareBeginYear(byScaleDto5.getNowWorkerCount() - byScaleDto5.getBeginWorkerCount());
        byScaleDto1.setChangeWorkerRangeCompareBeginYear(nowWorkerCount1 == 0L ? "0%" : Math.round((nowWorkerCount1 - beginWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byScaleDto2.setChangeWorkerRangeCompareBeginYear(nowWorkerCount2 == 0L ? "0%" : Math.round((nowWorkerCount2 - beginWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byScaleDto3.setChangeWorkerRangeCompareBeginYear(nowWorkerCount3 == 0L ? "0%" : Math.round((nowWorkerCount3 - beginWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byScaleDto4.setChangeWorkerRangeCompareBeginYear(nowWorkerCount4 == 0L ? "0%" : Math.round((nowWorkerCount4 - beginWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
        byScaleDto5.setChangeWorkerRangeCompareBeginYear(byScaleDto5.getNowWorkerCount() == 0L ? "0%" : Math.round((byScaleDto5.getNowWorkerCount() - byScaleDto5.getBeginWorkerCount()) * 100.00D / byScaleDto5.getNowWorkerCount()) + "%");
        List<ByScaleDto> list = new ArrayList<>();
        list.add(byScaleDto1);
        list.add(byScaleDto2);
        list.add(byScaleDto3);
        list.add(byScaleDto4);
        list.add(byScaleDto5);
        ByScaleDto dto = new ByScaleDto();
        dto.setList(list);
        Result<ByScaleDto> result = new Result();
        result.ok(dto);
        return result;
    }

    @Override
    public Result<LackWorkerDto> byLack(Map<String, Object> params) {
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        //查询当月情况
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);
        List<BCp03DTO> thisMonth = baseDao.getLackWorkerData(params);
        //查询上月情况
        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
        String preYm = DateUtil.format(newDate, "yyyy-MM");
        params.put("ym", preYm);
        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
        //查询当年第一月的情况
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("ym", beginYm);
        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
        //定义按产业监测企业数
        Long cpCountByEstate1 = 0L;
        Long cpCountByEstate2 = 0L;
        Long cpCountByEstate3 = 0L;
        Long cpCountByEstate4 = 0L;
        //定义按产业本月缺工企业数
        Long nowLackCpCountByEstate1 = 0L;
        Long nowLackCpCountByEstate2 = 0L;
        Long nowLackCpCountByEstate3 = 0L;
        Long nowLackCpCountByEstate4 = 0L;
        //定义按产业本月缺工人数
        Long nowLackWorkerCountByEstate1 = 0L;
        Long nowLackWorkerCountByEstate2 = 0L;
        Long nowLackWorkerCountByEstate3 = 0L;
        Long nowLackWorkerCountByEstate4 = 0L;
        //定义按产业上月缺工企业数
        Long preLackCpCountByEstate1 = 0L;
        Long preLackCpCountByEstate2 = 0L;
        Long preLackCpCountByEstate3 = 0L;
        Long preLackCpCountByEstate4 = 0L;
        //定义按产业上月缺工人数
        Long preLackWorkerCountByEstate1 = 0L;
        Long preLackWorkerCountByEstate2 = 0L;
        Long preLackWorkerCountByEstate3 = 0L;
        Long preLackWorkerCountByEstate4 = 0L;
        //定义按产业年初缺工企业数
        Long beginLackCpCountByEstate1 = 0L;
        Long beginLackCpCountByEstate2 = 0L;
        Long beginLackCpCountByEstate3 = 0L;
        Long beginLackCpCountByEstate4 = 0L;
        //定义按产业年初缺工人数
        Long beginLackWorkerCountByEstate1 = 0L;
        Long beginLackWorkerCountByEstate2 = 0L;
        Long beginLackWorkerCountByEstate3 = 0L;
        Long beginLackWorkerCountByEstate4 = 0L;
        //定义按行业监测企业数
        Long cpCountByIndustry1 = 0L;
        Long cpCountByIndustry2 = 0L;
        Long cpCountByIndustry3 = 0L;
        Long cpCountByIndustry4 = 0L;
        Long cpCountByIndustry5 = 0L;
        Long cpCountByIndustry6 = 0L;
        Long cpCountByIndustry7 = 0L;
        Long cpCountByIndustry8 = 0L;
        Long cpCountByIndustry9 = 0L;
        Long cpCountByIndustry10 = 0L;
        Long cpCountByIndustry11 = 0L;
        Long cpCountByIndustry12 = 0L;
        Long cpCountByIndustry13 = 0L;
        Long cpCountByIndustry14 = 0L;
        Long cpCountByIndustry15 = 0L;
        Long cpCountByIndustry16 = 0L;
        Long cpCountByIndustry17 = 0L;
        Long cpCountByIndustry18 = 0L;
        //定义按行业本月缺工企业数
        Long nowLackCpCountByIndustry1 = 0L;
        Long nowLackCpCountByIndustry2 = 0L;
        Long nowLackCpCountByIndustry3 = 0L;
        Long nowLackCpCountByIndustry4 = 0L;
        Long nowLackCpCountByIndustry5 = 0L;
        Long nowLackCpCountByIndustry6 = 0L;
        Long nowLackCpCountByIndustry7 = 0L;
        Long nowLackCpCountByIndustry8 = 0L;
        Long nowLackCpCountByIndustry9 = 0L;
        Long nowLackCpCountByIndustry10 = 0L;
        Long nowLackCpCountByIndustry11 = 0L;
        Long nowLackCpCountByIndustry12 = 0L;
        Long nowLackCpCountByIndustry13 = 0L;
        Long nowLackCpCountByIndustry14 = 0L;
        Long nowLackCpCountByIndustry15 = 0L;
        Long nowLackCpCountByIndustry16 = 0L;
        Long nowLackCpCountByIndustry17 = 0L;
        Long nowLackCpCountByIndustry18 = 0L;
        //定义按行业本月缺工人数
        Long nowLackWorkerCountByIndustry1 = 0L;
        Long nowLackWorkerCountByIndustry2 = 0L;
        Long nowLackWorkerCountByIndustry3 = 0L;
        Long nowLackWorkerCountByIndustry4 = 0L;
        Long nowLackWorkerCountByIndustry5 = 0L;
        Long nowLackWorkerCountByIndustry6 = 0L;
        Long nowLackWorkerCountByIndustry7 = 0L;
        Long nowLackWorkerCountByIndustry8 = 0L;
        Long nowLackWorkerCountByIndustry9 = 0L;
        Long nowLackWorkerCountByIndustry10 = 0L;
        Long nowLackWorkerCountByIndustry11 = 0L;
        Long nowLackWorkerCountByIndustry12 = 0L;
        Long nowLackWorkerCountByIndustry13 = 0L;
        Long nowLackWorkerCountByIndustry14 = 0L;
        Long nowLackWorkerCountByIndustry15 = 0L;
        Long nowLackWorkerCountByIndustry16 = 0L;
        Long nowLackWorkerCountByIndustry17 = 0L;
        Long nowLackWorkerCountByIndustry18 = 0L;
        //定义按行业上月缺工企业数
        Long preLackCpCountByIndustry1 = 0L;
        Long preLackCpCountByIndustry2 = 0L;
        Long preLackCpCountByIndustry3 = 0L;
        Long preLackCpCountByIndustry4 = 0L;
        Long preLackCpCountByIndustry5 = 0L;
        Long preLackCpCountByIndustry6 = 0L;
        Long preLackCpCountByIndustry7 = 0L;
        Long preLackCpCountByIndustry8 = 0L;
        Long preLackCpCountByIndustry9 = 0L;
        Long preLackCpCountByIndustry10 = 0L;
        Long preLackCpCountByIndustry11 = 0L;
        Long preLackCpCountByIndustry12 = 0L;
        Long preLackCpCountByIndustry13 = 0L;
        Long preLackCpCountByIndustry14 = 0L;
        Long preLackCpCountByIndustry15 = 0L;
        Long preLackCpCountByIndustry16 = 0L;
        Long preLackCpCountByIndustry17 = 0L;
        Long preLackCpCountByIndustry18 = 0L;
        //定义按行业上月缺工人数
        Long preLackWorkerCountByIndustry1 = 0L;
        Long preLackWorkerCountByIndustry2 = 0L;
        Long preLackWorkerCountByIndustry3 = 0L;
        Long preLackWorkerCountByIndustry4 = 0L;
        Long preLackWorkerCountByIndustry5 = 0L;
        Long preLackWorkerCountByIndustry6 = 0L;
        Long preLackWorkerCountByIndustry7 = 0L;
        Long preLackWorkerCountByIndustry8 = 0L;
        Long preLackWorkerCountByIndustry9 = 0L;
        Long preLackWorkerCountByIndustry10 = 0L;
        Long preLackWorkerCountByIndustry11 = 0L;
        Long preLackWorkerCountByIndustry12 = 0L;
        Long preLackWorkerCountByIndustry13 = 0L;
        Long preLackWorkerCountByIndustry14 = 0L;
        Long preLackWorkerCountByIndustry15 = 0L;
        Long preLackWorkerCountByIndustry16 = 0L;
        Long preLackWorkerCountByIndustry17 = 0L;
        Long preLackWorkerCountByIndustry18 = 0L;
        //定义按行业年初缺工人数
        Long beginLackCpCountByIndustry1 = 0L;
        Long beginLackCpCountByIndustry2 = 0L;
        Long beginLackCpCountByIndustry3 = 0L;
        Long beginLackCpCountByIndustry4 = 0L;
        Long beginLackCpCountByIndustry5 = 0L;
        Long beginLackCpCountByIndustry6 = 0L;
        Long beginLackCpCountByIndustry7 = 0L;
        Long beginLackCpCountByIndustry8 = 0L;
        Long beginLackCpCountByIndustry9 = 0L;
        Long beginLackCpCountByIndustry10 = 0L;
        Long beginLackCpCountByIndustry11 = 0L;
        Long beginLackCpCountByIndustry12 = 0L;
        Long beginLackCpCountByIndustry13 = 0L;
        Long beginLackCpCountByIndustry14 = 0L;
        Long beginLackCpCountByIndustry15 = 0L;
        Long beginLackCpCountByIndustry16 = 0L;
        Long beginLackCpCountByIndustry17 = 0L;
        Long beginLackCpCountByIndustry18 = 0L;
        //定义按行业年初缺工人数
        Long beginLackWorkerCountByIndustry1 = 0L;
        Long beginLackWorkerCountByIndustry2 = 0L;
        Long beginLackWorkerCountByIndustry3 = 0L;
        Long beginLackWorkerCountByIndustry4 = 0L;
        Long beginLackWorkerCountByIndustry5 = 0L;
        Long beginLackWorkerCountByIndustry6 = 0L;
        Long beginLackWorkerCountByIndustry7 = 0L;
        Long beginLackWorkerCountByIndustry8 = 0L;
        Long beginLackWorkerCountByIndustry9 = 0L;
        Long beginLackWorkerCountByIndustry10 = 0L;
        Long beginLackWorkerCountByIndustry11 = 0L;
        Long beginLackWorkerCountByIndustry12 = 0L;
        Long beginLackWorkerCountByIndustry13 = 0L;
        Long beginLackWorkerCountByIndustry14 = 0L;
        Long beginLackWorkerCountByIndustry15 = 0L;
        Long beginLackWorkerCountByIndustry16 = 0L;
        Long beginLackWorkerCountByIndustry17 = 0L;
        Long beginLackWorkerCountByIndustry18 = 0L;
        //定义按地区监测企业数
        Long cpCountByRegion1 = 0L;
        Long cpCountByRegion2 = 0L;
        Long cpCountByRegion3 = 0L;
        Long cpCountByRegion4 = 0L;
        Long cpCountByRegion5 = 0L;
        Long cpCountByRegion6 = 0L;
        Long cpCountByRegion7 = 0L;
        Long cpCountByRegion8 = 0L;
        //定义按地区本月缺工企业数
        Long nowLackCpCountByRegion1 = 0L;
        Long nowLackCpCountByRegion2 = 0L;
        Long nowLackCpCountByRegion3 = 0L;
        Long nowLackCpCountByRegion4 = 0L;
        Long nowLackCpCountByRegion5 = 0L;
        Long nowLackCpCountByRegion6 = 0L;
        Long nowLackCpCountByRegion7 = 0L;
        Long nowLackCpCountByRegion8 = 0L;
        //定义按地区本月缺工人数
        Long nowLackWorkerCountByRegion1 = 0L;
        Long nowLackWorkerCountByRegion2 = 0L;
        Long nowLackWorkerCountByRegion3 = 0L;
        Long nowLackWorkerCountByRegion4 = 0L;
        Long nowLackWorkerCountByRegion5 = 0L;
        Long nowLackWorkerCountByRegion6 = 0L;
        Long nowLackWorkerCountByRegion7 = 0L;
        Long nowLackWorkerCountByRegion8 = 0L;
        //定义按地区上月缺工企业数
        Long preLackCpCountByRegion1 = 0L;
        Long preLackCpCountByRegion2 = 0L;
        Long preLackCpCountByRegion3 = 0L;
        Long preLackCpCountByRegion4 = 0L;
        Long preLackCpCountByRegion5 = 0L;
        Long preLackCpCountByRegion6 = 0L;
        Long preLackCpCountByRegion7 = 0L;
        Long preLackCpCountByRegion8 = 0L;
        //定义按地区上月缺工人数
        Long preLackWorkerCountByRegion1 = 0L;
        Long preLackWorkerCountByRegion2 = 0L;
        Long preLackWorkerCountByRegion3 = 0L;
        Long preLackWorkerCountByRegion4 = 0L;
        Long preLackWorkerCountByRegion5 = 0L;
        Long preLackWorkerCountByRegion6 = 0L;
        Long preLackWorkerCountByRegion7 = 0L;
        Long preLackWorkerCountByRegion8 = 0L;
        //定义按地区年初缺工企业数
        Long beginLackCpCountByRegion1 = 0L;
        Long beginLackCpCountByRegion2 = 0L;
        Long beginLackCpCountByRegion3 = 0L;
        Long beginLackCpCountByRegion4 = 0L;
        Long beginLackCpCountByRegion5 = 0L;
        Long beginLackCpCountByRegion6 = 0L;
        Long beginLackCpCountByRegion7 = 0L;
        Long beginLackCpCountByRegion8 = 0L;
        //定义按地区年初缺工人数
        Long beginLackWorkerCountByRegion1 = 0L;
        Long beginLackWorkerCountByRegion2 = 0L;
        Long beginLackWorkerCountByRegion3 = 0L;
        Long beginLackWorkerCountByRegion4 = 0L;
        Long beginLackWorkerCountByRegion5 = 0L;
        Long beginLackWorkerCountByRegion6 = 0L;
        Long beginLackWorkerCountByRegion7 = 0L;
        Long beginLackWorkerCountByRegion8 = 0L;
        //循环本月数据
        for (BCp03DTO thisItem :
                thisMonth) {
            String thisItemIndustry = thisItem.getIndustry();
            String thisItemAreacode = thisItem.getAreacode();
            thisItemAreacode = thisItemAreacode.substring(0,6);
            String thisItemEstate = thisItem.getEstate();
            //计算按产业监测企业数，缺工企业数
            switch (thisItemEstate) {
                case "1":
                    cpCountByEstate1++;
                    nowLackWorkerCountByEstate1+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByEstate1++;
                    break;
                case "2":
                    cpCountByEstate2++;
                    nowLackWorkerCountByEstate2+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByEstate2++;
                    break;
                case "3":
                    cpCountByEstate3++;
                    nowLackWorkerCountByEstate3+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByEstate3++;
                    break;
            }
            //计算按行业监测企业数，缺工企业数
            switch (thisItemIndustry) {
                case "1":
                    cpCountByIndustry1++;
                    nowLackWorkerCountByIndustry1+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry1++;
                    break;
                case "2":
                    cpCountByIndustry2++;
                    nowLackWorkerCountByIndustry2+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry2++;
                    break;
                case "3":
                    cpCountByIndustry3++;
                    nowLackWorkerCountByIndustry3+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry3++;
                    break;
                case "4":
                    cpCountByIndustry4++;
                    nowLackWorkerCountByIndustry4+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry4++;
                    break;
                case "5":
                    cpCountByIndustry5++;
                    nowLackWorkerCountByIndustry5+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry5++;
                    break;
                case "6":
                    cpCountByIndustry6++;
                    nowLackWorkerCountByIndustry6+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry6++;
                    break;
                case "7":
                    cpCountByIndustry7++;
                    nowLackWorkerCountByIndustry7+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry7++;
                    break;
                case "8":
                    cpCountByIndustry8++;
                    nowLackWorkerCountByIndustry8+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry8++;
                    break;
                case "9":
                    cpCountByIndustry9++;
                    nowLackWorkerCountByIndustry9+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry9++;
                    break;
                case "10":
                    cpCountByIndustry10++;
                    nowLackWorkerCountByIndustry10+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry10++;
                    break;
                case "11":
                    cpCountByIndustry11++;
                    nowLackWorkerCountByIndustry11+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry11++;
                    break;
                case "12":
                    cpCountByIndustry12++;
                    nowLackWorkerCountByIndustry12+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry12++;
                    break;
                case "13":
                    cpCountByIndustry13++;
                    nowLackWorkerCountByIndustry13+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry13++;
                    break;
                case "14":
                    cpCountByIndustry14++;
                    nowLackWorkerCountByIndustry14+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry14++;
                    break;
                case "15":
                    cpCountByIndustry15++;
                    nowLackWorkerCountByIndustry15+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry15++;
                    break;
                case "16":
                    cpCountByIndustry16++;
                    nowLackWorkerCountByIndustry16+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry16++;
                    break;
                case "17":
                    cpCountByIndustry17++;
                    nowLackWorkerCountByIndustry17+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByIndustry17++;
                    break;
            }
            //计算按地区监测企业数，缺工企业数，缺工工人数
            switch (thisItemAreacode) {
                case "511602"://广安区
                    cpCountByRegion1++;
                    nowLackWorkerCountByRegion1+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion1++;
                    break;
                case "511603"://前锋区
                    cpCountByRegion2++;
                    nowLackWorkerCountByRegion2+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion2++;
                    break;
                case "511621"://岳池县
                    cpCountByRegion3++;
                    nowLackWorkerCountByRegion3+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion3++;
                    break;
                case "511622"://武胜县
                    cpCountByRegion4++;
                    nowLackWorkerCountByRegion4+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion4++;
                    break;
                case "511623"://邻水县
                    cpCountByRegion5++;
                    nowLackWorkerCountByRegion5+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion5++;
                    break;
                case "511681"://华蓥市
                    cpCountByRegion6++;
                    nowLackWorkerCountByRegion6+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion6++;
                    break;
                case "511699"://暂定经开区
                    cpCountByRegion7++;
                    nowLackWorkerCountByRegion7+=thisItem.getAllLackWorker();
                    if (thisItem.getAllLackWorker() > 0)
                        nowLackCpCountByRegion7++;
                    break;
            }
        }
        //循环上月数据
        for (BCp03DTO preItem :
                preMonth) {
            String preItemIndustry = preItem.getIndustry();
            String preItemAreacode = preItem.getAreacode();
            preItemAreacode = preItemAreacode.substring(0,6);
            String preItemEstate = preItem.getEstate();
            //计算按产业监测企业数，缺工企业数
            switch (preItemEstate) {
                case "1":
                    preLackWorkerCountByEstate1+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByEstate1++;
                    break;
                case "2":
                    preLackWorkerCountByEstate2+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByEstate2++;
                    break;
                case "3":
                    preLackWorkerCountByEstate3+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByEstate3++;
                    break;
            }
            //计算按行业监测企业数，缺工企业数
            switch (preItemIndustry) {
                case "1":
                    preLackWorkerCountByIndustry1+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry1++;
                    break;
                case "2":
                    preLackWorkerCountByIndustry2+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry2++;
                    break;
                case "3":
                    preLackWorkerCountByIndustry3+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry3++;
                    break;
                case "4":
                    preLackWorkerCountByIndustry4+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry4++;
                    break;
                case "5":
                    preLackWorkerCountByIndustry5+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry5++;
                    break;
                case "6":
                    preLackWorkerCountByIndustry6+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry6++;
                    break;
                case "7":
                    preLackWorkerCountByIndustry7+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry7++;
                    break;
                case "8":
                    preLackWorkerCountByIndustry8+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry8++;
                    break;
                case "9":
                    preLackWorkerCountByIndustry9+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry9++;
                    break;
                case "10":
                    preLackWorkerCountByIndustry10+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry10++;
                    break;
                case "11":
                    preLackWorkerCountByIndustry11+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry11++;
                    break;
                case "12":
                    preLackWorkerCountByIndustry12+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry12++;
                    break;
                case "13":
                    preLackWorkerCountByIndustry13+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry13++;
                    break;
                case "14":
                    preLackWorkerCountByIndustry14+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry14++;
                    break;
                case "15":
                    preLackWorkerCountByIndustry15+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry15++;
                    break;
                case "16":
                    preLackWorkerCountByIndustry16+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry16++;
                    break;
                case "17":
                    preLackWorkerCountByIndustry17+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByIndustry17++;
                    break;
            }
            //计算监测企业数,计算上月按地区缺工企业数
            switch (preItemAreacode) {
                case "511602"://广安区
                    preLackWorkerCountByRegion1+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion1++;
                    break;
                case "511603"://前锋区
                    preLackWorkerCountByRegion2+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion2++;
                    break;
                case "511621"://岳池县
                    preLackWorkerCountByRegion3+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion3++;
                    break;
                case "511622"://武胜县
                    preLackWorkerCountByRegion4+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion4++;
                    break;
                case "511623"://邻水县
                    preLackWorkerCountByRegion5+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion5++;
                    break;
                case "511681"://华蓥市
                    preLackWorkerCountByRegion6+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion6++;
                    break;
                case "511699"://暂定经开区
                    preLackWorkerCountByRegion7+=preItem.getAllLackWorker();
                    if (preItem.getAllLackWorker() > 0)
                        preLackCpCountByRegion7++;
                    break;
            }
        }
        //循环年初数据
        for (BCp03DTO beginItem :
                beginMonth) {
            String beginItemIndustry = beginItem.getIndustry();
            String beginItemAreacode = beginItem.getAreacode();
            String beginItemEstate = beginItem.getEstate();
            //计算按产业监测企业数，缺工企业数
            switch (beginItemEstate) {
                case "1":
                    beginLackWorkerCountByEstate1+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByEstate1++;
                    break;
                case "2":
                    beginLackWorkerCountByEstate2+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByEstate2++;
                    break;
                case "3":
                    beginLackWorkerCountByEstate3+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByEstate3++;
                    break;
            }
            //计算按行业监测企业数，缺工企业数
            switch (beginItemIndustry) {
                case "1":
                    beginLackWorkerCountByIndustry1+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry1++;
                    break;
                case "2":
                    beginLackWorkerCountByIndustry2+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry2++;
                    break;
                case "3":
                    beginLackWorkerCountByIndustry3+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry3++;
                    break;
                case "4":
                    beginLackWorkerCountByIndustry4+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry4++;
                    break;
                case "5":
                    beginLackWorkerCountByIndustry5+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry5++;
                    break;
                case "6":
                    beginLackWorkerCountByIndustry6+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry6++;
                    break;
                case "7":
                    beginLackWorkerCountByIndustry7+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry7++;
                    break;
                case "8":
                    beginLackWorkerCountByIndustry8+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry8++;
                    break;
                case "9":
                    beginLackWorkerCountByIndustry9+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry9++;
                    break;
                case "10":
                    beginLackWorkerCountByIndustry10+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry10++;
                    break;
                case "11":
                    beginLackWorkerCountByIndustry11+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry11++;
                    break;
                case "12":
                    beginLackWorkerCountByIndustry12+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry12++;
                    break;
                case "13":
                    beginLackWorkerCountByIndustry13+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry13++;
                    break;
                case "14":
                    beginLackWorkerCountByIndustry14+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry14++;
                    break;
                case "15":
                    beginLackWorkerCountByIndustry15+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry15++;
                    break;
                case "16":
                    beginLackWorkerCountByIndustry16+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry16++;
                    break;
                case "17":
                    beginLackWorkerCountByIndustry17+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByIndustry17++;
                    break;
            }
            //计算监测企业数,计算当月总工人数
            switch (beginItemAreacode.substring(0,6)) {
                case "511602"://广安区
                    beginLackWorkerCountByRegion1+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion1++;
                    beginLackCpCountByRegion8++;
                    break;
                case "511603"://前锋区
                    beginLackWorkerCountByRegion2+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion2++;
                    beginLackCpCountByRegion8++;
                    break;
                case "511621"://岳池县
                    beginLackWorkerCountByRegion3+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion3++;
                    beginLackCpCountByRegion8++;
                    break;
                case "511622"://武胜县
                    beginLackWorkerCountByRegion4+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion4++;
                    beginLackCpCountByRegion8++;
                    break;
                case "511623"://邻水县
                    beginLackWorkerCountByRegion5+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion5++;
                    beginLackCpCountByRegion8++;
                    break;
                case "511681"://华蓥市
                    beginLackWorkerCountByRegion6+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion6++;
                    beginLackCpCountByRegion8++;
                    break;
                case "511699"://暂定经开区
                    beginLackWorkerCountByRegion7+=beginItem.getAllLackWorker();
                    if (beginItem.getAllLackWorker() > 0)
                        beginLackCpCountByRegion7++;
                    beginLackCpCountByRegion8++;
                    break;
            }
        }
        //构造按产业数据
        String category11 = "按产业";
        LackWorkerDto lackWorkerDtoByEstate1 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByEstate2 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByEstate3 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByEstate4 = new LackWorkerDto();
        lackWorkerDtoByEstate1.setCategory1(category11);
        lackWorkerDtoByEstate1.setCategory2("第一产业");
        lackWorkerDtoByEstate1.setCpCount(cpCountByEstate1);
        lackWorkerDtoByEstate1.setNowLackWorkerCount(nowLackWorkerCountByEstate1);
        lackWorkerDtoByEstate1.setNowLackCpCount(nowLackCpCountByEstate1);
        lackWorkerDtoByEstate1.setNowLackCpCountRange(Math.round(nowLackCpCountByEstate1 * 100.00D / cpCountByEstate1) + "%");
        lackWorkerDtoByEstate1.setPreLackCpCount(preLackCpCountByEstate1);
        lackWorkerDtoByEstate1.setPreLackWorkerCount(preLackWorkerCountByEstate1);
        lackWorkerDtoByEstate1.setPreLackCpCountRange(Math.round((preLackCpCountByEstate1) * 100.00D / cpCountByEstate1) + "%");
        lackWorkerDtoByEstate1.setBeginLackCpCount(beginLackCpCountByEstate1);
        lackWorkerDtoByEstate1.setBeginLackWorkerCount(beginLackWorkerCountByEstate1);
        lackWorkerDtoByEstate1.setBeginLackCpCountRange(Math.round((beginLackCpCountByEstate1) * 100.00D / cpCountByEstate1) + "%");

        lackWorkerDtoByEstate2.setCategory1(category11);
        lackWorkerDtoByEstate2.setCategory2("第二产业");
        lackWorkerDtoByEstate2.setCpCount(cpCountByEstate2);
        lackWorkerDtoByEstate2.setNowLackWorkerCount(nowLackWorkerCountByEstate2);
        lackWorkerDtoByEstate2.setNowLackCpCount(nowLackCpCountByEstate2);
        lackWorkerDtoByEstate2.setNowLackCpCountRange(Math.round(nowLackCpCountByEstate2 * 100.00D / cpCountByEstate2) + "%");
        lackWorkerDtoByEstate2.setPreLackCpCount(preLackCpCountByEstate2);
        lackWorkerDtoByEstate2.setPreLackWorkerCount(preLackWorkerCountByEstate2);
        lackWorkerDtoByEstate2.setPreLackCpCountRange(Math.round((preLackCpCountByEstate2) * 100.00D / cpCountByEstate2) + "%");
        lackWorkerDtoByEstate2.setBeginLackCpCount(beginLackCpCountByEstate2);
        lackWorkerDtoByEstate2.setBeginLackWorkerCount(beginLackWorkerCountByEstate2);
        lackWorkerDtoByEstate2.setBeginLackCpCountRange(Math.round((beginLackCpCountByEstate2) * 100.00D / cpCountByEstate2) + "%");

        lackWorkerDtoByEstate3.setCategory1(category11);
        lackWorkerDtoByEstate3.setCategory2("第三产业");
        lackWorkerDtoByEstate3.setCpCount(cpCountByEstate3);
        lackWorkerDtoByEstate3.setNowLackWorkerCount(nowLackWorkerCountByEstate3);
        lackWorkerDtoByEstate3.setNowLackWorkerCount(nowLackWorkerCountByEstate3);
        lackWorkerDtoByEstate3.setNowLackCpCount(nowLackCpCountByEstate3);
        lackWorkerDtoByEstate3.setNowLackCpCountRange(Math.round(nowLackCpCountByEstate3 * 100.00D / cpCountByEstate3) + "%");
        lackWorkerDtoByEstate3.setPreLackCpCount(preLackCpCountByEstate3);
        lackWorkerDtoByEstate3.setPreLackWorkerCount(preLackWorkerCountByEstate3);
        lackWorkerDtoByEstate3.setPreLackCpCountRange(Math.round((preLackCpCountByEstate3) * 100.00D / cpCountByEstate3) + "%");
        lackWorkerDtoByEstate3.setBeginLackCpCount(beginLackCpCountByEstate3);
        lackWorkerDtoByEstate3.setBeginLackWorkerCount(beginLackWorkerCountByEstate3);
        lackWorkerDtoByEstate3.setBeginLackCpCountRange(Math.round((beginLackCpCountByEstate3) * 100.00D / cpCountByEstate3) + "%");

        lackWorkerDtoByEstate4.setCategory1(category11);
        lackWorkerDtoByEstate4.setCategory2("总计");
        lackWorkerDtoByEstate4.setCpCount(cpCountByEstate1 + cpCountByEstate2 + cpCountByEstate3);
        lackWorkerDtoByEstate4.setNowLackWorkerCount(nowLackWorkerCountByEstate1+nowLackWorkerCountByEstate2+nowLackWorkerCountByEstate3);
        lackWorkerDtoByEstate4.setNowLackCpCount(nowLackCpCountByEstate1 + nowLackCpCountByEstate2 + nowLackCpCountByEstate3);
        lackWorkerDtoByEstate4.setNowLackCpCountRange("-");
        lackWorkerDtoByEstate4.setPreLackCpCount(preLackCpCountByEstate1 + preLackCpCountByEstate2 + preLackCpCountByEstate3);
        lackWorkerDtoByEstate4.setPreLackCpCountRange("-");
        lackWorkerDtoByEstate4.setPreLackWorkerCount(preLackWorkerCountByEstate1+preLackWorkerCountByEstate2+preLackWorkerCountByEstate3);
        lackWorkerDtoByEstate4.setBeginLackCpCount(beginLackCpCountByEstate1 + beginLackCpCountByEstate2 + beginLackCpCountByEstate3);
        lackWorkerDtoByEstate4.setBeginLackWorkerCount(beginLackWorkerCountByEstate1+beginLackWorkerCountByEstate2+beginLackWorkerCountByEstate3);
        lackWorkerDtoByEstate4.setBeginLackCpCountRange("-");


        //构造按行业业数据
        String category12 = "按行业";
        LackWorkerDto lackWorkerDtoByIndustry1 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry2 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry3 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry4 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry5 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry6 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry7 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry8 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry9 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry10 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry11 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry12 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry13 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry14 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry15 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry16 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry17 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByIndustry18 = new LackWorkerDto();

        lackWorkerDtoByIndustry1.setCategory1(category12);
        lackWorkerDtoByIndustry1.setCategory2("A农、林、牧、渔业");
        lackWorkerDtoByIndustry1.setCpCount(cpCountByIndustry1);
        lackWorkerDtoByIndustry1.setNowLackWorkerCount(nowLackWorkerCountByIndustry1);
        lackWorkerDtoByIndustry1.setNowLackCpCount(nowLackCpCountByIndustry1);
        lackWorkerDtoByIndustry1.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry1 * 100.00D / cpCountByIndustry1) + "%");
        lackWorkerDtoByIndustry1.setPreLackCpCount(preLackCpCountByIndustry1);
        lackWorkerDtoByIndustry1.setPreLackWorkerCount(preLackWorkerCountByIndustry1);
        lackWorkerDtoByIndustry1.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry1) * 100.00D / cpCountByIndustry1) + "%");
        lackWorkerDtoByIndustry1.setBeginLackCpCount(beginLackCpCountByIndustry1);
        lackWorkerDtoByIndustry1.setBeginLackWorkerCount(beginLackWorkerCountByIndustry1);
        lackWorkerDtoByIndustry1.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry1) * 100.00D / cpCountByIndustry1) + "%");

        lackWorkerDtoByIndustry2.setCategory1(category12);
        lackWorkerDtoByIndustry2.setCategory2("B采矿业");
        lackWorkerDtoByIndustry2.setCpCount(cpCountByIndustry2);
        lackWorkerDtoByIndustry2.setNowLackCpCount(nowLackCpCountByIndustry2);
        lackWorkerDtoByIndustry2.setNowLackWorkerCount(nowLackWorkerCountByIndustry2);
        lackWorkerDtoByIndustry2.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry2 * 100.00D / cpCountByIndustry2) + "%");
        lackWorkerDtoByIndustry2.setPreLackCpCount(preLackCpCountByIndustry2);
        lackWorkerDtoByIndustry2.setPreLackWorkerCount(preLackWorkerCountByIndustry2);
        lackWorkerDtoByIndustry2.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry2) * 100.00D / cpCountByIndustry2) + "%");
        lackWorkerDtoByIndustry2.setBeginLackCpCount(beginLackCpCountByIndustry2);
        lackWorkerDtoByIndustry2.setBeginLackWorkerCount(beginLackWorkerCountByIndustry2);
        lackWorkerDtoByIndustry2.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry2) * 100.00D / cpCountByIndustry2) + "%");

        lackWorkerDtoByIndustry3.setCategory1(category12);
        lackWorkerDtoByIndustry3.setCategory2("C制造业");
        lackWorkerDtoByIndustry3.setCpCount(cpCountByIndustry3);
        lackWorkerDtoByIndustry3.setNowLackCpCount(nowLackCpCountByIndustry3);
        lackWorkerDtoByIndustry3.setNowLackWorkerCount(nowLackWorkerCountByIndustry3);
        lackWorkerDtoByIndustry3.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry3 * 100.00D / cpCountByIndustry3) + "%");
        lackWorkerDtoByIndustry3.setPreLackCpCount(preLackCpCountByIndustry3);
        lackWorkerDtoByIndustry3.setPreLackWorkerCount(preLackWorkerCountByIndustry3);
        lackWorkerDtoByIndustry3.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry3) * 100.00D / cpCountByIndustry3) + "%");
        lackWorkerDtoByIndustry3.setBeginLackCpCount(beginLackCpCountByIndustry3);
        lackWorkerDtoByIndustry3.setBeginLackWorkerCount(beginLackWorkerCountByIndustry3);
        lackWorkerDtoByIndustry3.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry3) * 100.00D / cpCountByIndustry3) + "%");

        lackWorkerDtoByIndustry4.setCategory1(category12);
        lackWorkerDtoByIndustry4.setCategory2("D电力、热力、燃气及水生产和供应业");
        lackWorkerDtoByIndustry4.setCpCount(cpCountByIndustry4);
        lackWorkerDtoByIndustry4.setNowLackCpCount(nowLackCpCountByIndustry4);
        lackWorkerDtoByIndustry4.setNowLackWorkerCount(nowLackWorkerCountByIndustry4);
        lackWorkerDtoByIndustry4.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry4 * 100.00D / cpCountByIndustry4) + "%");
        lackWorkerDtoByIndustry4.setPreLackCpCount(preLackCpCountByIndustry1);
        lackWorkerDtoByIndustry4.setPreLackWorkerCount(preLackWorkerCountByIndustry4);
        lackWorkerDtoByIndustry4.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry4) * 100.00D / cpCountByIndustry4) + "%");
        lackWorkerDtoByIndustry4.setBeginLackCpCount(beginLackCpCountByIndustry4);
        lackWorkerDtoByIndustry4.setBeginLackWorkerCount(beginLackWorkerCountByIndustry4);
        lackWorkerDtoByIndustry4.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry4) * 100.00D / cpCountByIndustry4) + "%");

        lackWorkerDtoByIndustry5.setCategory1(category12);
        lackWorkerDtoByIndustry5.setCategory2("E建筑业");
        lackWorkerDtoByIndustry5.setCpCount(cpCountByIndustry5);
        lackWorkerDtoByIndustry5.setNowLackCpCount(nowLackCpCountByIndustry5);
        lackWorkerDtoByIndustry5.setNowLackWorkerCount(nowLackWorkerCountByIndustry5);
        lackWorkerDtoByIndustry5.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry5 * 100.00D / cpCountByIndustry5) + "%");
        lackWorkerDtoByIndustry5.setPreLackCpCount(preLackCpCountByIndustry5);
        lackWorkerDtoByIndustry5.setPreLackWorkerCount(preLackWorkerCountByIndustry5);
        lackWorkerDtoByIndustry5.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry5) * 100.00D / cpCountByIndustry5) + "%");
        lackWorkerDtoByIndustry5.setBeginLackCpCount(beginLackCpCountByIndustry5);
        lackWorkerDtoByIndustry5.setBeginLackWorkerCount(beginLackWorkerCountByIndustry5);
        lackWorkerDtoByIndustry5.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry5) * 100.00D / cpCountByIndustry5) + "%");

        lackWorkerDtoByIndustry6.setCategory1(category12);
        lackWorkerDtoByIndustry6.setCategory2("F批发和零售业");
        lackWorkerDtoByIndustry6.setCpCount(cpCountByIndustry6);
        lackWorkerDtoByIndustry6.setNowLackCpCount(nowLackCpCountByIndustry6);
        lackWorkerDtoByIndustry6.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry6 * 100.00D / cpCountByIndustry6) + "%");
        lackWorkerDtoByIndustry6.setPreLackCpCount(preLackCpCountByIndustry6);
        lackWorkerDtoByIndustry6.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry6) * 100.00D / cpCountByIndustry6) + "%");
        lackWorkerDtoByIndustry6.setBeginLackCpCount(beginLackCpCountByIndustry6);
        lackWorkerDtoByIndustry6.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry6) * 100.00D / cpCountByIndustry6) + "%");
        lackWorkerDtoByIndustry6.setNowLackWorkerCount(nowLackWorkerCountByIndustry6);
        lackWorkerDtoByIndustry6.setPreLackWorkerCount(preLackWorkerCountByIndustry6);
        lackWorkerDtoByIndustry6.setBeginLackWorkerCount(beginLackWorkerCountByIndustry6);

        lackWorkerDtoByIndustry7.setCategory1(category12);
        lackWorkerDtoByIndustry7.setCategory2("G交通运输、仓储和邮政业");
        lackWorkerDtoByIndustry7.setCpCount(cpCountByIndustry7);
        lackWorkerDtoByIndustry7.setNowLackCpCount(nowLackCpCountByIndustry7);
        lackWorkerDtoByIndustry7.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry7 * 100.00D / cpCountByIndustry7) + "%");
        lackWorkerDtoByIndustry7.setPreLackCpCount(preLackCpCountByIndustry7);
        lackWorkerDtoByIndustry7.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry7) * 100.00D / cpCountByIndustry7) + "%");
        lackWorkerDtoByIndustry7.setBeginLackCpCount(beginLackCpCountByIndustry7);
        lackWorkerDtoByIndustry7.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry7) * 100.00D / cpCountByIndustry7) + "%");
        lackWorkerDtoByIndustry7.setNowLackWorkerCount(nowLackWorkerCountByIndustry7);
        lackWorkerDtoByIndustry7.setPreLackWorkerCount(preLackWorkerCountByIndustry7);
        lackWorkerDtoByIndustry7.setBeginLackWorkerCount(beginLackWorkerCountByIndustry7);

        lackWorkerDtoByIndustry8.setCategory1(category12);
        lackWorkerDtoByIndustry8.setCategory2("H住宿和餐饮业");
        lackWorkerDtoByIndustry8.setCpCount(cpCountByIndustry1);
        lackWorkerDtoByIndustry8.setNowLackCpCount(nowLackCpCountByIndustry8);
        lackWorkerDtoByIndustry8.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry8 * 100.00D / cpCountByIndustry8) + "%");
        lackWorkerDtoByIndustry8.setPreLackCpCount(preLackCpCountByIndustry8);
        lackWorkerDtoByIndustry8.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry8) * 100.00D / cpCountByIndustry8) + "%");
        lackWorkerDtoByIndustry8.setBeginLackCpCount(beginLackCpCountByIndustry8);
        lackWorkerDtoByIndustry8.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry8) * 100.00D / cpCountByIndustry8) + "%");
        lackWorkerDtoByIndustry8.setNowLackWorkerCount(nowLackWorkerCountByIndustry8);
        lackWorkerDtoByIndustry8.setPreLackWorkerCount(preLackWorkerCountByIndustry8);
        lackWorkerDtoByIndustry8.setBeginLackWorkerCount(beginLackWorkerCountByIndustry8);

        lackWorkerDtoByIndustry9.setCategory1(category12);
        lackWorkerDtoByIndustry9.setCategory2("I信息传输、软件和信息技术服务业");
        lackWorkerDtoByIndustry9.setCpCount(cpCountByIndustry9);
        lackWorkerDtoByIndustry9.setNowLackCpCount(nowLackCpCountByIndustry9);
        lackWorkerDtoByIndustry9.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry9 * 100.00D / cpCountByIndustry9) + "%");
        lackWorkerDtoByIndustry9.setPreLackCpCount(preLackCpCountByIndustry9);
        lackWorkerDtoByIndustry9.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry9) * 100.00D / cpCountByIndustry9) + "%");
        lackWorkerDtoByIndustry9.setBeginLackCpCount(beginLackCpCountByIndustry9);
        lackWorkerDtoByIndustry9.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry9) * 100.00D / cpCountByIndustry9) + "%");
        lackWorkerDtoByIndustry9.setNowLackWorkerCount(nowLackWorkerCountByIndustry9);
        lackWorkerDtoByIndustry9.setPreLackWorkerCount(preLackWorkerCountByIndustry9);
        lackWorkerDtoByIndustry9.setBeginLackWorkerCount(beginLackWorkerCountByIndustry9);

        lackWorkerDtoByIndustry10.setCategory1(category12);
        lackWorkerDtoByIndustry10.setCategory2("J金融业");
        lackWorkerDtoByIndustry10.setCpCount(cpCountByIndustry10);
        lackWorkerDtoByIndustry10.setNowLackCpCount(nowLackCpCountByIndustry10);
        lackWorkerDtoByIndustry10.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry10 * 100.00D / cpCountByIndustry10) + "%");
        lackWorkerDtoByIndustry10.setPreLackCpCount(preLackCpCountByIndustry10);
        lackWorkerDtoByIndustry10.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry10) * 100.00D / cpCountByIndustry10) + "%");
        lackWorkerDtoByIndustry10.setBeginLackCpCount(beginLackCpCountByIndustry10);
        lackWorkerDtoByIndustry10.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry10) * 100.00D / cpCountByIndustry10) + "%");
        lackWorkerDtoByIndustry10.setNowLackWorkerCount(nowLackWorkerCountByIndustry10);
        lackWorkerDtoByIndustry10.setPreLackWorkerCount(preLackWorkerCountByIndustry10);
        lackWorkerDtoByIndustry10.setBeginLackWorkerCount(beginLackWorkerCountByIndustry10);

        lackWorkerDtoByIndustry11.setCategory1(category12);
        lackWorkerDtoByIndustry11.setCategory2("K房地产业");
        lackWorkerDtoByIndustry11.setCpCount(cpCountByIndustry11);
        lackWorkerDtoByIndustry11.setNowLackCpCount(nowLackCpCountByIndustry11);
        lackWorkerDtoByIndustry11.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry11 * 100.00D / cpCountByIndustry11) + "%");
        lackWorkerDtoByIndustry11.setPreLackCpCount(preLackCpCountByIndustry11);
        lackWorkerDtoByIndustry11.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry11) * 100.00D / cpCountByIndustry11) + "%");
        lackWorkerDtoByIndustry11.setBeginLackCpCount(beginLackCpCountByIndustry11);
        lackWorkerDtoByIndustry11.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry11) * 100.00D / cpCountByIndustry11) + "%");
        lackWorkerDtoByIndustry11.setNowLackWorkerCount(nowLackWorkerCountByIndustry11);
        lackWorkerDtoByIndustry11.setPreLackWorkerCount(preLackWorkerCountByIndustry11);
        lackWorkerDtoByIndustry11.setBeginLackWorkerCount(beginLackWorkerCountByIndustry11);

        lackWorkerDtoByIndustry12.setCategory1(category12);
        lackWorkerDtoByIndustry12.setCategory2("L租赁和商务服务业");
        lackWorkerDtoByIndustry12.setCpCount(cpCountByIndustry12);
        lackWorkerDtoByIndustry12.setNowLackCpCount(nowLackCpCountByIndustry12);
        lackWorkerDtoByIndustry12.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry12 * 100.00D / cpCountByIndustry12) + "%");
        lackWorkerDtoByIndustry12.setPreLackCpCount(preLackCpCountByIndustry12);
        lackWorkerDtoByIndustry12.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry12) * 100.00D / cpCountByIndustry12) + "%");
        lackWorkerDtoByIndustry12.setBeginLackCpCount(beginLackCpCountByIndustry12);
        lackWorkerDtoByIndustry12.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry12) * 100.00D / cpCountByIndustry12) + "%");
        lackWorkerDtoByIndustry12.setNowLackWorkerCount(nowLackWorkerCountByIndustry12);
        lackWorkerDtoByIndustry12.setPreLackWorkerCount(preLackWorkerCountByIndustry12);
        lackWorkerDtoByIndustry12.setBeginLackWorkerCount(beginLackWorkerCountByIndustry12);

        lackWorkerDtoByIndustry13.setCategory1(category12);
        lackWorkerDtoByIndustry13.setCategory2("M科学研究和技术服务业");
        lackWorkerDtoByIndustry13.setCpCount(cpCountByIndustry13);
        lackWorkerDtoByIndustry13.setNowLackCpCount(nowLackCpCountByIndustry13);
        lackWorkerDtoByIndustry13.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry13 * 100.00D / cpCountByIndustry13) + "%");
        lackWorkerDtoByIndustry13.setPreLackCpCount(preLackCpCountByIndustry13);
        lackWorkerDtoByIndustry13.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry13) * 100.00D / cpCountByIndustry13) + "%");
        lackWorkerDtoByIndustry13.setBeginLackCpCount(beginLackCpCountByIndustry13);
        lackWorkerDtoByIndustry13.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry13) * 100.00D / cpCountByIndustry13) + "%");
        lackWorkerDtoByIndustry13.setNowLackWorkerCount(nowLackWorkerCountByIndustry13);
        lackWorkerDtoByIndustry13.setPreLackWorkerCount(preLackWorkerCountByIndustry13);
        lackWorkerDtoByIndustry13.setBeginLackWorkerCount(beginLackWorkerCountByIndustry13);

        lackWorkerDtoByIndustry14.setCategory1(category12);
        lackWorkerDtoByIndustry14.setCategory2("O居民服务、修理和其他服务业");
        lackWorkerDtoByIndustry14.setCpCount(cpCountByIndustry14);
        lackWorkerDtoByIndustry14.setNowLackCpCount(nowLackCpCountByIndustry14);
        lackWorkerDtoByIndustry14.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry14 * 100.00D / cpCountByIndustry14) + "%");
        lackWorkerDtoByIndustry14.setPreLackCpCount(preLackCpCountByIndustry14);
        lackWorkerDtoByIndustry14.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry14) * 100.00D / cpCountByIndustry14) + "%");
        lackWorkerDtoByIndustry14.setBeginLackCpCount(beginLackCpCountByIndustry14);
        lackWorkerDtoByIndustry14.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry14) * 100.00D / cpCountByIndustry14) + "%");
        lackWorkerDtoByIndustry14.setNowLackWorkerCount(nowLackWorkerCountByIndustry14);
        lackWorkerDtoByIndustry14.setPreLackWorkerCount(preLackWorkerCountByIndustry14);
        lackWorkerDtoByIndustry14.setBeginLackWorkerCount(beginLackWorkerCountByIndustry14);

        lackWorkerDtoByIndustry15.setCategory1(category12);
        lackWorkerDtoByIndustry15.setCategory2("P教育");
        lackWorkerDtoByIndustry15.setCpCount(cpCountByIndustry15);
        lackWorkerDtoByIndustry15.setNowLackCpCount(nowLackCpCountByIndustry15);
        lackWorkerDtoByIndustry15.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry15 * 100.00D / cpCountByIndustry15) + "%");
        lackWorkerDtoByIndustry15.setPreLackCpCount(preLackCpCountByIndustry15);
        lackWorkerDtoByIndustry15.setPreLackCpCountRange(Math.round((- preLackCpCountByIndustry15) * 100.00D / cpCountByIndustry15) + "%");
        lackWorkerDtoByIndustry15.setBeginLackCpCount(beginLackCpCountByIndustry15);
        lackWorkerDtoByIndustry15.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry15) * 100.00D / cpCountByIndustry15) + "%");
        lackWorkerDtoByIndustry15.setNowLackWorkerCount(nowLackWorkerCountByIndustry15);
        lackWorkerDtoByIndustry15.setPreLackWorkerCount(preLackWorkerCountByIndustry15);
        lackWorkerDtoByIndustry15.setBeginLackWorkerCount(beginLackWorkerCountByIndustry15);

        lackWorkerDtoByIndustry16.setCategory1(category12);
        lackWorkerDtoByIndustry16.setCategory2("Q卫生和社会工作");
        lackWorkerDtoByIndustry16.setCpCount(cpCountByIndustry16);
        lackWorkerDtoByIndustry16.setNowLackCpCount(nowLackCpCountByIndustry16);
        lackWorkerDtoByIndustry16.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry16 * 100.00D / cpCountByIndustry16) + "%");
        lackWorkerDtoByIndustry16.setPreLackCpCount(preLackCpCountByIndustry16);
        lackWorkerDtoByIndustry16.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry16) * 100.00D / cpCountByIndustry16) + "%");
        lackWorkerDtoByIndustry16.setBeginLackCpCount(beginLackCpCountByIndustry16);
        lackWorkerDtoByIndustry16.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry16) * 100.00D / cpCountByIndustry16) + "%");
        lackWorkerDtoByIndustry16.setNowLackWorkerCount(nowLackWorkerCountByIndustry16);
        lackWorkerDtoByIndustry16.setPreLackWorkerCount(preLackWorkerCountByIndustry16);
        lackWorkerDtoByIndustry16.setBeginLackWorkerCount(beginLackWorkerCountByIndustry16);

        lackWorkerDtoByIndustry17.setCategory1(category12);
        lackWorkerDtoByIndustry17.setCategory2("R文化、体育和娱乐业");
        lackWorkerDtoByIndustry17.setCpCount(cpCountByIndustry17);
        lackWorkerDtoByIndustry17.setNowLackCpCount(nowLackCpCountByIndustry17);
        lackWorkerDtoByIndustry17.setNowLackCpCountRange(Math.round(nowLackCpCountByIndustry17 * 100.00D / cpCountByIndustry17) + "%");
        lackWorkerDtoByIndustry17.setPreLackCpCount(preLackCpCountByIndustry1);
        lackWorkerDtoByIndustry17.setPreLackCpCountRange(Math.round((preLackCpCountByIndustry17) * 100.00D / cpCountByIndustry17) + "%");
        lackWorkerDtoByIndustry17.setBeginLackCpCount(beginLackCpCountByIndustry17);
        lackWorkerDtoByIndustry17.setBeginLackCpCountRange(Math.round((beginLackCpCountByIndustry17) * 100.00D / cpCountByIndustry17) + "%");
        lackWorkerDtoByIndustry17.setNowLackWorkerCount(nowLackWorkerCountByIndustry17);
        lackWorkerDtoByIndustry17.setPreLackWorkerCount(preLackWorkerCountByIndustry17);
        lackWorkerDtoByIndustry17.setBeginLackWorkerCount(beginLackWorkerCountByIndustry17);

        lackWorkerDtoByIndustry18.setCategory1(category12);
        lackWorkerDtoByIndustry18.setCategory2("总计");
        lackWorkerDtoByIndustry18.setCpCount(cpCountByIndustry1 + cpCountByIndustry2 + cpCountByIndustry3 + cpCountByIndustry4 + cpCountByIndustry5 + cpCountByIndustry6 + cpCountByIndustry7 + cpCountByIndustry8 + cpCountByIndustry9 + cpCountByIndustry10 + cpCountByIndustry11 + cpCountByIndustry12 + cpCountByIndustry13 + cpCountByIndustry14 + cpCountByIndustry15 + cpCountByIndustry16 + cpCountByIndustry17);
        lackWorkerDtoByIndustry18.setNowLackCpCount(nowLackCpCountByIndustry18);
        lackWorkerDtoByIndustry18.setNowLackCpCountRange("-");
        lackWorkerDtoByIndustry18.setPreLackCpCount(preLackCpCountByIndustry18);
        lackWorkerDtoByIndustry18.setPreLackCpCountRange("-");
        lackWorkerDtoByIndustry18.setBeginLackCpCount(beginLackCpCountByIndustry18);
        lackWorkerDtoByIndustry18.setBeginLackCpCountRange("-");
        lackWorkerDtoByIndustry18.setNowLackWorkerCount(nowLackWorkerCountByIndustry1+nowLackWorkerCountByIndustry2+nowLackWorkerCountByIndustry3+nowLackWorkerCountByIndustry4+nowLackWorkerCountByIndustry5+nowLackWorkerCountByIndustry6+nowLackWorkerCountByIndustry7+nowLackWorkerCountByIndustry8+nowLackWorkerCountByIndustry9+nowLackWorkerCountByIndustry10+nowLackWorkerCountByIndustry11+nowLackWorkerCountByIndustry12+nowLackWorkerCountByIndustry13+nowLackWorkerCountByIndustry14+nowLackWorkerCountByIndustry15+nowLackWorkerCountByIndustry16+nowLackWorkerCountByIndustry17);
        lackWorkerDtoByIndustry18.setPreLackWorkerCount(preLackWorkerCountByIndustry1+preLackWorkerCountByIndustry2+preLackWorkerCountByIndustry3+preLackWorkerCountByIndustry4+preLackWorkerCountByIndustry5+preLackWorkerCountByIndustry6+preLackWorkerCountByIndustry7+preLackWorkerCountByIndustry8+preLackWorkerCountByIndustry9+preLackWorkerCountByIndustry10+preLackWorkerCountByIndustry11+preLackWorkerCountByIndustry12+preLackWorkerCountByIndustry13+preLackWorkerCountByIndustry14+preLackWorkerCountByIndustry15+preLackWorkerCountByIndustry16+preLackWorkerCountByIndustry17);
        lackWorkerDtoByIndustry18.setBeginLackWorkerCount(beginLackWorkerCountByIndustry1+beginLackWorkerCountByIndustry2+beginLackWorkerCountByIndustry3+beginLackWorkerCountByIndustry4+beginLackWorkerCountByIndustry5+beginLackWorkerCountByIndustry6+beginLackWorkerCountByIndustry7+beginLackWorkerCountByIndustry8+beginLackWorkerCountByIndustry9+beginLackWorkerCountByIndustry10+beginLackWorkerCountByIndustry11+beginLackWorkerCountByIndustry12+beginLackWorkerCountByIndustry13+beginLackWorkerCountByIndustry14+beginLackWorkerCountByIndustry15+beginLackWorkerCountByIndustry16+beginLackWorkerCountByIndustry17);

        //构造按行业业数据
        String category13 = "按地区";
        LackWorkerDto lackWorkerDtoByRegion1 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion2 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion3 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion4 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion5 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion6 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion7 = new LackWorkerDto();
        LackWorkerDto lackWorkerDtoByRegion8 = new LackWorkerDto();

        lackWorkerDtoByRegion1.setCategory1(category13);
        lackWorkerDtoByRegion1.setCategory2("广安区");
        lackWorkerDtoByRegion1.setCpCount(cpCountByRegion1);
        lackWorkerDtoByRegion1.setNowLackCpCount(nowLackCpCountByRegion1);
        lackWorkerDtoByRegion1.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion1 * 100.00D / cpCountByRegion1) + "%");
        lackWorkerDtoByRegion1.setPreLackCpCount(preLackCpCountByRegion1);
        lackWorkerDtoByRegion1.setPreLackCpCountRange(Math.round((preLackCpCountByRegion1) * 100.00D / cpCountByRegion1) + "%");
        lackWorkerDtoByRegion1.setBeginLackCpCount(beginLackCpCountByRegion1);
        lackWorkerDtoByRegion1.setBeginLackCpCountRange(Math.round(beginLackCpCountByRegion1 * 100.00D / cpCountByRegion1) + "%");
        lackWorkerDtoByRegion1.setNowLackWorkerCount(nowLackWorkerCountByRegion1);
        lackWorkerDtoByRegion1.setPreLackWorkerCount(preLackWorkerCountByRegion1);
        lackWorkerDtoByRegion1.setBeginLackWorkerCount(beginLackWorkerCountByRegion1);

        lackWorkerDtoByRegion2.setCategory1(category13);
        lackWorkerDtoByRegion2.setCategory2("前锋区");
        lackWorkerDtoByRegion2.setCpCount(cpCountByRegion2);
        lackWorkerDtoByRegion2.setNowLackCpCount(nowLackCpCountByRegion2);
        lackWorkerDtoByRegion2.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion2 * 100.00D / cpCountByRegion2) + "%");
        lackWorkerDtoByRegion2.setPreLackCpCount(preLackCpCountByRegion2);
        lackWorkerDtoByRegion2.setPreLackCpCountRange(Math.round((preLackCpCountByRegion2) * 100.00D / cpCountByRegion2) + "%");
        lackWorkerDtoByRegion2.setBeginLackCpCount(beginLackCpCountByRegion2);
        lackWorkerDtoByRegion2.setBeginLackCpCountRange(Math.round(beginLackCpCountByRegion2 * 100.00D / cpCountByRegion2) + "%");
        lackWorkerDtoByRegion2.setNowLackWorkerCount(nowLackWorkerCountByRegion2);
        lackWorkerDtoByRegion2.setPreLackWorkerCount(preLackWorkerCountByRegion2);
        lackWorkerDtoByRegion2.setBeginLackWorkerCount(beginLackWorkerCountByRegion2);

        lackWorkerDtoByRegion3.setCategory1(category13);
        lackWorkerDtoByRegion3.setCategory2("岳池县");
        lackWorkerDtoByRegion3.setCpCount(cpCountByRegion3);
        lackWorkerDtoByRegion3.setNowLackCpCount(nowLackCpCountByRegion3);
        lackWorkerDtoByRegion3.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion3 * 100.00D / cpCountByRegion3) + "%");
        lackWorkerDtoByRegion3.setPreLackCpCount(preLackCpCountByRegion3);
        lackWorkerDtoByRegion3.setPreLackCpCountRange(Math.round((preLackCpCountByRegion3) * 100.00D / cpCountByRegion3) + "%");
        lackWorkerDtoByRegion3.setBeginLackCpCount(beginLackCpCountByRegion3);
        lackWorkerDtoByRegion3.setBeginLackCpCountRange(Math.round((beginLackCpCountByRegion3) * 100.00D / cpCountByRegion3) + "%");
        lackWorkerDtoByRegion3.setNowLackWorkerCount(nowLackWorkerCountByRegion3);
        lackWorkerDtoByRegion3.setPreLackWorkerCount(preLackWorkerCountByRegion3);
        lackWorkerDtoByRegion3.setBeginLackWorkerCount(beginLackWorkerCountByRegion3);

        lackWorkerDtoByRegion4.setCategory1(category13);
        lackWorkerDtoByRegion4.setCategory2("武胜县");
        lackWorkerDtoByRegion4.setCpCount(cpCountByRegion4);
        lackWorkerDtoByRegion4.setNowLackCpCount(nowLackCpCountByRegion4);
        lackWorkerDtoByRegion4.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion4 * 100.00D / cpCountByRegion4) + "%");
        lackWorkerDtoByRegion4.setPreLackCpCount(preLackCpCountByRegion1);
        lackWorkerDtoByRegion4.setPreLackCpCountRange(Math.round((preLackCpCountByRegion4) * 100.00D / cpCountByRegion4) + "%");
        lackWorkerDtoByRegion4.setBeginLackCpCount(beginLackCpCountByRegion4);
        lackWorkerDtoByRegion4.setBeginLackCpCountRange(Math.round((beginLackCpCountByRegion4) * 100.00D / cpCountByRegion4) + "%");
        lackWorkerDtoByRegion4.setNowLackWorkerCount(nowLackWorkerCountByRegion4);
        lackWorkerDtoByRegion4.setPreLackWorkerCount(preLackWorkerCountByRegion4);
        lackWorkerDtoByRegion4.setBeginLackWorkerCount(beginLackWorkerCountByRegion4);

        lackWorkerDtoByRegion5.setCategory1(category13);
        lackWorkerDtoByRegion5.setCategory2("邻水县");
        lackWorkerDtoByRegion5.setCpCount(cpCountByRegion5);
        lackWorkerDtoByRegion5.setNowLackCpCount(nowLackCpCountByRegion5);
        lackWorkerDtoByRegion5.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion5 * 100.00D / cpCountByRegion5) + "%");
        lackWorkerDtoByRegion5.setPreLackCpCount(preLackCpCountByRegion5);
        lackWorkerDtoByRegion5.setPreLackCpCountRange(Math.round((preLackCpCountByRegion5) * 100.00D / cpCountByRegion5) + "%");
        lackWorkerDtoByRegion5.setBeginLackCpCount(beginLackCpCountByRegion5);
        lackWorkerDtoByRegion5.setBeginLackCpCountRange(Math.round((beginLackCpCountByRegion5) * 100.00D / cpCountByRegion5) + "%");
        lackWorkerDtoByRegion5.setNowLackWorkerCount(nowLackWorkerCountByRegion5);
        lackWorkerDtoByRegion5.setPreLackWorkerCount(preLackWorkerCountByRegion5);
        lackWorkerDtoByRegion5.setBeginLackWorkerCount(beginLackWorkerCountByRegion5);

        lackWorkerDtoByRegion6.setCategory1(category13);
        lackWorkerDtoByRegion6.setCategory2("华蓥市");
        lackWorkerDtoByRegion6.setCpCount(cpCountByRegion6);
        lackWorkerDtoByRegion6.setNowLackCpCount(nowLackCpCountByRegion6);
        lackWorkerDtoByRegion6.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion6 * 100.00D / cpCountByRegion6) + "%");
        lackWorkerDtoByRegion6.setPreLackCpCount(preLackCpCountByRegion6);
        lackWorkerDtoByRegion6.setPreLackCpCountRange(Math.round((preLackCpCountByRegion6) * 100.00D / cpCountByRegion6) + "%");
        lackWorkerDtoByRegion6.setBeginLackCpCount(beginLackCpCountByRegion6);
        lackWorkerDtoByRegion6.setBeginLackCpCountRange(Math.round((beginLackCpCountByRegion6) * 100.00D / cpCountByRegion6) + "%");
        lackWorkerDtoByRegion6.setNowLackWorkerCount(nowLackWorkerCountByRegion6);
        lackWorkerDtoByRegion6.setPreLackWorkerCount(preLackWorkerCountByRegion6);
        lackWorkerDtoByRegion6.setBeginLackWorkerCount(beginLackWorkerCountByRegion6);

        lackWorkerDtoByRegion7.setCategory1(category13);
        lackWorkerDtoByRegion7.setCategory2("经开区");
        lackWorkerDtoByRegion7.setCpCount(cpCountByRegion7);
        lackWorkerDtoByRegion7.setNowLackCpCount(nowLackCpCountByRegion7);
        lackWorkerDtoByRegion7.setNowLackCpCountRange(Math.round(nowLackCpCountByRegion7 * 100.00D / cpCountByRegion7) + "%");
        lackWorkerDtoByRegion7.setPreLackCpCount(preLackCpCountByRegion7);
        lackWorkerDtoByRegion7.setPreLackCpCountRange(Math.round((preLackCpCountByRegion7) * 100.00D / cpCountByRegion7) + "%");
        lackWorkerDtoByRegion7.setBeginLackCpCount(beginLackCpCountByRegion7);
        lackWorkerDtoByRegion7.setBeginLackCpCountRange(Math.round((beginLackCpCountByRegion7) * 100.00D / cpCountByRegion7) + "%");
        lackWorkerDtoByRegion7.setNowLackWorkerCount(nowLackWorkerCountByRegion7);
        lackWorkerDtoByRegion7.setPreLackWorkerCount(preLackWorkerCountByRegion7);
        lackWorkerDtoByRegion7.setBeginLackWorkerCount(beginLackWorkerCountByRegion7);

        lackWorkerDtoByRegion8.setCategory1(category13);
        lackWorkerDtoByRegion8.setCategory2("总计");
        lackWorkerDtoByRegion8.setCpCount(cpCountByRegion1 + cpCountByRegion2 + cpCountByRegion3 + cpCountByRegion4 + cpCountByRegion5 + cpCountByRegion6 + cpCountByRegion7);
        lackWorkerDtoByRegion8.setNowLackCpCount(nowLackCpCountByRegion1 + nowLackCpCountByRegion2 + nowLackCpCountByRegion3 + nowLackCpCountByRegion4 + nowLackCpCountByRegion5 + nowLackCpCountByRegion6 + nowLackCpCountByRegion7);
        lackWorkerDtoByRegion8.setNowLackCpCountRange("-");
        lackWorkerDtoByRegion8.setPreLackCpCount(preLackCpCountByRegion1 + preLackCpCountByRegion2 + preLackCpCountByRegion3 + preLackCpCountByRegion4 + preLackCpCountByRegion5 + preLackCpCountByRegion6 + preLackCpCountByRegion7);
        lackWorkerDtoByRegion8.setPreLackCpCountRange("-");
        lackWorkerDtoByRegion8.setBeginLackCpCount(beginLackCpCountByRegion1 + beginLackCpCountByRegion2 + beginLackCpCountByRegion3 + beginLackCpCountByRegion4 + beginLackCpCountByRegion5 + beginLackCpCountByRegion6 + beginLackCpCountByRegion7);
        lackWorkerDtoByRegion8.setBeginLackCpCountRange("-");
        lackWorkerDtoByRegion8.setNowLackWorkerCount(nowLackWorkerCountByRegion1+nowLackWorkerCountByRegion2+nowLackWorkerCountByRegion3+nowLackWorkerCountByRegion4+nowLackWorkerCountByRegion5+nowLackWorkerCountByRegion6+nowLackWorkerCountByRegion7);
        lackWorkerDtoByRegion8.setPreLackWorkerCount(preLackWorkerCountByRegion1+preLackWorkerCountByRegion2+preLackWorkerCountByRegion3+preLackWorkerCountByRegion4+preLackWorkerCountByRegion5+preLackWorkerCountByRegion6+preLackWorkerCountByRegion7);
        lackWorkerDtoByRegion8.setBeginLackWorkerCount(beginLackWorkerCountByRegion1+beginLackWorkerCountByRegion2+beginLackWorkerCountByRegion3+beginLackWorkerCountByRegion4+beginLackWorkerCountByRegion5+beginLackWorkerCountByRegion6+beginLackWorkerCountByRegion7);

        LackWorkerDto lackWorkerDto = new LackWorkerDto();
        List list = new ArrayList();
        list.add(lackWorkerDtoByEstate1);
        list.add(lackWorkerDtoByEstate2);
        list.add(lackWorkerDtoByEstate3);
        list.add(lackWorkerDtoByEstate4);
        list.add(lackWorkerDtoByIndustry1);
        list.add(lackWorkerDtoByIndustry2);
        list.add(lackWorkerDtoByIndustry3);
        list.add(lackWorkerDtoByIndustry4);
        list.add(lackWorkerDtoByIndustry5);
        list.add(lackWorkerDtoByIndustry6);
        list.add(lackWorkerDtoByIndustry7);
        list.add(lackWorkerDtoByIndustry8);
        list.add(lackWorkerDtoByIndustry9);
        list.add(lackWorkerDtoByIndustry10);
        list.add(lackWorkerDtoByIndustry11);
        list.add(lackWorkerDtoByIndustry12);
        list.add(lackWorkerDtoByIndustry13);
        list.add(lackWorkerDtoByIndustry14);
        list.add(lackWorkerDtoByIndustry15);
        list.add(lackWorkerDtoByIndustry16);
        list.add(lackWorkerDtoByIndustry17);
        list.add(lackWorkerDtoByIndustry18);
        list.add(lackWorkerDtoByRegion1);
        list.add(lackWorkerDtoByRegion2);
        list.add(lackWorkerDtoByRegion3);
        list.add(lackWorkerDtoByRegion4);
        list.add(lackWorkerDtoByRegion5);
        list.add(lackWorkerDtoByRegion6);
        list.add(lackWorkerDtoByRegion7);
        list.add(lackWorkerDtoByRegion8);
        lackWorkerDto.setList(list);
        Result<LackWorkerDto> result = new Result<>();
        result.ok(lackWorkerDto);
        return result;

    }

    @Override
    public Result<ByYearMonthDto> byYearMonth(Map<String, Object> params) {
        //查询当月情况
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        Object year = params.get("year");
        String ym = null;
        if (year != null && !"".equals(year)) {
            ym = year.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy");
        }
        String lastYearMonth = (Long.valueOf(ym) - 1) + "-12";
        params.put("lastYearMonth", lastYearMonth);
        List<YearMonthDto> thisYearList = baseDao.selectYearMonthData(params);
        //月份变量
        ByYearMonthDto byYearMonthDto = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto1 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto2 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto3 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto4 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto5 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto6 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto7 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto8 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto9 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto10 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto11 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto12 = new ByYearMonthDto();
        ByYearMonthDto byYearMonthDto13 = new ByYearMonthDto();
        //年初用工数量
        Long beginWorkerCount = 0L;
        //上期用工数量
        Long preWorkerCount1 = 0L;
        Long preWorkerCount2 = 0L;
        Long preWorkerCount3 = 0L;
        Long preWorkerCount4 = 0L;
        Long preWorkerCount5 = 0L;
        Long preWorkerCount6 = 0L;
        Long preWorkerCount7 = 0L;
        Long preWorkerCount8 = 0L;
        Long preWorkerCount9 = 0L;
        Long preWorkerCount10 = 0L;
        Long preWorkerCount11 = 0L;
        Long preWorkerCount12 = 0L;
        for (YearMonthDto item :
                thisYearList) {
            String thisYearMonth = item.getYearMonth();
            //设置年月和本期用工数量
            if (thisYearMonth.equals(year + "-01")) {
                byYearMonthDto1.setYearMonth(thisYearMonth);
                byYearMonthDto1.setNowWorkerCount(item.getAllUseWorker());
                beginWorkerCount = item.getAllUseWorker();
                preWorkerCount2 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-02")) {
                byYearMonthDto2.setYearMonth(thisYearMonth);
                byYearMonthDto2.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount3 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-03")) {
                byYearMonthDto3.setYearMonth(thisYearMonth);
                byYearMonthDto3.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount4 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-04")) {
                byYearMonthDto4.setYearMonth(thisYearMonth);
                byYearMonthDto4.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount5 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-05")) {
                byYearMonthDto5.setYearMonth(thisYearMonth);
                byYearMonthDto5.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount6 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-06")) {
                byYearMonthDto6.setYearMonth(thisYearMonth);
                byYearMonthDto6.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount7 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-07")) {
                byYearMonthDto7.setYearMonth(thisYearMonth);
                byYearMonthDto7.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount8 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-08")) {
                byYearMonthDto8.setYearMonth(thisYearMonth);
                byYearMonthDto8.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount9 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-09")) {
                byYearMonthDto9.setYearMonth(thisYearMonth);
                byYearMonthDto9.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount10 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-10")) {
                byYearMonthDto10.setYearMonth(thisYearMonth);
                byYearMonthDto10.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount11 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-11")) {
                byYearMonthDto11.setYearMonth(thisYearMonth);
                byYearMonthDto11.setNowWorkerCount(item.getAllUseWorker());
                preWorkerCount12 = item.getAllUseWorker();
            } else if (thisYearMonth.equals(year + "-12")) {
                byYearMonthDto12.setYearMonth(thisYearMonth);
                byYearMonthDto12.setNowWorkerCount(item.getAllUseWorker());
            } else if (thisYearMonth.equals(lastYearMonth)) {
                preWorkerCount1 = item.getAllUseWorker();
            }

        }
        for (YearMonthDto item :
                thisYearList) {
            String thisYearMonth = item.getYearMonth();
            //设置上期用工数量、差值、幅度和年初数量、差值、幅度
            if (thisYearMonth.equals(year + "-01")) {
                byYearMonthDto1.setPreWorkerCount(preWorkerCount1);
                byYearMonthDto1.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount1);
                byYearMonthDto1.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount1) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto1.setChangeWorkerCountCompareBeginYear(0L);
                byYearMonthDto1.setChangeWorkerRangeCompareBeginYear("0.0%");
            } else if (thisYearMonth.equals(year + "-02")) {
                byYearMonthDto2.setPreWorkerCount(preWorkerCount2);
                byYearMonthDto2.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount2);
                byYearMonthDto2.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount2) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto2.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto2.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-03")) {
                byYearMonthDto3.setPreWorkerCount(preWorkerCount3);
                byYearMonthDto3.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount3);
                byYearMonthDto3.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount3) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto3.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto3.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-04")) {
                byYearMonthDto4.setPreWorkerCount(preWorkerCount4);
                byYearMonthDto4.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount4);
                byYearMonthDto4.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount4) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto4.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto4.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-05")) {
                byYearMonthDto5.setPreWorkerCount(preWorkerCount5);
                byYearMonthDto5.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount5);
                byYearMonthDto5.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount5) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto5.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto5.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-06")) {
                byYearMonthDto6.setPreWorkerCount(preWorkerCount6);
                byYearMonthDto6.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount6);
                byYearMonthDto6.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount6) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto6.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto6.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-07")) {
                byYearMonthDto7.setPreWorkerCount(preWorkerCount7);
                byYearMonthDto7.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount7);
                byYearMonthDto7.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount7) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto7.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto7.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-08")) {
                byYearMonthDto8.setPreWorkerCount(preWorkerCount8);
                byYearMonthDto8.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount8);
                byYearMonthDto8.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount8) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto8.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto8.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-09")) {
                byYearMonthDto9.setPreWorkerCount(preWorkerCount9);
                byYearMonthDto9.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount9);
                byYearMonthDto9.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount9) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto9.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto9.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-10")) {
                byYearMonthDto10.setPreWorkerCount(preWorkerCount10);
                byYearMonthDto10.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount10);
                byYearMonthDto10.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount10) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto10.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto10.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-11")) {
                byYearMonthDto11.setPreWorkerCount(preWorkerCount11);
                byYearMonthDto11.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount11);
                byYearMonthDto11.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount11) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto11.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto11.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            } else if (thisYearMonth.equals(year + "-12")) {
                byYearMonthDto12.setPreWorkerCount(preWorkerCount12);
                byYearMonthDto12.setChangeWorkerCount(item.getAllUseWorker() - preWorkerCount12);
                byYearMonthDto12.setChangeWorkerRange(Math.round((item.getAllUseWorker() - preWorkerCount12) * 100.00D / item.getAllUseWorker()) + "%");
                byYearMonthDto12.setChangeWorkerCountCompareBeginYear(item.getAllUseWorker() - beginWorkerCount);
                byYearMonthDto12.setChangeWorkerRangeCompareBeginYear(Math.round((item.getAllUseWorker() - beginWorkerCount) * 100.00D / item.getAllUseWorker()) + "%");
            }

        }
        //构造数据
        byYearMonthDto1.setYearMonth(year + "-01");
        byYearMonthDto2.setYearMonth(year + "-02");
        byYearMonthDto3.setYearMonth(year + "-03");
        byYearMonthDto4.setYearMonth(year + "-04");
        byYearMonthDto5.setYearMonth(year + "-05");
        byYearMonthDto6.setYearMonth(year + "-06");
        byYearMonthDto7.setYearMonth(year + "-07");
        byYearMonthDto8.setYearMonth(year + "-08");
        byYearMonthDto9.setYearMonth(year + "-09");
        byYearMonthDto10.setYearMonth(year + "-10");
        byYearMonthDto11.setYearMonth(year + "-11");
        byYearMonthDto12.setYearMonth(year + "-12");
        byYearMonthDto13.setYearMonth("总计");
        byYearMonthDto13.setPreWorkerCount(preWorkerCount1 + preWorkerCount2 + preWorkerCount3 + preWorkerCount4 + preWorkerCount5 + preWorkerCount6 + preWorkerCount7 + preWorkerCount8 + preWorkerCount9 + preWorkerCount10 + preWorkerCount11 + preWorkerCount12);
        byYearMonthDto13.setNowWorkerCount(byYearMonthDto1.getNowWorkerCount() == null ? 0L : byYearMonthDto1.getNowWorkerCount()
                + (byYearMonthDto2.getNowWorkerCount() == null ? 0L : byYearMonthDto2.getNowWorkerCount())
                + (byYearMonthDto3.getNowWorkerCount() == null ? 0L : byYearMonthDto3.getNowWorkerCount())
                + (byYearMonthDto4.getNowWorkerCount() == null ? 0L : byYearMonthDto4.getNowWorkerCount())
                + (byYearMonthDto5.getNowWorkerCount() == null ? 0L : byYearMonthDto5.getNowWorkerCount())
                + (byYearMonthDto6.getNowWorkerCount() == null ? 0L : byYearMonthDto6.getNowWorkerCount())
                + (byYearMonthDto7.getNowWorkerCount() == null ? 0L : byYearMonthDto7.getNowWorkerCount())
                + (byYearMonthDto8.getNowWorkerCount() == null ? 0L : byYearMonthDto8.getNowWorkerCount())
                + (byYearMonthDto9.getNowWorkerCount() == null ? 0L : byYearMonthDto9.getNowWorkerCount())
                + (byYearMonthDto10.getNowWorkerCount() == null ? 0L : byYearMonthDto10.getNowWorkerCount())
                + (byYearMonthDto11.getNowWorkerCount() == null ? 0L : byYearMonthDto11.getNowWorkerCount())
                + (byYearMonthDto12.getNowWorkerCount() == null ? 0L : byYearMonthDto12.getNowWorkerCount()));
        byYearMonthDto13.setChangeWorkerCount(byYearMonthDto13.getNowWorkerCount() - byYearMonthDto13.getPreWorkerCount());
        byYearMonthDto13.setChangeWorkerRange(byYearMonthDto13.getNowWorkerCount() == 0L ? "0%" : Math.round((byYearMonthDto13.getNowWorkerCount() - byYearMonthDto13.getPreWorkerCount()) * 100.00D / byYearMonthDto13.getNowWorkerCount()) + "%");
        byYearMonthDto13.setBeginWorkerCount(byYearMonthDto1.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount()
                + (byYearMonthDto2.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto3.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto4.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto5.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto6.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto7.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto8.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto9.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto10.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto11.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount())
                + (byYearMonthDto12.getBeginWorkerCount() == null ? 0L : byYearMonthDto1.getBeginWorkerCount()));
        byYearMonthDto13.setChangeWorkerCountCompareBeginYear(byYearMonthDto13.getNowWorkerCount() - byYearMonthDto13.getBeginWorkerCount());
        byYearMonthDto13.setChangeWorkerRangeCompareBeginYear(byYearMonthDto13.getNowWorkerCount() == 0L ? "0%" : Math.round((byYearMonthDto13.getNowWorkerCount() - byYearMonthDto13.getBeginWorkerCount()) * 100.00D / byYearMonthDto13.getNowWorkerCount()) + "%");
        List<ByYearMonthDto> list = new ArrayList<>();
        list.add(byYearMonthDto1);
        list.add(byYearMonthDto2);
        list.add(byYearMonthDto3);
        list.add(byYearMonthDto4);
        list.add(byYearMonthDto5);
        list.add(byYearMonthDto6);
        list.add(byYearMonthDto7);
        list.add(byYearMonthDto8);
        list.add(byYearMonthDto9);
        list.add(byYearMonthDto10);
        list.add(byYearMonthDto11);
        list.add(byYearMonthDto12);
        list.add(byYearMonthDto13);
        byYearMonthDto.setList(list);
        Result<ByYearMonthDto> result = new Result<>();
        result.ok(byYearMonthDto);
        return result;
    }

    @Override
    public Result canAdd() {
        int dayOfMonth = DateUtil.dayOfMonth(DateUtil.date());
        BCp03ConfigEntity bCp03ConfigEntity = bCp03ConfigService.selectById(1);
        String startday = bCp03ConfigEntity.getStartday();
        String endday = bCp03ConfigEntity.getEndday();
        Result result = new Result();
        if (dayOfMonth >= Integer.valueOf(startday) && dayOfMonth <= Integer.valueOf(endday)) {
            result.ok("");
        } else {
            result.error("请在" + startday + "到" + endday + "之间的日期填报！");
        }
        return result;
    }

    @Override
    public Result<ByParkDto> byPark(Map<String, Object> params) {
        //查询当月情况
        UserDetail user = SecurityUser.getUser();
        Long deptId = user.getDeptId();
        params.put("deptId", deptId.toString());
        Object yearMonth = params.get("yearMonth");
        String ym = null;
        if (yearMonth != null && !"".equals(yearMonth)) {
            ym = yearMonth.toString();
        } else {
            ym = DateUtil.format(new Date(), "yyyy-MM");
        }
        params.put("ym", ym);
        List<BCp03DTO> thisMonth = baseDao.getThisMonthData(params);
        //查询上月情况
        Date newDate = DateUtil.offset(DateUtil.parse(ym + "-01"), DateField.DAY_OF_MONTH, -1);
        String preYm = DateUtil.format(newDate, "yyyy-MM");
        params.put("ym", preYm);
        List<BCp03DTO> preMonth = baseDao.getThisMonthData(params);
        //查询当年第一月的情况
        String beginYm = ym.substring(0, 4) + "-01";
        params.put("ym", beginYm);
        List<BCp03DTO> beginMonth = baseDao.getThisMonthData(params);
        //定义各行业监测企业数
        Long cpCount1 = 0L;
        Long cpCount2 = 0L;
        Long cpCount3 = 0L;
        Long cpCount4 = 0L;
        Long cpCount5 = 0L;
        Long cpCount6 = 0L;
        Long cpCount7 = 0L;
        Long cpCount8 = 0L;
        Long cpCount9 = 0L;
        Long cpCount10 = 0L;
        //定义上月用工人数
        Long preWorkerCount1 = 0L;
        Long preWorkerCount2 = 0L;
        Long preWorkerCount3 = 0L;
        Long preWorkerCount4 = 0L;
        Long preWorkerCount5 = 0L;
        Long preWorkerCount6 = 0L;
        Long preWorkerCount7 = 0L;
        Long preWorkerCount8 = 0L;
        Long preWorkerCount9 = 0L;
        Long preWorkerCount10 = 0L;
        //定义本月用工人数
        Long nowWorkerCount1 = 0L;
        Long nowWorkerCount2 = 0L;
        Long nowWorkerCount3 = 0L;
        Long nowWorkerCount4 = 0L;
        Long nowWorkerCount5 = 0L;
        Long nowWorkerCount6 = 0L;
        Long nowWorkerCount7 = 0L;
        Long nowWorkerCount8 = 0L;
        Long nowWorkerCount9 = 0L;
        Long nowWorkerCount10 = 0L;
        //定义年初用工人数
        Long beginWorkerCount1 = 0L;
        Long beginWorkerCount2 = 0L;
        Long beginWorkerCount3 = 0L;
        Long beginWorkerCount4 = 0L;
        Long beginWorkerCount5 = 0L;
        Long beginWorkerCount6 = 0L;
        Long beginWorkerCount7 = 0L;
        Long beginWorkerCount8 = 0L;
        Long beginWorkerCount9 = 0L;
        Long beginWorkerCount10 = 0L;
        for (BCp03DTO thisItem :
                thisMonth) {
            String thisItemAreacode = thisItem.getAreacode();
            //计算监测企业数,计算当月总工人数
            switch (thisItemAreacode) {
                case "51160298"://广安区官盛工业园区
                    cpCount1++;
                    nowWorkerCount1 += thisItem.getAllUseWorker();
                    break;
                case "51160398"://前锋工业园区
                    cpCount2++;
                    nowWorkerCount2 += thisItem.getAllUseWorker();
                    break;
                case "51162198"://岳池县经济技术开发区
                    cpCount3++;
                    nowWorkerCount3 += thisItem.getAllUseWorker();
                    break;
                case "51162298"://四川武胜经济开发区
                    cpCount4++;
                    nowWorkerCount4 += thisItem.getAllUseWorker();
                    break;
                case "51162398"://川渝高竹新区
                    cpCount5++;
                    nowWorkerCount5 += thisItem.getAllUseWorker();
                    break;
                case "51162397"://广安高新区（邻水）
                    cpCount6++;
                    nowWorkerCount6 += thisItem.getAllUseWorker();
                    break;
                case "51168198"://华蓥山经济开发区
                    cpCount7++;
                    nowWorkerCount7 += thisItem.getAllUseWorker();
                    break;
                case "51169998"://奎阁工业园区
                    cpCount8++;
                    nowWorkerCount8 += thisItem.getAllUseWorker();
                    break;
                case "51169997"://新桥工业园区
                    cpCount9++;
                    nowWorkerCount9 += thisItem.getAllUseWorker();
                    break;
            }


        }
        //上月循环
        for (BCp03DTO preItem :
                preMonth) {
            String preItemAreacode = preItem.getAreacode();
            //计算上月总工人数
            switch (preItemAreacode) {
                case "51160298"://广安区官盛工业园区
                    preWorkerCount1 += preItem.getAllUseWorker();
                    break;
                case "51160398"://前锋工业园区
                    preWorkerCount2 += preItem.getAllUseWorker();
                    break;
                case "51162198"://岳池县经济技术开发区
                    preWorkerCount3 += preItem.getAllUseWorker();
                    break;
                case "51162298"://四川武胜经济开发区
                    preWorkerCount4 += preItem.getAllUseWorker();
                    break;
                case "51162398"://川渝高竹新区
                    preWorkerCount5 += preItem.getAllUseWorker();
                    break;
                case "51162397"://广安高新区（邻水）
                    preWorkerCount6 += preItem.getAllUseWorker();
                    break;
                case "51168198"://华蓥山经济开发区
                    preWorkerCount7 += preItem.getAllUseWorker();
                    break;
                case "51169998"://奎阁工业园区
                    preWorkerCount8 += preItem.getAllUseWorker();
                    break;
                case "51169997"://新桥工业园区
                    preWorkerCount9 += preItem.getAllUseWorker();
                    break;
            }
        }
        //年初循环
        for (BCp03DTO beginItem :
                beginMonth) {
            String beginItemAreacode = beginItem.getAreacode();
            //计算年初总工人数
            switch (beginItemAreacode) {
                case "51160298"://广安区官盛工业园区
                    beginWorkerCount1 += beginItem.getAllUseWorker();
                    break;
                case "51160398"://前锋工业园区
                    beginWorkerCount2 += beginItem.getAllUseWorker();
                    break;
                case "51162198"://岳池县经济技术开发区
                    beginWorkerCount3 += beginItem.getAllUseWorker();
                    break;
                case "51162298"://四川武胜经济开发区
                    beginWorkerCount4 += beginItem.getAllUseWorker();
                    break;
                case "51162398"://川渝高竹新区
                    beginWorkerCount5 += beginItem.getAllUseWorker();
                    break;
                case "51162397"://广安高新区（邻水）
                    beginWorkerCount6 += beginItem.getAllUseWorker();
                    break;
                case "51168198"://华蓥山经济开发区
                    beginWorkerCount7 += beginItem.getAllUseWorker();
                    break;
                case "51169998"://奎阁工业园区
                    beginWorkerCount8 += beginItem.getAllUseWorker();
                    break;
                case "51169997"://新桥工业园区
                    beginWorkerCount9 += beginItem.getAllUseWorker();
                    break;
            }
        }
        //构造数据
        ByParkDto byParkDto1 = new ByParkDto();
        ByParkDto byParkDto2 = new ByParkDto();
        ByParkDto byParkDto3 = new ByParkDto();
        ByParkDto byParkDto4 = new ByParkDto();
        ByParkDto byParkDto5 = new ByParkDto();
        ByParkDto byParkDto6 = new ByParkDto();
        ByParkDto byParkDto7 = new ByParkDto();
        ByParkDto byParkDto8 = new ByParkDto();
        ByParkDto byParkDto9 = new ByParkDto();
        ByParkDto byParkDto10 = new ByParkDto();
        byParkDto1.setPark("广安区官盛工业园区");
        byParkDto2.setPark("前锋工业园区");
        byParkDto3.setPark("岳池县经济技术开发区");
        byParkDto4.setPark("四川武胜经济开发区");
        byParkDto5.setPark("川渝高竹新区");
        byParkDto6.setPark("广安高新区（邻水）");
        byParkDto7.setPark("华蓥山经济开发区");
        byParkDto8.setPark("奎阁工业园区");
        byParkDto9.setPark("新桥工业园区");
        byParkDto10.setPark("总计");
        byParkDto1.setCpCount(cpCount1);
        byParkDto2.setCpCount(cpCount2);
        byParkDto3.setCpCount(cpCount3);
        byParkDto4.setCpCount(cpCount4);
        byParkDto5.setCpCount(cpCount5);
        byParkDto6.setCpCount(cpCount6);
        byParkDto7.setCpCount(cpCount7);
        byParkDto8.setCpCount(cpCount8);
        byParkDto9.setCpCount(cpCount9);
        byParkDto10.setCpCount(cpCount1 + cpCount2 + cpCount3 + cpCount4 + cpCount5 + cpCount6 + cpCount7 + cpCount8 + cpCount9);
        byParkDto1.setNowWorkerCount(nowWorkerCount1);
        byParkDto2.setNowWorkerCount(nowWorkerCount2);
        byParkDto3.setNowWorkerCount(nowWorkerCount3);
        byParkDto4.setNowWorkerCount(nowWorkerCount4);
        byParkDto5.setNowWorkerCount(nowWorkerCount5);
        byParkDto6.setNowWorkerCount(nowWorkerCount6);
        byParkDto7.setNowWorkerCount(nowWorkerCount7);
        byParkDto8.setNowWorkerCount(nowWorkerCount8);
        byParkDto9.setNowWorkerCount(nowWorkerCount9);
        byParkDto10.setNowWorkerCount(nowWorkerCount1 + nowWorkerCount2 + nowWorkerCount3 + nowWorkerCount4 + nowWorkerCount5 + nowWorkerCount6 + nowWorkerCount7 + nowWorkerCount8 + nowWorkerCount9);
        byParkDto1.setPreWorkerCount(preWorkerCount1);
        byParkDto2.setPreWorkerCount(preWorkerCount2);
        byParkDto3.setPreWorkerCount(preWorkerCount3);
        byParkDto4.setPreWorkerCount(preWorkerCount4);
        byParkDto5.setPreWorkerCount(preWorkerCount5);
        byParkDto6.setPreWorkerCount(preWorkerCount6);
        byParkDto7.setPreWorkerCount(preWorkerCount7);
        byParkDto8.setPreWorkerCount(preWorkerCount8);
        byParkDto9.setPreWorkerCount(preWorkerCount9);
        byParkDto10.setPreWorkerCount(preWorkerCount1 + preWorkerCount2 + preWorkerCount3 + preWorkerCount4 + preWorkerCount5 + preWorkerCount6 + preWorkerCount7 + preWorkerCount8 + preWorkerCount9);
        byParkDto1.setBeginWorkerCount(beginWorkerCount1);
        byParkDto2.setBeginWorkerCount(beginWorkerCount2);
        byParkDto3.setBeginWorkerCount(beginWorkerCount3);
        byParkDto4.setBeginWorkerCount(beginWorkerCount4);
        byParkDto5.setBeginWorkerCount(beginWorkerCount5);
        byParkDto6.setBeginWorkerCount(beginWorkerCount6);
        byParkDto7.setBeginWorkerCount(beginWorkerCount7);
        byParkDto8.setBeginWorkerCount(beginWorkerCount8);
        byParkDto9.setBeginWorkerCount(beginWorkerCount9);
        byParkDto10.setBeginWorkerCount(beginWorkerCount1 + beginWorkerCount2 + beginWorkerCount3 + beginWorkerCount4 + beginWorkerCount5 + beginWorkerCount6 + beginWorkerCount7 + beginWorkerCount8 + beginWorkerCount9);
        byParkDto1.setChangeWorkerCount(nowWorkerCount1 - preWorkerCount1);
        byParkDto2.setChangeWorkerCount(nowWorkerCount2 - preWorkerCount2);
        byParkDto3.setChangeWorkerCount(nowWorkerCount3 - preWorkerCount3);
        byParkDto4.setChangeWorkerCount(nowWorkerCount4 - preWorkerCount4);
        byParkDto5.setChangeWorkerCount(nowWorkerCount5 - preWorkerCount5);
        byParkDto6.setChangeWorkerCount(nowWorkerCount6 - preWorkerCount6);
        byParkDto7.setChangeWorkerCount(nowWorkerCount7 - preWorkerCount7);
        byParkDto8.setChangeWorkerCount(nowWorkerCount8 - preWorkerCount8);
        byParkDto9.setChangeWorkerCount(nowWorkerCount9 - preWorkerCount9);
        byParkDto10.setChangeWorkerCount(byParkDto1.getChangeWorkerCount() + byParkDto2.getChangeWorkerCount() + byParkDto3.getChangeWorkerCount() + byParkDto4.getChangeWorkerCount() + byParkDto5.getChangeWorkerCount() + byParkDto6.getChangeWorkerCount() + byParkDto7.getChangeWorkerCount() + byParkDto8.getChangeWorkerCount() + byParkDto9.getChangeWorkerCount());
        byParkDto1.setChangeWorkerCountCompareBeginYear(nowWorkerCount1 - beginWorkerCount1);
        byParkDto2.setChangeWorkerCountCompareBeginYear(nowWorkerCount2 - beginWorkerCount2);
        byParkDto3.setChangeWorkerCountCompareBeginYear(nowWorkerCount3 - beginWorkerCount3);
        byParkDto4.setChangeWorkerCountCompareBeginYear(nowWorkerCount4 - beginWorkerCount4);
        byParkDto5.setChangeWorkerCountCompareBeginYear(nowWorkerCount5 - beginWorkerCount5);
        byParkDto6.setChangeWorkerCountCompareBeginYear(nowWorkerCount6 - beginWorkerCount6);
        byParkDto7.setChangeWorkerCountCompareBeginYear(nowWorkerCount7 - beginWorkerCount7);
        byParkDto8.setChangeWorkerCountCompareBeginYear(nowWorkerCount8 - beginWorkerCount8);
        byParkDto9.setChangeWorkerCountCompareBeginYear(nowWorkerCount9 - beginWorkerCount9);
        byParkDto10.setChangeWorkerCountCompareBeginYear(byParkDto1.getBeginWorkerCount() + byParkDto2.getBeginWorkerCount() + byParkDto3.getBeginWorkerCount() + byParkDto4.getBeginWorkerCount() + byParkDto5.getBeginWorkerCount() + byParkDto6.getBeginWorkerCount() + byParkDto7.getBeginWorkerCount() + byParkDto8.getBeginWorkerCount() + byParkDto9.getBeginWorkerCount());
        byParkDto1.setChangeWorkerCountCompareBeginYear(nowWorkerCount1 - beginWorkerCount1);
        byParkDto2.setChangeWorkerCountCompareBeginYear(nowWorkerCount2 - beginWorkerCount2);
        byParkDto3.setChangeWorkerCountCompareBeginYear(nowWorkerCount3 - beginWorkerCount3);
        byParkDto4.setChangeWorkerCountCompareBeginYear(nowWorkerCount4 - beginWorkerCount4);
        byParkDto5.setChangeWorkerCountCompareBeginYear(nowWorkerCount5 - beginWorkerCount5);
        byParkDto6.setChangeWorkerCountCompareBeginYear(nowWorkerCount6 - beginWorkerCount6);
        byParkDto7.setChangeWorkerCountCompareBeginYear(nowWorkerCount7 - beginWorkerCount7);
        byParkDto8.setChangeWorkerCountCompareBeginYear(nowWorkerCount8 - beginWorkerCount8);
        byParkDto9.setChangeWorkerCountCompareBeginYear(nowWorkerCount9 - beginWorkerCount9);
        byParkDto10.setChangeWorkerCountCompareBeginYear(byParkDto1.getBeginWorkerCount() + byParkDto2.getBeginWorkerCount() + byParkDto3.getBeginWorkerCount() + byParkDto4.getBeginWorkerCount() + byParkDto5.getBeginWorkerCount() + byParkDto6.getBeginWorkerCount() + byParkDto7.getBeginWorkerCount() + byParkDto8.getBeginWorkerCount() + byParkDto9.getBeginWorkerCount());
        byParkDto1.setChangeWorkerRange(Math.round((nowWorkerCount1 - preWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byParkDto2.setChangeWorkerRange(Math.round((nowWorkerCount2 - preWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byParkDto3.setChangeWorkerRange(Math.round((nowWorkerCount3 - preWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byParkDto4.setChangeWorkerRange(Math.round((nowWorkerCount4 - preWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
        byParkDto5.setChangeWorkerRange(Math.round((nowWorkerCount5 - preWorkerCount5) * 100.00D / nowWorkerCount5) + "%");
        byParkDto6.setChangeWorkerRange(Math.round((nowWorkerCount6 - preWorkerCount6) * 100.00D / nowWorkerCount6) + "%");
        byParkDto7.setChangeWorkerRange(Math.round((nowWorkerCount7 - preWorkerCount7) * 100.00D / nowWorkerCount7) + "%");
        byParkDto8.setChangeWorkerRange(Math.round((nowWorkerCount8 - preWorkerCount8) * 100.00D / nowWorkerCount8) + "%");
        byParkDto9.setChangeWorkerRange(Math.round((nowWorkerCount9 - preWorkerCount9) * 100.00D / nowWorkerCount9) + "%");
        byParkDto1.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount1 - beginWorkerCount1) * 100.00D / nowWorkerCount1) + "%");
        byParkDto2.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount2 - beginWorkerCount2) * 100.00D / nowWorkerCount2) + "%");
        byParkDto3.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount3 - beginWorkerCount3) * 100.00D / nowWorkerCount3) + "%");
        byParkDto4.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount4 - beginWorkerCount4) * 100.00D / nowWorkerCount4) + "%");
        byParkDto5.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount5 - beginWorkerCount5) * 100.00D / nowWorkerCount5) + "%");
        byParkDto6.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount6 - beginWorkerCount6) * 100.00D / nowWorkerCount6) + "%");
        byParkDto7.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount7 - beginWorkerCount7) * 100.00D / nowWorkerCount7) + "%");
        byParkDto8.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount8 - beginWorkerCount8) * 100.00D / nowWorkerCount8) + "%");
        byParkDto9.setChangeWorkerRangeCompareBeginYear(Math.round((nowWorkerCount9 - beginWorkerCount9) * 100.00D / nowWorkerCount9) + "%");
        ByParkDto byParkDto = new ByParkDto();
        List<ByParkDto> list = new ArrayList<>();
        list.add(byParkDto1);
        list.add(byParkDto2);
        list.add(byParkDto3);
        list.add(byParkDto4);
        list.add(byParkDto5);
        list.add(byParkDto6);
        list.add(byParkDto7);
        list.add(byParkDto8);
        list.add(byParkDto9);
        byParkDto.setList(list);
        Result result = new Result();
        result.ok(byParkDto);
        return result;
    }


}