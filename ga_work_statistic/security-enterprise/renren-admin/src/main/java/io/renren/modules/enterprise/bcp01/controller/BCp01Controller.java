package io.renren.modules.enterprise.bcp01.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.CommonUtils;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.NumberUtil;
import io.renren.common.utils.Result;
import io.renren.common.utils.aliyun.AilynUtils;
import io.renren.common.utils.aliyun.CompanyDTO;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.modules.enterprise.bcp01.dao.BCp01Dao;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.entity.BCp01Entity;
import io.renren.modules.enterprise.bcp01.excel.BCp01Excel;
import io.renren.modules.enterprise.bcp01.service.BCp01Service;
import io.renren.modules.enterprise.bcp02.entity.BCp02Entity;
import io.renren.modules.enterprise.bot01.dto.BOt01DTO;
import io.renren.modules.enterprise.bot01.service.BOt01Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 企业基础信息
 *
 * @author JY
 * @since 1.0.0 2025-07-29
 */
@RestController
@RequestMapping("enterprise/cp01")
@Api(tags="企业基础信息")
public class BCp01Controller {
    @Autowired
    private BCp01Service bCp01Service;
    @Autowired
    private BOt01Service bOt01Service;
    @Autowired
    private BCp01Dao bCp01Dao;

    @GetMapping("page")
    @ApiOperation(value = "查询列表", notes = "列表字段：企业名称corpname、单位性质organizationtype、注册资本(万元)regcapital、联系人姓名linkman、联系人手机号码linkcellphone<br/>" +
            "查询条件：企业名称corpname、单位性质organizationtype、注册地区areacode")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("enterprise:cp01:page")
    public Result<PageData<BCp01DTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletRequest request){
        Long deptId = (Long) request.getSession().getAttribute("deptId");
        params.put("deptId",deptId);
        PageData<BCp01DTO> page = bCp01Service.pageList(params);
        return new Result<PageData<BCp01DTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("详情信息")
    @RequiresPermissions("enterprise:cp01:info")
    public Result<BCp01DTO> get(@PathVariable("id") Long id){
        BCp01DTO data = bCp01Service.get(id);
        List list = bOt01Service.listByMap(data.getCp0101());
        data.setOt01DTOList(list);
        return new Result<BCp01DTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("enterprise:cp01:save")
    public Result save(@RequestBody BCp01DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp01Service.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改企业信息——申请")
    @LogOperation("修改企业信息——申请")
    @RequiresPermissions("enterprise:cp01:update")
    public Result updateApply(@RequestBody BCp01DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);
        BCp01Entity bCp01Entity = bCp01Dao.selectById(dto.getCp0101());
        String newCorpcode = dto.getCorpcode();
        if(!newCorpcode.equals(bCp01Entity.getCorpcode())){
            BCp01Entity bCp01Entity1 = bCp01Dao.selectOne(new QueryWrapper<BCp01Entity>().eq("corpcode", dto.getCorpcode()));
            if(bCp01Entity1!=null){
                return new Result().error("社会统一信用代码已经存在，请勿录入重复数据。");
            }
        }
        SecurityUtils.getSubject();
        CommonUtils.userCpInfo();
        BCp02Entity bCp02Entity = new BCp02Entity();
        BeanUtils.copyProperties(dto,bCp02Entity);
        bCp02Entity.setCp02Status("0");
        bCp01Service.updateApply(bCp02Entity);
        Long cp0201 = bCp02Entity.getCp0201();
        List<BOt01DTO> ot01DTOList = dto.getOt01DTOList();
        if(ot01DTOList!=null && ot01DTOList.size()>0){
            for (BOt01DTO item :
                    ot01DTOList) {
                item.setBusisysno(cp0201);
                bOt01Service.update(item);
            }
        }
        Result result = new Result();
        result.ok("");
        result.setMsg("修改申请提交成功，请等待管理人员审核。");
        return result;
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("enterprise:cp01:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        bCp01Service.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<BCp01DTO> list = bCp01Service.list(params);


        ExcelUtils.exportExcelToTarget(response, null,"", list, BCp01Excel.class);
    }


    @PostMapping("register")
    @ApiOperation(value="注册企业账号",notes = "传入参数：注册地区areacode、社会统一信用代码corpcode、企业名称corpname、联系人姓名linkman、用户电话mobile、行业industy、产业estate、规模scale")
    @LogOperation("注册企业账号")
    public Result register(@RequestBody BCp01DTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        return bCp01Service.saveCp01Info(dto);
    }

    @GetMapping("getCp01InfoByAli/{corpcode}")
    @ApiOperation("调用天眼查获取企业工商信息")
    public Result<BCp01DTO> getCp01InfoByAli(@PathVariable("corpcode") String corpcode){
        CompanyDTO companyDTO = AilynUtils.getApiStore(corpcode);
        BCp01DTO cp01DTO = new BCp01DTO();
        if(companyDTO != null){
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
            if(!"".equals(companyDTO.getRegMoney()) && companyDTO.getRegMoney() != null){
                cp01DTO.setRegcapital(new Double(NumberUtil.getNumber(companyDTO.getRegMoney())));
            }
            //注册资本币种
            cp01DTO.setCapitalcurrencytype("1");
            //成立日期
            if(!"".equals(companyDTO.getChkDate()) && companyDTO.getChkDate()!=null){
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
            return new Result<BCp01DTO>().ok(cp01DTO);
        }else{
            return new Result<BCp01DTO>().error("请输入正确的社会信用代码！");
        }
    }
}