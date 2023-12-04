package com.jsp.exptrack.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.exptrack.dto.ExpensesDTO;
import com.jsp.exptrack.entity.Expenses;
import com.jsp.exptrack.entity.ExpensesCatagory;
import com.jsp.exptrack.entity.User;

public interface ExpensesRepository extends JpaRepository<Expenses, Integer>
{
	List<Expenses> findByDateAndCategoryAndAmountAndUser(LocalDate date,ExpensesCatagory catagory,double amount,User user);
	
	List<Expenses> findByDateBetween(LocalDate startDate,LocalDate endDate);
	
	List<Expenses> findByUser(User user);
	
	
	
	
}
