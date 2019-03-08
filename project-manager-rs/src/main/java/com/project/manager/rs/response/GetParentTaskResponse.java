package com.project.manager.rs.response;

import java.util.List;

import com.project.manager.rs.vo.ParentTaskVO;

public class GetParentTaskResponse {
	
	private List<ParentTaskVO> parentTaskVO;

	private String status;

	public List<ParentTaskVO> getParentTaskVO() {
		return parentTaskVO;
	}

	public void setParentTaskVO(List<ParentTaskVO> parentTaskVO) {
		this.parentTaskVO = parentTaskVO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
