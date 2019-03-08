package com.project.manager.rs.response;

import java.util.List;

import com.project.manager.rs.vo.ProjectVO;

public class GetProjectResponse {
	
	private List<ProjectVO> projectVO;

	private String status;

	public List<ProjectVO> getProjectVO() {
		return projectVO;
	}

	public void setProjectVO(List<ProjectVO> projectVO) {
		this.projectVO = projectVO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
