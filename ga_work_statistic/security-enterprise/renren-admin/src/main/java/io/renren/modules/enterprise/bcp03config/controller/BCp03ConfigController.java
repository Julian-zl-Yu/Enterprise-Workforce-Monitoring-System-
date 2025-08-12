package io.renren.modules.enterprise.bcp03config.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.enterprise.bcp03config.dto.BCp03ConfigDTO;
import io.renren.modules.enterprise.bcp03config.excel.BCp03ConfigExcel;
import io.renren.modules.enterprise.bcp03config.service.BCp03ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 企业用工缺工情况配置表
 *
 * @author JY 
 * @since 1.0.0 2025-08-08
 */
@RestController
@RequestMapping("enterprise/cp03config")
@Api(tags="企业用工缺工情况配置表")
public class BCp03ConfigController {
    @Autowired
    private BCp03ConfigService bCp03ConfigService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("enterprise:cp03config:page")
    public Result<PageData<BCp03ConfigDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BCp03ConfigDTO> page = bCp03ConfigService.page(params);

        return new Result<PageData<BCp03ConfigDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("enterprise:cp03config:info")
    public Result<BCp03ConfigDTO> get(@PathVariable("id") Long id){
        BCp03ConfigDTO data = bCp03ConfigService.get(id);

        return new Result<BCp03ConfigDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("enterprise:cp03config:save")
    public Result save(@RequestBody BCp03ConfigDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp03ConfigService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("enterprise:cp03config:update")
    public Result update(@RequestBody BCp03ConfigDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp03ConfigService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("enterprise:cp03config:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        bCp03ConfigService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("enterprise:cp03config:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<BCp03ConfigDTO> list = bCp03ConfigService.list(params);

        ExcelUtils.exportExcelToTarget(response, null,"" ,list, BCp03ConfigExcel.class);
    }

}