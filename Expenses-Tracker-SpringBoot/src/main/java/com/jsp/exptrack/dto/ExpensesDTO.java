package com.jsp.exptrack.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExpensesDTO 
{
	// convert to string we can't take date as LocalDate object from user
//	private LocalDate date;
	
	private int expensesId;
	private String date;
	private double amount;
	private String description;
	
//	foreign keys
	
//	try
//	creating ExpensesCategoryDTO reference variable may create difficulties for taking data from frontEnd to backEnd
//	private ExpensesCategoryDTO categoryDTO;
//	private UserDTO userDTO;
	
	private String category;
}
