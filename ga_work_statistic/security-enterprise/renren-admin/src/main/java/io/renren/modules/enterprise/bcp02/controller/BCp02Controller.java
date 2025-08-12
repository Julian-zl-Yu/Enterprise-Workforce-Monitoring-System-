package io.renren.modules.enterprise.bcp02.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.enterprise.bcp01.dto.BCp01DTO;
import io.renren.modules.enterprise.bcp01.service.BCp01Service;
import io.renren.modules.enterprise.bcp02.dto.BCp02DTO;
import io.renren.modules.enterprise.bcp02.excel.BCp02Excel;
import io.renren.modules.enterprise.bcp02.service.BCp02Service;
import io.renren.modules.enterprise.bot01.entity.BOt01Entity;
import io.renren.modules.enterprise.bot01.service.BOt01Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 修改企业基础信息审核表
 *
 * @author JY 
 * @since 1.0.0 2025-08-03
 */
@RestController
@RequestMapping("enterprise/cp02")
@Api(tags="修改企业基础信息审核表")
public class BCp02Controller {
    @Autowired
    private BCp02Service bCp02Service;
    @Autowired
    private BCp01Service bCp01Service;
    @Autowired
    private BOt01Service bOt01Service;

    @GetMapping("page")
    @ApiOperation(value = "查询列表", notes = "列表字段：企业名称corpname、单位性质organizationtype、注册资本(万元)regcapital、联系人姓名linkman、联系人手机号码linkcellphone<br/>" +
            "查询条件：企业名称corpname、单位性质organizationtype、注册地区areacode")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("enterprise:cp02:page")
    public Result<PageData<BCp02DTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BCp02DTO> page = bCp02Service.pageList(params);

        return new Result<PageData<BCp02DTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("查看详情")
    @RequiresPermissions("enterprise:cp02:info")
    public Result<BCp02DTO> get(@PathVariable("id") Long id){
        BCp02DTO data = bCp02Service.get(id);
        List list = bOt01Service.listByMap(data.getCp0201());
        List list2 = bOt01Service.listByMap(data.getCp0101());
        list.addAll(list2);
        data.setOt01DTOList(list);
        return new Result<BCp02DTO>().ok(data);
    }

    @PutMapping("agree")
    @ApiOperation("审核通过")
    @LogOperation("审核通过")
    public Result agree(@RequestBody BCp02DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);
        Long cp0101 = dto.getCp0101();
        List<BOt01Entity> list = bOt01Service.listByMap(dto.getCp0201());
        for (BOt01Entity item :
                list) {
            item.setBusisysno(cp0101);
            bOt01Service.updateById(item);
        }
        dto.setCp02Status("1");
        bCp02Service.update(dto);
        BCp01DTO bCp01DTO = bCp01Service.get(dto.getCp0101());
        BeanUtils.copyProperties(dto,bCp01DTO);
        bCp01Service.update(bCp01DTO);
        Result result = new Result();
        result.setMsg("操作成功");
        return result;
    }

    @PutMapping("disagree")
    @ApiOperation("审核不通过")
    @LogOperation("审核不通过")
    public Result disagree(@RequestBody BCp02DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);
        dto.setCp02Status("2");
        bCp02Service.update(dto);
        Result result = new Result();
        result.setMsg("操作成功");
        return result;
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("enterprise:cp02:save")
    public Result save(@RequestBody BCp02DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp02Service.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("enterprise:cp02:update")
    public Result update(@RequestBody BCp02DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp02Service.update(dto);


        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("enterprise:cp02:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        bCp02Service.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("enterprise:cp02:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<BCp02DTO> list = bCp02Service.list(params);

        ExcelUtils.exportExcelToTarget(response, null,"", list, BCp02Excel.class);
    }

}