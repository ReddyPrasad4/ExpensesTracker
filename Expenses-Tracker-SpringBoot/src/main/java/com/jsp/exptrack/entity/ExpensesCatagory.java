package com.jsp.exptrack.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class ExpensesCatagory 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int categoryId;
	
	@Column(unique = true)
	private String category;
	
	@OneToMany(mappedBy = "category")
	private List<Expenses> expenses;

	
	public ExpensesCatagory(String category) 
	{
		this.category = category;
	}
	
}
