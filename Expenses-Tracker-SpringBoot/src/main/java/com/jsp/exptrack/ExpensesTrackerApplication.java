package com.jsp.exptrack;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jsp.exptrack.entity.ExpensesCatagory;
import com.jsp.exptrack.repository.ExpensesCategoryRepository;

@SpringBootApplication
public class ExpensesTrackerApplication // implements CommandLineRunner 
{	
	
	public static void main(String[] args) 
	{
		SpringApplication.run(ExpensesTrackerApplication.class, args);
		openSignInPage();
	}
	
	// This method will helps to open the index page when we run the program using springBoot App
	private static void openSignInPage()  
	{
		
		Runtime run =  Runtime.getRuntime();
		try 
		{
			// this exec() is a over loaded method 
			// It is taking 2 arguments one is browser location to open , url of index file
			
			run.exec( new String[] { "C:/Program Files/Google/Chrome/Application/chrome.exe" ,"http://localhost:8080/project/index"});
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
//	Storing all the catagories into the data base
//	@Override
//	public void run(String... args) throws Exception
//	{
//		to execute any code at the starting of an application
		
//		these are the categories given by developer --> so after inserting into table make it as comment
//		List<ExpensesCatagory> catagories = Arrays.asList(          
//				new ExpensesCatagory("Utilities"),
//				new ExpensesCatagory("Transportation"),
//				new ExpensesCatagory("Groceries"),
//				new ExpensesCatagory("Dining Out"),
//				new ExpensesCatagory("Health care"),
//				new ExpensesCatagory("Entertainment"),
//				new ExpensesCatagory("Education"),
//				new ExpensesCatagory("Personal Care"),
//				new ExpensesCatagory("Clothing"),
//				new ExpensesCatagory("Home Maintenance"),
//				new ExpensesCatagory("Gifts and Donations"),
//				new ExpensesCatagory("Savings and Investments"),
//				new ExpensesCatagory("Tax"),
//				new ExpensesCatagory("Others")
//				);
		
//		repository.saveAll(catagories);
		
//		to insert categories again uncomment above line
		
//	}

}
