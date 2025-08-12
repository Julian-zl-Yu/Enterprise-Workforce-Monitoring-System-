package io.renren.modules.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 重置密码
 *
 * @author Ytw
 */
@Data
@ApiModel(value = "重置密码")
public class ReSetPassWordDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message="手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "短信验证码")
    @NotBlank(message="短信验证码不能为空")
    private String verificationCode;
}
