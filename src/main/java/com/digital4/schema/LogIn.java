package com.digital4.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class LogIn {
	@ApiModelProperty(required = true, position = 1, example = "loginId", dataType = "string", notes = "로그인ID")
	private String loginId;
	
	@ApiModelProperty(required = true, position = 2, example = "password", dataType = "string", notes = "비밀번호")
	private String password;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
