package com.jsp.exptrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.exptrack.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByUserNameAndPassword(String userName , String password);
	
	User findByEmail(String email);
}
