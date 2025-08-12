package io.renren.modules.enterprise.bcp11.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.enterprise.bcp11.dto.BCp11DTO;
import io.renren.modules.enterprise.bcp11.entity.BCp11Entity;
import io.renren.modules.enterprise.bcp11.excel.BCp11Excel;
import io.renren.modules.enterprise.bcp11.excel.BPs12Excel;
import io.renren.modules.enterprise.bcp11.service.BCp11Service;
import io.renren.modules.enterprise.bcp12.dto.BCp12DTO;
import io.renren.modules.enterprise.bcp12.service.BCp12Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 批量导入企业主表
 *
 * @author CJF
 * @since 1.0.0 2025-08-08
 */
@RestController
@RequestMapping("enterprise/cp11")
@Api(tags="批量导入企业主表")
public class BCp11Controller {
    @Autowired
    private BCp11Service bCp11Service;
    @Autowired
    private BCp12Service bCp12Service;

//    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("enterprise:bcp11:page")
    public Result<PageData<BCp11DTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BCp11DTO> page = bCp11Service.page(params);

        return new Result<PageData<BCp11DTO>>().ok(page);
    }

    @GetMapping("page")
    @ApiOperation("导入校验后调用查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("enterprise:bcp11:page")
    public Result<PageData<BCp12DTO>> repage(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BCp12DTO> page = bCp12Service.repage(params);

        return new Result<PageData<BCp12DTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("enterprise:bcp11:info")
    public Result<BCp11DTO> get(@PathVariable("id") Long id){
        BCp11DTO data = bCp11Service.get(id);

        return new Result<BCp11DTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("enterprise:bcp11:save")
    public Result save(@RequestBody BCp11DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp11Service.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("enterprise:bcp11:update")
    public Result update(@RequestBody BCp11DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bCp11Service.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("enterprise:bcp11:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        bCp11Service.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
//    @RequiresPermissions("enterprise:bcp11:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<BCp11DTO> list = bCp11Service.list(params);

        ExcelUtils.exportExcelToTarget(response, null,"", list, BCp11Excel.class);
    }

    @PostMapping("importCp01")
    @ApiOperation("批量导入企业")
    public Result importLive(MultipartFile file) {
        Result r = new Result();
        try {
//            ExportParams exportParams = new ExportParams();
//            exportParams.setType(ExcelType.XSSF);
            ImportParams params = new ImportParams();
//            params.setNeedVerify(true);
            List<BCp11Excel> excelList = ExcelImportUtil.importExcel(file.getInputStream(), BCp11Excel.class, params);
            //清除社会统一信用代码为空的
            List<BCp11Excel> collect = excelList.stream().filter(c -> c.getCorpcode() != null).collect(Collectors.toList());
            r = bCp11Service.importCp(collect);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return r.ok("error");
        }
    }


    @GetMapping("saveDate/{cp1101}")
    @ApiOperation("保存数据")
    public Result saveDate(@PathVariable("cp1101") String cp1101){

        return bCp11Service.saveData(cp1101);
    }

}