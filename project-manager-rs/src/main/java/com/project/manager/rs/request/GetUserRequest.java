package com.project.manager.rs.request;

import com.project.manager.rs.vo.UserVO;

public class GetUserRequest {
	
	private UserVO userVO;

	private String action;

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
