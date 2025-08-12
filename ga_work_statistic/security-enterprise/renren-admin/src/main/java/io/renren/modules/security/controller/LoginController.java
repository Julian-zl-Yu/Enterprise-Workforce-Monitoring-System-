/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.security.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.renren.common.constant.Constant;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.redis.RedisKeys;
import io.renren.common.redis.RedisUtils;
import io.renren.common.utils.IpUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.enterprise.bcp01.service.BCp01Service;
import io.renren.modules.enterprise.bcp03.service.BCp03Service;
import io.renren.modules.log.entity.SysLogLoginEntity;
import io.renren.modules.log.enums.LoginOperationEnum;
import io.renren.modules.log.enums.LoginStatusEnum;
import io.renren.modules.log.service.SysLogLoginService;
import io.renren.modules.message.service.SysSmsService;
import io.renren.modules.security.dto.LoginDTO;
import io.renren.modules.security.dto.ReSetPassWordDTO;
import io.renren.modules.security.password.PasswordUtils;
import io.renren.modules.security.service.CaptchaService;
import io.renren.modules.security.service.SysUserTokenService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.dto.SysUserDTO;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.enums.UserStatusEnum;
import io.renren.modules.sys.service.SysParamsService;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 登录
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@Api(tags="登录管理")
public class LoginController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private SysLogLoginService sysLogLoginService;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysParamsService sysParamsService;
	@Autowired
	private SysSmsService sysSmsService;
	@Autowired
	private BCp03Service cp03Service;

	@GetMapping("captcha")
	@ApiOperation(value = "验证码", produces="application/octet-stream")
	@ApiImplicitParam(paramType = "query", dataType="string", name = "uuid", required = true)
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		//uuid不能为空
		AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);

		//生成验证码
		captchaService.create(response, uuid);
	}

	@GetMapping("mail")
	@ApiOperation(value = "站内信" ,notes = "返回值data为1就弹框提示msg")
	public Result mail(HttpServletRequest request){
		//判断是否企业用户
		String data = "0";
		UserDetail user = SecurityUser.getUser();
		String userType = user.getUserType();
		Long deptId = user.getDeptId();
		if("2".equals(userType)){
			//判断是否到25号
			String dd = DateUtil.format(DateUtil.date(), "dd");
			Integer ddInt = Integer.valueOf(dd);
			if(ddInt>=25){
				//判断是否填报数据
				Long count = cp03Service.hasData(deptId);
				if(count.equals(0L)){
					data = "1";
				}
			}
		}else{

		}
		Result result = new Result();
		result.setMsg("请尽快填报用工缺工情况！");
		return result.ok(data);
	}

	@PostMapping("login")
	@ApiOperation(value = "登录")
	public Result login(HttpServletRequest request, @RequestBody LoginDTO login) {
		//效验数据
		ValidatorUtils.validateEntity(login);

		//验证码是否正确
		boolean flag = captchaService.validate(login.getUuid(), login.getCaptcha());
		if(!flag){
			return new Result().error(ErrorCode.CAPTCHA_ERROR);
		}

		//用户信息
		SysUserDTO user = sysUserService.getByUsername(login.getUsername());
		if(user == null){
			user = sysUserService.getByMobile(login.getUsername());
		}
		SysLogLoginEntity log = new SysLogLoginEntity();
		log.setOperation(LoginOperationEnum.LOGIN.value());
		log.setCreateDate(new Date());
		log.setIp(IpUtils.getIpAddr(request));
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setIp(IpUtils.getIpAddr(request));

		//用户不存在
		if(user == null){
			log.setStatus(LoginStatusEnum.FAIL.value());
			log.setCreatorName(login.getUsername());
			sysLogLoginService.save(log);

			throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
		}

		//密码错误
		if(!PasswordUtils.matches(login.getPassword(), user.getPassword())){
			log.setStatus(LoginStatusEnum.FAIL.value());
			log.setCreator(user.getId());
			log.setCreatorName(user.getUsername());
			sysLogLoginService.save(log);

			throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
		}

		//账号停用
		if(user.getStatus() == UserStatusEnum.DISABLE.value()){
			log.setStatus(LoginStatusEnum.LOCK.value());
			log.setCreator(user.getId());
			log.setCreatorName(user.getUsername());
			sysLogLoginService.save(log);

			throw new RenException(ErrorCode.ACCOUNT_DISABLE);
		}

		//登录成功
		log.setStatus(LoginStatusEnum.SUCCESS.value());
		log.setCreator(user.getId());
		log.setCreatorName(user.getUsername());
		sysLogLoginService.save(log);
		HttpSession session = request.getSession();
		session.setAttribute("deptId",user.getDeptId());

		return sysUserTokenService.createToken(user.getId());
	}

	@PostMapping("logout")
	@ApiOperation(value = "退出")
	public Result logout(HttpServletRequest request) {
		UserDetail user = SecurityUser.getUser();

		//退出
		sysUserTokenService.logout(user.getId());

		//用户信息
		SysLogLoginEntity log = new SysLogLoginEntity();
		log.setOperation(LoginOperationEnum.LOGOUT.value());
		log.setIp(IpUtils.getIpAddr(request));
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setIp(IpUtils.getIpAddr(request));
		log.setStatus(LoginStatusEnum.SUCCESS.value());
		log.setCreator(user.getId());
		log.setCreatorName(user.getUsername());
		log.setCreateDate(new Date());
		sysLogLoginService.save(log);

		return new Result();
	}

	@GetMapping("sendCode/{mobile}")
	@ApiOperation("重置密码之发送验证码")
	public Result sendCode(@PathVariable("mobile") String mobile){
		Result result = new Result();
		//验证手机号是否正确
		List<SysUserEntity> list = sysUserDao.selectList(new QueryWrapper<SysUserEntity>().eq("mobile", mobile));
		if(list !=null && list.size()==1){
			//生成6位验证码
			Random randObj = new Random();
			String yzm6 = Integer.toString(50000 + randObj.nextInt(900000));
			redisUtils.set(RedisKeys.getReSetPassWordKey(mobile),yzm6,RedisUtils.MIN_FIVE_EXPIRE);
			//发送账号密码到用户手机短信
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code",yzm6);
			sysSmsService.send("1002", mobile, jsonObject.toJSONString());
			result.setMsg("验证码发送成功，有效时间为10分钟");
		}else{
			result.error("该手机号没有关联账号，请输入正确手机号");
		}
		return result;
	}

	@PostMapping("reSetPassWord")
	@ApiOperation(value = "重置密码" , notes = "参数：手机号码mobile、短信验证码verificationCode")
	public Result reSetPassWord(HttpServletRequest request, @RequestBody ReSetPassWordDTO reSetPassWordDTO) {
		//效验数据
		ValidatorUtils.validateEntity(reSetPassWordDTO);
		Result result = new Result();
		//校验验证码是否正确
		String mobile = reSetPassWordDTO.getMobile();
		String verificationCode = reSetPassWordDTO.getVerificationCode();
		Object object = redisUtils.get(RedisKeys.getReSetPassWordKey(mobile));
		if(object!=null){
			String s = object.toString();
			if(s.equals(verificationCode)){
				List<SysUserEntity> list = sysUserDao.selectList(new QueryWrapper<SysUserEntity>().eq("mobile", mobile));
				if(list !=null && list.size()==1){
					SysUserEntity sysUserEntity = list.get(0);
					sysUserDao.updatePassword(sysUserEntity.getId(),PasswordUtils.encode(sysParamsService.getValue(Constant.PASSWORD)));
				}else{
					result.error("手机号关联账号存在问题，请联系管理员");
				}
				result.ok("");
				result.setMsg("重置密码成功，请及时修改默认密码。");
			}else{
				result.error("验证码不正确，请输入正确验证码");
			}
		}else{
			result.error("未找到该手机号的验证码，请发送验证码后勿修改手机号");
		}
		return result;
	}
	
}