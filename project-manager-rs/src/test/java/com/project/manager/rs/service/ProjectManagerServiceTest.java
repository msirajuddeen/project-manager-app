package com.project.manager.rs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
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

@RunWith(SpringRunner.class)
public class ProjectManagerServiceTest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String SUCCESS = "Success";
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ProjectRepository projectRepository;
	
	@MockBean
	private ParentTaskRepository parentTaskRepository;
	
	@MockBean
	private TaskRepository taskRepository;
	
	@Autowired
    private ProjectManagerService projectManagerService;
	
	@Configuration
	static class ProjectManagerServiceTestContextConfiguration {
		@Bean
		public ProjectManagerService projectManagerService() {
			return new ProjectManagerServiceImpl();
		}
	}

	@Before
    public void setUp() throws Exception {
		given(userRepository.findAll()).willReturn(getMockUser());
		given(projectRepository.findAll()).willReturn(getMockProject());
		given(parentTaskRepository.findAll()).willReturn(getMockParentTask());
		given(taskRepository.findByProjectId(1)).willReturn(getMockTask());
		given(projectRepository.findByUserId(1)).willReturn(getMockProjEnt());
	}
	
	@Test
	public void test_getUserService() throws Exception {
		when(userRepository.findAllUsers("A")).thenReturn(getMockUser());
		assertNotNull(projectManagerService.getUser());
	}
	
	@Test
	public void test_getProjectService() throws Exception {
		when(projectRepository.findAll()).thenReturn(getMockProject());
		given(taskRepository.findByProjectId(0)).willReturn(getMockTask());
		assertNotNull(projectManagerService.getProject());
	}
	
	@Test
	public void test_getParentTaskService() throws Exception {
		assertNotNull(projectManagerService.getParentTask());
	}
	
	@Test(expected=ProjectManagerException.class)
	public void test_getTaskService() throws Exception {
		when(taskRepository.findByProjectId(2)).thenReturn(getMockTask());
		projectManagerService.viewTask(2);
	}
	
	@Test
	public void test_update_addUserService() throws Exception {
		assertNotNull(projectManagerService.updateUser(getMockUser_forAdd()));
		assertEquals("Success", projectManagerService.updateUser(getMockUser_forAdd()));
	}
	
	@Test
	public void test_update_addProjectService() throws Exception {
		assertNotNull(projectManagerService.updateProject(getMockProject_forAdd()));
		assertEquals(SUCCESS, projectManagerService.updateProject(getMockProject_forAdd()));
	}
	
	@Test
	public void test_update_addParentTaskService() throws Exception {
		assertNotNull(projectManagerService.updateParentTask(getMockParentTask_forAdd()));
		assertEquals(SUCCESS, projectManagerService.updateParentTask(getMockParentTask_forAdd()));
	}
	
	@Test
	public void test_update_addTaskService() throws Exception {
		assertNotNull(projectManagerService.updateTask(getMockTask_forAdd()));
		assertEquals(SUCCESS, projectManagerService.updateTask(getMockTask_forAdd()));
	}
	
	@Test
	public void test_update_deleteUserService() throws Exception {
		assertNotNull(projectManagerService.updateUser(getMockUser_forDelete()));
		assertEquals("Success", projectManagerService.updateUser(getMockUser_forDelete()));
	}
	
	@Test
	public void test_update_deleteProjectService() throws Exception {
		assertNotNull(projectManagerService.updateProject(getMockProject_forDelete()));
		assertEquals(SUCCESS, projectManagerService.updateProject(getMockProject_forDelete()));
	}
	
	@Test
	public void test_update_deleteParentTaskService() throws Exception {
		assertNotNull(projectManagerService.updateParentTask(getMockParentTask_forDelete()));
		assertEquals(SUCCESS, projectManagerService.updateParentTask(getMockParentTask_forDelete()));
	}
	
	@Test
	public void test_update_deleteTaskService() throws Exception {
		assertNotNull(projectManagerService.updateTask(getMockTask_forDelete()));
		assertEquals(SUCCESS, projectManagerService.updateTask(getMockTask_forDelete()));
	}
	
	private List<UserEntity> getMockUser() {
		Gson gson = new Gson();
		List<UserEntity> res = new ArrayList<UserEntity>();
		try {
			UserEntity ent = gson.fromJson(new FileReader("mockData/getUserRes.json"), UserEntity.class);
			res.add(ent);
        } catch (Exception e) {
            logger.error("Exception in ProjectManagerServiceTest getMockUser : " + e);
        }
		return res;
	}

	private List<ProjectEntity> getMockProject() {
		Gson gson = new Gson();
		List<ProjectEntity> res = new ArrayList<ProjectEntity>();
		try {
			ProjectEntity ent = new ProjectEntity(); 
			ent.setEndDate(new Date());
			ent.setStartDate(new Date());
			ent.setProjectId(1);
			ent.setPriority(1);
			res.add(ent);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockProject : " + e);
        }
		return res;
	}
	
	private List<ParentTaskEntity> getMockParentTask() {
		Gson gson = new Gson();
		List<ParentTaskEntity> res = new ArrayList<ParentTaskEntity>();
		try {
			ParentTaskEntity ent = gson.fromJson(new FileReader("mockData/getParentTaskRes.json"), ParentTaskEntity.class);
			res.add(ent);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockParentTask : " + e);
        }
		return res;
	}
	
	private List<TaskEntity> getMockTask() {
		Gson gson = new Gson();
		List<TaskEntity> res = new ArrayList<TaskEntity>();
		try {
			TaskEntity ent = gson.fromJson(new FileReader("mockData/getTaskEntRes.json"), TaskEntity.class);
			res.add(ent);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockTask : " + e);
        }
		return res;
	}
	
	private ProjectEntity getMockProjEnt() {
		Gson gson = new Gson();
		ProjectEntity res = new ProjectEntity();
		try {
			res = gson.fromJson(new FileReader("mockData/projectEntRes.json"), ProjectEntity.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockTask : " + e);
        }
		return res;
	}
	
	private GetUserRequest getMockUser_forAdd() {
		Gson gson = new Gson();
		GetUserRequest req = new GetUserRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/addUserReq.json"), GetUserRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockUser_forAdd : " + e);
        }
		return req;
	}
	
	private GetProjectRequest getMockProject_forAdd() {
		Gson gson = new Gson();
		GetProjectRequest req = new GetProjectRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/addProjectReq.json"), GetProjectRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockProject_forAdd : " + e);
        }
		return req;
	}
	
	private GetParentTaskRequest getMockParentTask_forAdd() {
		Gson gson = new Gson();
		GetParentTaskRequest req = new GetParentTaskRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/addParentTaskReq.json"), GetParentTaskRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockParentTask_forAdd : " + e);
        }
		return req;
	}
	
	private GetTaskRequest getMockTask_forAdd() {
		Gson gson = new Gson();
		GetTaskRequest req = new GetTaskRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/addTaskReq.json"), GetTaskRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockTask_forAdd : " + e);
        }
		return req;
	}
	
	private GetUserRequest getMockUser_forDelete() {
		Gson gson = new Gson();
		GetUserRequest req = new GetUserRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/deleteUserReq.json"), GetUserRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockUser_forAdd : " + e);
        }
		return req;
	}
	
	private GetProjectRequest getMockProject_forDelete() {
		Gson gson = new Gson();
		GetProjectRequest req = new GetProjectRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/deleteProjectReq.json"), GetProjectRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockProject_forAdd : " + e);
        }
		return req;
	}
	
	private GetParentTaskRequest getMockParentTask_forDelete() {
		Gson gson = new Gson();
		GetParentTaskRequest req = new GetParentTaskRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/deleteParentTaskReq.json"), GetParentTaskRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockParentTask_forAdd : " + e);
        }
		return req;
	}
	
	private GetTaskRequest getMockTask_forDelete() {
		Gson gson = new Gson();
		GetTaskRequest req = new GetTaskRequest();
		try {
			req = gson.fromJson(new FileReader("mockData/deleteTaskReq.json"), GetTaskRequest.class);
        } catch (Exception e) {
        	logger.error("Exception in ProjectManagerServiceTest getMockTask_forAdd : " + e);
        }
		return req;
	}
	
}
