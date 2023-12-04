package com.jsp.exptrack.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Expenses 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int expensesId;
	private LocalDate date;
	private double amount;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "category_Id")
	private ExpensesCatagory category;
	
	@ManyToOne
	@JoinColumn(name = "user_Id")
	private User user;
	
	
}
