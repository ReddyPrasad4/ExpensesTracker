package com.jsp.exptrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.exptrack.entity.ExpensesCatagory;

public interface ExpensesCategoryRepository extends JpaRepository<ExpensesCatagory, Integer> 
{
	ExpensesCatagory findByCategory(String category);
}
