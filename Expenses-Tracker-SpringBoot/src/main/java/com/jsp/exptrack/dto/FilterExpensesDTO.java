package com.jsp.exptrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FilterExpensesDTO
{
	private String amountRange;
	private String startDate;
	private String endDate;
	private String category;
}
