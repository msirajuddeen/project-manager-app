package com.project.manager.rs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.manager.rs.entity.UserEntity;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	@Query("from UserEntity usr where usr.userId=(:userId)")
	UserEntity findByUserId(@Param("userId") int userId);
	
	@Query("from UserEntity usr where usr.empId=(:empId)")
	UserEntity findByEmpId(@Param("empId") String empId);
	
	@Query("from UserEntity usr where usr.status=(:status)")
	List<UserEntity> findAllUsers(@Param("status") String status);
}
