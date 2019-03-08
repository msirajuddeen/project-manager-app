package com.project.manager.rs.response;

import java.util.List;

import com.project.manager.rs.vo.UserVO;

public class GetUserResponse {
	
	private List<UserVO> userVO;

	private String status;

	public List<UserVO> getUserVO() {
		return userVO;
	}

	public void setUserVO(List<UserVO> userVO) {
		this.userVO = userVO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}