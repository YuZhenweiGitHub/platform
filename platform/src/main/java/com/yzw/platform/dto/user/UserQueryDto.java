package com.yzw.platform.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserQueryDto {

    @NotBlank(message = "{userQueryDto.userName.notBlank}")
    @Size(min = 1, message = "{userQueryDto.userName.size}")
    private String userName;

    @NotBlank(message = "{userQueryDto.passWord.notBlank}")
    @Size(min = 6, max = 18, message = "{userQueryDto.passWord.size}")
    private String passWord;

    /*@NotBlank(message = "{userQueryDto.verificationCode.notBlank}")
    @Size(min = 6, max = 6, message = "{userQueryDto.verificationCode.size}")
    private String verificationCode;*/

    private String newPassWord;

    private Boolean rememberMe = Boolean.FALSE;

}
