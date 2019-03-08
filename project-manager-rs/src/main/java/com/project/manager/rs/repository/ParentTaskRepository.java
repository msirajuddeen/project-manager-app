package com.project.manager.rs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.manager.rs.entity.ParentTaskEntity;

@Repository
@Transactional
public interface ParentTaskRepository extends JpaRepository<ParentTaskEntity, Integer> {
	
	@Query("from ParentTaskEntity pTask where pTask.parentId=(:parentId)")
	ParentTaskEntity findByParentTaskId(@Param("parentId") int parentId);
	
	@Modifying
	@Query(value = "DELETE FROM PARENT_TASK where project_id=(:projectId)", nativeQuery = true)
    void deleteByProjectId(@Param("projectId") int projectId);
}
