package com.project.manager.rs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.manager.rs.entity.ProjectEntity;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

	@Query("from ProjectEntity proj where proj.projectId=(:projectId)")
    ProjectEntity findByProjectId(@Param("projectId") int projectId);
	
	@Query("from ProjectEntity proj where proj.userEntity.userId=(:userId)")
    ProjectEntity findByUserId(@Param("userId") int userId);
	
	@Modifying
	@Query(value = "DELETE FROM PROJECT where user_id=(:userId)", nativeQuery = true)
    void deleteByUserId(@Param("userId") int userId);
}
