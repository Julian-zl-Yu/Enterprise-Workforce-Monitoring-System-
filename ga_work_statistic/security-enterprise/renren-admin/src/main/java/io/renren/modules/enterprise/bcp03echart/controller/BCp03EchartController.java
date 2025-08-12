package io.renren.modules.enterprise.bcp03echart.controller;

import io.renren.common.utils.Result;
import io.renren.modules.enterprise.bcp03.service.BCp03Service;
import io.renren.modules.enterprise.bcp03echart.dto.BCp03EchartDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 企业用工缺工情况趋势
 *
 * @author JY 
 * @since 1.0.0 2025-08-06
 */
@RestController
@RequestMapping("enterprise/cp03echart")
@Api(tags="企业用工缺工情况趋势")
public class BCp03EchartController {
    @Autowired
    private BCp03Service bCp03Service;

    @GetMapping("echart")
    @ApiOperation("趋势图")
    public Result<BCp03EchartDTO> echart(@ApiIgnore @RequestParam Map<String, Object> params){

        return bCp03Service.echart(params);
    }



}