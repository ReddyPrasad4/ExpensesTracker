package com.jsp.exptrack.serivce;

import java.time.LocalDate;
import java.util.List;

import com.jsp.exptrack.dto.ExpensesDTO;
import com.jsp.exptrack.dto.FilterExpensesDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.entity.Expenses;

public interface ExpensesService 
{
	int addExpenses(ExpensesDTO dto,int userId);
	
	double sumExpenses(String startDate,String endDate,int userId);
	
	List<ExpensesDTO> viewExpenses(int userId);
	
	ExpensesDTO updateExpenses(int expensesId,ExpensesDTO dto);
	
	int deleteExpenses(int expensesId);
	
	ExpensesDTO findByExpensesId( int expensesId);
	
	List<ExpensesDTO> filterBetweenDates(String stDate, String edDate, int userId);
	
	List<ExpensesDTO> filterBasedOnCategoryAmountDate(FilterExpensesDTO filterExpensesDTO , int userId);
	
	List<ExpensesDTO> filterExpenses(String date,String category,double amount,int userId);
	
	List<ExpensesDTO> filterBasedOnDateAndCategory(FilterExpensesDTO filterExpensesDTO , int userId);
	
	List<ExpensesDTO> filterBasedOnDateAndAmount(FilterExpensesDTO filterExpensesDTO , int userId);
	
	List<ExpensesDTO> filterBasedOnCategoryAndAmount(FilterExpensesDTO filterExpensesDTO , int userId);
	
	List<ExpensesDTO> filterBasedOnDate( FilterExpensesDTO filterExpensesDTO,int userId);
	
	List<ExpensesDTO> filterBasedOnAmount(String amount,int userId);
	
	List<ExpensesDTO> filterBasedOnCategory(String category,int userId);
		
	
}
