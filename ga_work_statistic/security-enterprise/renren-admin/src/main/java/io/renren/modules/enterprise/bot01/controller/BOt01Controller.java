package io.renren.modules.enterprise.bot01.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.enterprise.bot01.dto.BOt01DTO;
import io.renren.modules.enterprise.bot01.excel.BOt01Excel;
import io.renren.modules.enterprise.bot01.service.BOt01Service;
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
import java.util.List;
import java.util.Map;


/**
 * 附件数据表
 *
 * @author CJF
 * @since 1.0.0 2025-08-05
 */
@RestController
@RequestMapping("enterprise/ot01")
@Api(tags="附件数据表")
public class BOt01Controller {
    @Autowired
    private BOt01Service bOt01Service;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("enterprise:ot01:page")
    public Result<PageData<BOt01DTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<BOt01DTO> page = bOt01Service.page(params);

        return new Result<PageData<BOt01DTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("enterprise:ot01:info")
    public Result<BOt01DTO> get(@PathVariable("id") Long id){
        BOt01DTO data = bOt01Service.get(id);

        return new Result<BOt01DTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("enterprise:ot01:save")
    public Result save(@RequestBody BOt01DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bOt01Service.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("enterprise:ot01:update")
    public Result update(@RequestBody BOt01DTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto);

        bOt01Service.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        bOt01Service.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("enterprise:ot01:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<BOt01DTO> list = bOt01Service.list(params);

        ExcelUtils.exportExcelToTarget(response, null,"", list, BOt01Excel.class);
    }

    @PostMapping("upload")
    @ApiOperation("文件上传")
    public Result<Long> upload(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType, @RequestParam("viewType") String viewType) {
        if (file.isEmpty()) {
            throw new RenException(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        Long ot0101 = bOt01Service.upload(file, fileType,viewType);
        return new Result<Long>().ok(ot0101);
    }

}