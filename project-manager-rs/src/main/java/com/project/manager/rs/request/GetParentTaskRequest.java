package com.project.manager.rs.request;

import com.project.manager.rs.vo.ParentTaskVO;

public class GetParentTaskRequest {
	
	private ParentTaskVO parentTaskVO;

	private String action;

	public ParentTaskVO getParentTaskVO() {
		return parentTaskVO;
	}

	public void setParentTaskVO(ParentTaskVO parentTaskVO) {
		this.parentTaskVO = parentTaskVO;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}