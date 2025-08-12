package io.renren.modules.enterprise.bcp03.controller;

import cn.hutool.core.date.DateUtil;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.CommonUtils;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.enterprise.bcp03.dto.BCp03DTO;
import io.renren.modules.enterprise.bcp03.excel.BCp03Excel;
import io.renren.modules.enterprise.bcp03.service.BCp03Service;
import io.renren.modules.security.user.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 企业用工缺工情况表
 *
 * @author JY 
 * @since 1.0.0 2025-08-06
 */
@RestController
@RequestMapping("enterprise/cp03")
@Api(tags="企业用工缺工情况表")
public class BCp03Controller {
    @Autowired
    private BCp03Service bCp03Service;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("enterprise:cp03:page")
    public Result<PageData<BCp03DTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BCp03DTO> page = bCp03Service.pageList(params);
        return new Result<PageData<BCp03DTO>>().ok(page);
    }

    @GetMapping("canAdd")
    @ApiOperation("是否能够新增数据")
    public Result canAdd(@ApiIgnore @RequestParam Map<String, Object> params){
        return bCp03Service.canAdd();
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("enterprise:cp03:info")
    public Result<BCp03DTO> get(@PathVariable("id") Long id){
        BCp03DTO data = bCp03Service.getInfo(id);

        return new Result<BCp03DTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("enterprise:cp03:save")
    public Result save(@RequestBody BCp03DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);
        Result result = new Result();
        Long cp0101 = CommonUtils.userCpInfo().getCp0101();
        dto.setCp0101(cp0101);
        List<BCp03DTO> list=bCp03Service.listByYM(dto);
        if(list!=null && list.size()>0)
            return result.error("已有该月份数据，请勿重复添加");
        dto.setExamineStatus("0");
        bCp03Service.save(dto);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("enterprise:cp03:update")
    public Result update(@RequestBody BCp03DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp03Service.update(dto);

        return new Result();
    }



    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("enterprise:cp03:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        bCp03Service.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<BCp03DTO> list = bCp03Service.list(params);

        ExcelUtils.exportExcelToTarget(response, null,"", list, BCp03Excel.class);

    }

    @GetMapping("trend")
    @ApiOperation("趋势")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    public Result<PageData<BCp03DTO>> trend(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BCp03DTO> page = bCp03Service.pageList(params);
        return new Result<PageData<BCp03DTO>>().ok(page);
    }

}