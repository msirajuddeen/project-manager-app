package com.project.manager.rs.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.manager.rs.constants.ProjectManagerConstants;
import com.project.manager.rs.entity.ParentTaskEntity;
import com.project.manager.rs.entity.ProjectEntity;
import com.project.manager.rs.entity.TaskEntity;
import com.project.manager.rs.entity.UserEntity;
import com.project.manager.rs.exception.ProjectManagerException;
import com.project.manager.rs.repository.ParentTaskRepository;
import com.project.manager.rs.repository.ProjectRepository;
import com.project.manager.rs.repository.TaskRepository;
import com.project.manager.rs.repository.UserRepository;
import com.project.manager.rs.request.GetParentTaskRequest;
import com.project.manager.rs.request.GetProjectRequest;
import com.project.manager.rs.request.GetTaskRequest;
import com.project.manager.rs.request.GetUserRequest;
import com.project.manager.rs.response.GetParentTaskResponse;
import com.project.manager.rs.response.GetProjectResponse;
import com.project.manager.rs.response.GetTaskResponse;
import com.project.manager.rs.response.GetUserResponse;
import com.project.manager.rs.util.ProjectManagerUtil;
import com.project.manager.rs.vo.ParentTaskVO;
import com.project.manager.rs.vo.ProjectVO;
import com.project.manager.rs.vo.TaskVO;
import com.project.manager.rs.vo.UserVO;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService, ProjectManagerConstants {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ParentTaskRepository parentTaskRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public GetTaskResponse viewTask(int projectId) throws ProjectManagerException {
		GetTaskResponse getTaskResponse = new GetTaskResponse();
		List<TaskVO> taskVOList = null;
		try {
			List<TaskEntity> taskEntList = getTaskByProjectId(projectId);
			if (null != taskEntList && !taskEntList.isEmpty()) {
				taskVOList = new ArrayList<>();

				for (TaskEntity taskEnt : taskEntList) {
					TaskVO task = new TaskVO();
					task.setTaskId(taskEnt.getTaskId());
					task.setTask(taskEnt.getTask());

					task.setParentId(null != taskEnt.getParentTaskEntity() ? taskEnt.getParentTaskEntity().getParentId() : 0);
					task.setParentTaskName(
							null != taskEnt.getParentTaskEntity() ? taskEnt.getParentTaskEntity().getParentTask() : "");

					task.setProjectId(taskEnt.getProjectEnt().getProjectId());
					task.setProjectName(taskEnt.getProjectEnt().getProject());

					task.setUserId(taskEnt.getProjectEnt().getUserEntity().getUserId());
					task.setUserFName(taskEnt.getProjectEnt().getUserEntity().getFname());
					task.setUserLName(taskEnt.getProjectEnt().getUserEntity().getLname());

					task.setPriority(taskEnt.getPriority());
					task.setStartDate(ProjectManagerUtil.dateToString(taskEnt.getStartDate()));
					task.setEndDate(ProjectManagerUtil.dateToString(taskEnt.getEndDate()));
					task.setStatus(taskEnt.getStatus());

					taskVOList.add(task);
				}
			}
			getTaskResponse.setTaskVO(taskVOList);
			getTaskResponse.setStatus(SUCCESS);
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return getTaskResponse;
	}

	@Override
	public GetParentTaskResponse getParentTask() throws ProjectManagerException {
		GetParentTaskResponse getParentTaskResponse = new GetParentTaskResponse();
		List<ParentTaskVO> parentTaskVOList = null;
		List<ParentTaskEntity> parentTaskEntList = null;
		try {
			parentTaskEntList = parentTaskRepository.findAll();
			if (null != parentTaskEntList && !parentTaskEntList.isEmpty()) {
				parentTaskVOList = new ArrayList<>();

				for (ParentTaskEntity parentTaskEnt : parentTaskEntList) {
					ParentTaskVO parentTask = new ParentTaskVO();
					parentTask.setParentId(parentTaskEnt.getParentId());
					parentTask.setParentTaskName(parentTaskEnt.getParentTask());
					parentTask.setProjectId(
							null != parentTaskEnt.getProjectEntity() ? parentTaskEnt.getProjectEntity().getProjectId() : 0);

					parentTaskVOList.add(parentTask);
				}
			}
			getParentTaskResponse.setParentTaskVO(parentTaskVOList);
			getParentTaskResponse.setStatus(SUCCESS);
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return getParentTaskResponse;
	}

	@Override
	public GetProjectResponse getProject() throws ProjectManagerException {
		GetProjectResponse getProjectResponse = new GetProjectResponse();
		List<ProjectVO> projectVOList = null;
		int completedTaskCount = 0;
		try {
			List<ProjectEntity> projectEntList = projectRepository.findAll();
			if (null != projectEntList && !projectEntList.isEmpty()) {
				projectVOList = new ArrayList<>();

				for (ProjectEntity projectEnt : projectEntList) {
					ProjectVO project = new ProjectVO();
					project.setProjectId(projectEnt.getProjectId());
					project.setProject(projectEnt.getProject());
					project.setPriority(projectEnt.getPriority());
					project.setStartDate(ProjectManagerUtil.dateToString(projectEnt.getStartDate()));
					project.setEndDate(ProjectManagerUtil.dateToString(projectEnt.getEndDate()));
					project.setEmpId(null != projectEnt.getUserEntity() ? projectEnt.getUserEntity().getUserId() : 0);

					List<TaskEntity> taskEntList = getTaskByProjectId(projectEnt.getProjectId());
					if (null != taskEntList && !taskEntList.isEmpty()) {
						for (TaskEntity taskEnt : taskEntList) {
							if (null != taskEnt.getStatus() && STATUS_COMPLETED.equalsIgnoreCase(taskEnt.getStatus())) {
								completedTaskCount++;
							}
						}
						project.setNoOfTask(taskEntList.size());
						project.setNoOfCompletedTask(completedTaskCount);
						completedTaskCount = 0;
					}
					projectVOList.add(project);
				}
			}
			getProjectResponse.setProjectVO(projectVOList);
			getProjectResponse.setStatus(SUCCESS);
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return getProjectResponse;
	}

	@Override
	public GetUserResponse getUser() throws ProjectManagerException {
		GetUserResponse getUserResponse = new GetUserResponse();
		List<UserVO> userVOList = null;
		try {
			List<UserEntity> userEntList = userRepository.findAllUsers("A");
			if (null != userEntList && !userEntList.isEmpty()) {
				userVOList = new ArrayList<>();

				for (UserEntity userEnt : userEntList) {
					UserVO user = new UserVO();
					user.setUserId(userEnt.getUserId());
					user.setFname(userEnt.getFname());
					user.setLname(userEnt.getLname());
					user.setEmpId(userEnt.getEmpId());
					user.setStatus(userEnt.getStatus());
					userVOList.add(user);
				}
			}
			getUserResponse.setUserVO(userVOList);
			getUserResponse.setStatus(SUCCESS);
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return getUserResponse;
	}

	@Override
	public String updateTask(GetTaskRequest request) throws ProjectManagerException {
		TaskEntity taskEnt = new TaskEntity();
		String status = EMPTY;
		try {
			ProjectEntity projectEnt = projectRepository.findByProjectId(request.getTaskVO().getProjectId());
			ParentTaskEntity parentTaskEnt = parentTaskRepository.findByParentTaskId(request.getTaskVO().getParentId());

			taskEnt.setTaskId(request.getTaskVO().getTaskId());
			taskEnt.setTask(request.getTaskVO().getTask());
			taskEnt.setStartDate(ProjectManagerUtil.stringToDate(request.getTaskVO().getStartDate()));
			taskEnt.setEndDate(ProjectManagerUtil.stringToDate(request.getTaskVO().getEndDate()));
			taskEnt.setPriority(request.getTaskVO().getPriority());
			taskEnt.setProjectEnt(projectEnt);
			taskEnt.setParentTaskEnt(parentTaskEnt);
			taskEnt.setStatus(request.getTaskVO().getStatus());
			taskRepository.save(taskEnt);
			status = SUCCESS;
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return status;
	}

	@Override
	public String updateParentTask(GetParentTaskRequest request) throws ProjectManagerException {
		String status = EMPTY;
		ParentTaskEntity parentTaskEnt = new ParentTaskEntity();
		try {
			if (ACTION_DELETE.equalsIgnoreCase(request.getAction())) {
				parentTaskEnt.setParentId(request.getParentTaskVO().getParentId());
				if (deleteTask(request.getParentTaskVO().getParentId())) {
					parentTaskRepository.delete(parentTaskEnt);
				} else {
					logger.error("updateParentTask - Task deletion failed");
					status = USER_DELETE_FAILED_MESSAGE;
				}
			} else {
				ProjectEntity projectEnt = projectRepository.findByProjectId(request.getParentTaskVO().getProjectId());

				parentTaskEnt.setParentId(request.getParentTaskVO().getParentId());
				parentTaskEnt.setParentTask(request.getParentTaskVO().getParentTaskName());
				parentTaskEnt.setProjectEntity(projectEnt);
				parentTaskRepository.save(parentTaskEnt);
			}

			status = SUCCESS;
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return status;
	}

	@Override
	public String updateProject(GetProjectRequest request) throws ProjectManagerException {
		String status = EMPTY;
		ProjectEntity projectEnt = new ProjectEntity();
		try {
			if (ACTION_DELETE.equalsIgnoreCase(request.getAction())) {
				projectEnt.setProjectId(request.getProjectVO().getProjectId());
				if (deleteTask(request.getProjectVO().getProjectId())) {
					if (deleteParentTask(request.getProjectVO().getProjectId())) {
						projectRepository.delete(projectEnt);
					} else {
						logger.error("updateProject - Parent Task deletion failed");
						status = USER_DELETE_FAILED_MESSAGE;
					}
				} else {
					logger.error("updateProject - Task deletion failed");
					status = USER_DELETE_FAILED_MESSAGE;
				}
			} else {
				UserEntity userEnt = userRepository.findByUserId(request.getProjectVO().getEmpId());

				projectEnt.setProjectId(request.getProjectVO().getProjectId());
				projectEnt.setProject(request.getProjectVO().getProject());
				projectEnt.setPriority(request.getProjectVO().getPriority());
				projectEnt.setStartDate(ProjectManagerUtil.stringToDate(request.getProjectVO().getStartDate()));
				projectEnt.setEndDate(ProjectManagerUtil.stringToDate(request.getProjectVO().getEndDate()));
				projectEnt.setUserEnt(userEnt);
				projectRepository.save(projectEnt);
			}
			status = SUCCESS;
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return status;
	}

	@Override
	public String updateUser(GetUserRequest request) throws ProjectManagerException {
		UserEntity userEnt = new UserEntity();
		String status = EMPTY;
		try {
			userEnt.setUserId(request.getUserVO().getUserId());
			if (ACTION_DELETE.equalsIgnoreCase(request.getAction())) {
				userEnt.setFname(request.getUserVO().getFname());
				userEnt.setLname(request.getUserVO().getLname());
				userEnt.setEmpId(request.getUserVO().getEmpId());
				userEnt.setStatus("I");
				userRepository.save(userEnt);
				status = SUCCESS;
			} else {
				userEnt.setFname(request.getUserVO().getFname());
				userEnt.setLname(request.getUserVO().getLname());
				userEnt.setEmpId(request.getUserVO().getEmpId());
				userEnt.setStatus("A");
				userRepository.save(userEnt);
				status = SUCCESS;
			}
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return status;
	}

	private List<TaskEntity> getTaskByProjectId(int projectId) throws ProjectManagerException {
		List<TaskEntity> taskEntList = null;
		try {
			taskEntList = taskRepository.findByProjectId(projectId);
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return taskEntList;
	}

	private boolean deleteTask(int projectId) throws ProjectManagerException {
		boolean flag = false;
		try {
			taskRepository.deleteByProjectId(projectId);
			flag = true;
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return flag;
	}

	private boolean deleteParentTask(int projectId) throws ProjectManagerException {
		boolean flag = false;
		try {
			parentTaskRepository.deleteByProjectId(projectId);
			flag = true;
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return flag;
	}

	private boolean deleteProject(int userId) throws ProjectManagerException {
		boolean flag = false;
		try {
			projectRepository.deleteByUserId(userId);
			flag = true;
		} catch (Exception e) {
			throw new ProjectManagerException(TECH_ERROR_CODE, TECH_ERROR_MESSAGE, STATUS_500);
		}
		return flag;
	}


}
