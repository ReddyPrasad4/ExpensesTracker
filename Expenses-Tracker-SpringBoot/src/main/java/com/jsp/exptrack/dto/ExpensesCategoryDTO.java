package com.jsp.exptrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

// Data Transfer Object  ----> it will transfer the data from controller to service
// It is used to store the data , which is entered in frontEnd files
public class ExpensesCategoryDTO 
{
	private String category;
}
