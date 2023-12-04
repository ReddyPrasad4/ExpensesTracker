package com.jsp.exptrack.serivce;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.jsp.exptrack.dto.ExpensesCategoryDTO;
import com.jsp.exptrack.dto.ExpensesDTO;
import com.jsp.exptrack.dto.FilterExpensesDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.entity.Expenses;
import com.jsp.exptrack.entity.ExpensesCatagory;
import com.jsp.exptrack.entity.User;
import com.jsp.exptrack.repository.ExpensesCategoryRepository;
import com.jsp.exptrack.repository.ExpensesRepository;
import com.jsp.exptrack.repository.UserRepository;

@Service
public class ExpensesServiceImple implements ExpensesService 
{

	@Autowired
	private ExpensesCategoryRepository categoryRepository;

	@Autowired
	private ExpensesRepository expensesRepository;

	@Autowired
	private UserRepository userRepository;

	private double totalExpenses6;
	
	
	//                    USE STREAM API FOR EASY AND LESS CODING 

	@Override
	public int addExpenses(ExpensesDTO dto,int userId) 
	{
		/*
		 * -> Expenses Table contains 2 foreign keys those are category_Id And user_Id
		 * -> To insert value in foreign keys columns 
		 * 		* firstly programmer have to verify user and category and then retrieve those by using categoryName and userId
		 * 		* After that insert into Expenses table
		 *   
		 */
		
		//--> verification and retrieval of category by using categoryName
		ExpensesCatagory expeCatagory = categoryRepository.findByCategory(dto.getCategory());
		
		//--> verification and retrieval user by using userId
		Optional<User> user = userRepository.findById(userId);
		
		//--> if both are valid and then inserting expenses record into table
		if (expeCatagory != null && user.isPresent()) 
		{
			User currentUser = user.get();
			Expenses expense = new Expenses();
			
			BeanUtils.copyProperties(dto, expense);

			expense.setUser(currentUser);
			expense.setCategory(expeCatagory);
			
			// convert String date of ExpensesDTO to LocalDate of Expenses Entity class
			// inserting data of LocalDate to entity by converting String to LocalDate by using parse()
			expense.setDate(LocalDate.parse(dto.getDate()));
			
			Expenses exp = expensesRepository.save(expense);
			return exp.getExpensesId();
		}
		return 0;
	}

	@Override
	public List<ExpensesDTO> filterExpenses(String date, String category, double amount, int userId)
	{
		ExpensesCatagory expCategory = categoryRepository.findByCategory(category);

		List<ExpensesDTO> expensesDTOs = null;
		
		
		Optional<User> userAccount = userRepository.findById(userId);
		if (userAccount.isPresent() && expCategory != null)
		{
			User user = userAccount.get();
			
			LocalDate convertedDate = LocalDate.parse(date);
			
			List<Expenses> expenses = expensesRepository.findByDateAndCategoryAndAmountAndUser(convertedDate, expCategory,amount, user);
			
			/*
			 * -> This is stream API map method [map()] 
			 * -> This method will convert all the ExpensesDTO into a List
			 * 
			 * -> WHY WE USE RETURNING THE EXPENSESDTO IN BETWEEN STREAM ?
			 *    * we want which generic type of list ExpensesDTO so we are returning it
			 *    * the returned ExpensesDTO object go and stores in List<ExpensesDTO> by collect(Collectors.toList())
			 *    
			 *    * the job of collect(Collectors.toList()) is to collect all the returned ExpensesDTO object and store it in List
			 * 		and at last the List<ExpensesDto> which is stored by collect(Collect) is assigned to expensesDTOS variable
			 * */
			expensesDTOs =  expenses.stream().map( exp -> {
				
			/* -> our ExpensesDTO class contains ExpensesDTO object as well as UserDTO object 
			 * -> We can't copy all the list by using BeanUtils.copyProperties(List<Expenses>, List<ExpensesDTO>) due to miss match of classes
			 * -> so we are creating all DTO class object and copying the all corresponding entity class object data from List<Expenses>
			 * 	  [Expenses class contains all the data]  by using copyProperties()
			 * 	  SEE EXPENSES CLASS AND EXPENSESDTO CLASSES
			 * -> finally we are setting the UserDTO and ExpenseCategoryDTO into ExpensesDTO (SEE ExpensesDTO class once)
			 */
				
				ExpensesDTO expDTO = new ExpensesDTO();
				
				BeanUtils.copyProperties(exp, expDTO);
				
				expDTO.setCategory(category);
				return expDTO;
				
			}).collect(Collectors.toList());

		}
		return expensesDTOs;
	}
	
	
	/*
	 * It will fetch  all the expenses of respective user based on userId because every user have different expenses 
	 */
	
	@Override
	public List<ExpensesDTO> viewExpenses(int userId) 
	{
		
		// 1. find User records from userTable based on userId and then retrieve data
		Optional<User> userAccount = userRepository.findById(userId);
		
		List<ExpensesDTO> expensesDTOs = null;
		if (userAccount.isPresent()) 
		{
			User user = userAccount.get();
			
			/*
			 * create a user-defined method in ExpensesRepository to access expenses by using user-details
			 * 
			 * -> findByUser() returns list of expenses of a respective user 
			 * -> after that copy the data from expenses entity list to expensesDTO list make use of stream API or for each loop 
			 * -> copy the data by using BeanUtils abstract class copyProperties() 
			 * -> it will copy the data of same variable (see the DESCRIPTION of that method)
			 * -> Expenses class User variable doesn't matches with ExpensesDTO class UserDTO similarly ExpensesCategory != ExpensesCAtegoryDTO
			 * -> so we have to copy the data separately and then set the data into expensesDTO
			 */
			
			List<Expenses> expenses = expensesRepository.findByUser(user);

			
			expensesDTOs =  expenses.stream().map( exp -> {
				
				ExpensesDTO expensesDTO = new ExpensesDTO();
				
				BeanUtils.copyProperties(exp, expensesDTO);
				
				expensesDTO.setDate(exp.getDate().toString());
				
				Optional<ExpensesCatagory> expCatOpt =  categoryRepository.findById(exp.getCategory().getCategoryId());
				ExpensesCatagory expensesCatagory = expCatOpt.get();
				expensesDTO.setCategory(expensesCatagory.getCategory());
				
				return expensesDTO;
			}).collect(Collectors.toList());

		}
		return expensesDTOs;
	}
	
	@Override
	public List<ExpensesDTO> filterBetweenDates(String stDate, String edDate, int userId)
	{
		LocalDate startDate = LocalDate.parse(stDate);
		LocalDate endDate = LocalDate.parse(edDate);
		
		
		List<ExpensesDTO> expenses =  viewExpenses(userId).stream().filter( expDTO ->
				// see not (!) operator
				! LocalDate.parse(expDTO.getDate()).isBefore(startDate) &&  ! LocalDate.parse(expDTO.getDate()).isAfter(endDate))
				.toList();
		
		return expenses;
	}

	/*
	 * It will find total expenses between two given dates of respective user
	 */
	@Override
	public double sumExpenses(String stDate, String edDate, int userId)
	{
		LocalDate startDate = LocalDate.parse(stDate);
		LocalDate endDate = LocalDate.parse(edDate);
		
		/*
		 * i. get all expenses for respective user (by using viewExpenses(userId))
		 * ii. make use of stream API to filter expenses based on given startDate and endDate
		 * iii. from expensesDTO object take amount and convert into double list
		 * iv. perform summation of all elements present in double list
		 */
		
		// a) isAfter() and isBefore() and mapToDouble() and sum()
		
		//	getting all expenses of respective user and filtering by using isAfter() and IsBefore() of stream API 
		
		double totalExpenses1 =  viewExpenses(userId).stream().filter( expDTO ->
				// see not (!) operator
				! LocalDate.parse(expDTO.getDate()).isBefore(startDate) &&  ! LocalDate.parse(expDTO.getDate()).isAfter(endDate))
				.mapToDouble(t -> t.getAmount()).sum();
//			return sumExp;
//		System.err.println("sum = "+totalExpenses1);
		
		
		/* 	b)  compareTo() and  summingDouble() and :: (method reference operator) 
		 * 
		 * -----> validating expenses between two dates by using compareTo()
		 * 
		 * 		compareTo() :    	date1.compareTo(date2)
		 * 
		 * 		This method returns '0' if both the dates are equal,
		 *  it returns a value "greater than 0" if date1 is after date2 and 
		 *  it returns a value "less than 0" if date1 is before date2.
		 *
		 * 	-> Summing the amount of the expenses which are between the startDate and endDate 
		 *  -> we are fetching the expenses amount of expenses by filtering the expenses between the startDate and endDate 
		 *  -> by using compareTo() method of Comparable interface
		 */
		
		double totalExpenses2= viewExpenses(userId).stream().filter(expDTO ->
				LocalDate.parse(expDTO.getDate()).compareTo(startDate)>=0 && LocalDate.parse(expDTO.getDate()).compareTo(endDate)<=0)
			   .collect(Collectors.summingDouble(ExpensesDTO::getAmount));
//		System.err.println("total Sum = "+totalExpenses2);
		
		
		/*
		 * It is another way 
		 * 
		 * 1. at first we are finding all expenses between the two dates 
		 * 2. after that filtering by userId 
		 * 3. finally summing all the userAmount
		 */
		List<Expenses> expenses = expensesRepository.findByDateBetween(startDate, endDate);

		if (expenses != null) 
		{
			/*
			 * by for each loop
			 */
			double totalExpenses3 = 0;
			for (Expenses exp : expenses) 
			{
				if (exp.getUser().getUserId() == userId) 
				{
					totalExpenses3 += exp.getAmount();
				}
			}
//			System.err.println(totalExpenses3);
			
			/*
			 * by Stream API
			 */
			
			/*
			 * 1. filter()  
			 * 	 
			 * 	a) summingDouble() and :: (method reference operator)
			 * 
			 *  ----> validating byUsing isAfter() and isBefore() and userId
			 */
			double totalExpenses4 = expenses.stream().filter(exp -> exp.getUser().getUserId()==userId) 
					   .collect(Collectors.summingDouble(Expenses::getAmount));
			
//			System.err.println(totalExpenses4);
			
			
			/*
			 * b)  mapToDouble() and :: (method reference operator) and sum()
			 */
			double totalExpenses5  = expenses.stream().filter(exp->
					 exp.getUser().getUserId()==userId)
					.mapToDouble(Expenses::getAmount).sum();
//			System.err.println(totalExpenses5);
			
			
			/*
			 * 3. forEach()
			 * 
			 */
			
			totalExpenses6 = 0;
			expenses.stream().forEach(ex -> 
			{
				if(ex.getUser().getUserId()==userId)
				{
					totalExpenses6+=ex.getAmount();
				}
			});
//			System.err.println("total Expenses = " + totalExpenses6);
			
			return totalExpenses1;

		}
		return totalExpenses6;

	}
	
	/*
	 * to get expenses based on expensesId
	 */
	@Override
	public ExpensesDTO findByExpensesId(int expensesId) 
	{
		Optional<Expenses> expense =  expensesRepository.findById(expensesId);
		ExpensesDTO expensesDTO = new ExpensesDTO();
		if(expense.isPresent())
		{
			Expenses exp = expense.get();
			expensesDTO.setDate(exp.getDate().toString());
			
			ExpensesCatagory category = categoryRepository.findByCategory(exp.getCategory().getCategory());
			expensesDTO.setCategory(category.getCategory());
			BeanUtils.copyProperties(exp, expensesDTO);
		}
		return expensesDTO;
	}

	/*
	 * to delete expenses based on expensesId
	 */

	@Override
	public int deleteExpenses(int expensesId) 
	{
	
//		expensesRepository.deleteById(expensesId);
		
		// i). find expenses based on Id
		Optional<Expenses> fetchedExp =  expensesRepository.findById(expensesId);
		// ii). verify that expenses is present or not
		if(fetchedExp.isPresent())
		{
			//iii).  delete expenses by using delete(EntityObject)
			expensesRepository.delete(fetchedExp.get());
			return fetchedExp.get().getExpensesId();
		}
		return 0;

	}
	
	/*
	 * update expenses details
	 */

	@Override
	public ExpensesDTO updateExpenses(int expensesId, ExpensesDTO dto)
	{
		//1. find the expenses based expensesId
		Optional<Expenses> expense = expensesRepository.findById(expensesId);
		//2. verify expenses
		if (expense.isPresent()) 
		{
			Expenses exp = expense.get();
			
			exp.setAmount(dto.getAmount());
			exp.setDate(LocalDate.parse(dto.getDate()));
			ExpensesCatagory expensesCatagory =  categoryRepository.findByCategory(dto.getCategory());
			exp.setCategory(expensesCatagory);
			exp.setDescription(dto.getDescription());
			expensesRepository.save(exp);
		}
		return dto;
	}
	
	
	/*
	 * This method will retrieve data from data base based on matching category amount and date make use of filter method from stream API
	 */
	
	@Override
	public List<ExpensesDTO> filterBasedOnCategoryAmountDate(FilterExpensesDTO filterExpensesDTO, int userId) 
	{
		/*
		 * -->	call viewExpeses() method because it contains the logic, to get all expenses of respective use , so programmer have to just
		 * filter expenses of user as per requirement
		 */
		
		//1. split the amount at '-'
			String[] amt = filterExpensesDTO.getAmountRange().split("-");
			
			//2. convert the string into int/double by using parse methods
			double minAmount = Double.parseDouble(amt[0]);
			double maxAmount = Double.parseDouble(amt[1]);
			
			//3. first find the expenses of user by viewExpenses() and then filter the expenses using stream API 
			List<ExpensesDTO> expensesDTOList = viewExpenses(userId).stream().filter( expDTO ->
						! LocalDate.parse(expDTO.getDate()).isBefore(LocalDate.parse(filterExpensesDTO.getStartDate())) && 
						! LocalDate.parse(expDTO.getDate()).isAfter(LocalDate.parse(filterExpensesDTO.getEndDate())) &&
						expDTO.getAmount()>=minAmount && expDTO.getAmount()<=maxAmount &&
						expDTO.getCategory().equals(filterExpensesDTO.getCategory()) ).toList();
			
			return expensesDTOList;
	}

	
	/*
	 * This method will retrieve data from data base based on date make use of filter method of stream API
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnDate(FilterExpensesDTO filterExpensesDTO,int userId) 
	{
//		by using stream API filter()
		List<ExpensesDTO> expensesDTOList = viewExpenses(userId).stream().filter( expDTO -> 
//			expdto.getDate().compareTo(date)<=0 // compares date
			/*
			 * see Not(!) operator the dates must not 
			 * 	--> before startDate and after endDate
			 */
			! LocalDate.parse(expDTO.getDate()).isBefore(LocalDate.parse(filterExpensesDTO.getStartDate())) && 
			! LocalDate.parse(expDTO.getDate()).isAfter(LocalDate.parse(filterExpensesDTO.getEndDate())))
			.toList();
		
		return expensesDTOList;
	}
	
	/*
	 *  This method will filter the expenses data of a user based on Amount Range (EX 100-200)  
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnAmount(String amountRange,int userId) 
	{
		//1. split the amount at '-'
		String[] amt = amountRange.split("-");
		
		//2. convert the string into int/double by using parse methods
		double minAmount = Double.parseDouble(amt[0]);
		double maxAmount = Double.parseDouble(amt[1]);
		
		//3. first find the expenses of user by viewExpenses() and then filter the expenses using stream API 
		List<ExpensesDTO> expensesDTOList = viewExpenses(userId).stream().filter( expdto ->
			expdto.getAmount()>=minAmount && expdto.getAmount()<=maxAmount )
			.toList();
		
		expensesDTOList.forEach(e-> System.err.println(e.getAmount()));
		return expensesDTOList;
	}
	
	/*
	 * This method will filter the expenses of a user based on category 
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnCategory(String category, int userId) 
	{
		List<ExpensesDTO> expensesDTOList =  viewExpenses(userId).stream().filter(exp-> 
												exp.getCategory().equals(category)).toList();
												expensesDTOList.forEach(e-> System.err.println(e.getAmount()));
		return expensesDTOList;
	}
	
	
	/*
	 * This method will filter the expenses of a user based on category and date 
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnDateAndCategory(FilterExpensesDTO filterExpensesDTO , int userId) 
	{
		// -> first find the expenses of user by using viewExpenses() and then filter the expenses based on requirement using Stream API
		List<ExpensesDTO> expensesDTOList = viewExpenses(userId).stream().filter(expDto -> 
		! LocalDate.parse(expDto.getDate()).isBefore(LocalDate.parse(filterExpensesDTO.getStartDate())) && 
		! LocalDate.parse(expDto.getDate()).isAfter(LocalDate.parse(filterExpensesDTO.getEndDate())) && 
			expDto.getCategory().equals(filterExpensesDTO.getCategory()))
			.toList();
		return expensesDTOList;
		
	}
	
	/*
	 * This method will filter the expenses of a user based on date and amount Range 
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnDateAndAmount(FilterExpensesDTO filterExpensesDTO, int userId) 
	{
		//1. split the amount at '-'
		String[] amt = filterExpensesDTO.getAmountRange().split("-");
		
		//2. convert the string into int/double by using parse methods
		double minAmount = Double.parseDouble(amt[0]);
		double maxAmount = Double.parseDouble(amt[1]);
		
		//3. first find the expenses of user by viewExpenses() and then filter the expenses using stream API 
		List<ExpensesDTO> expensesDTOList = viewExpenses(userId).stream().filter( expDTO ->
		! LocalDate.parse(expDTO.getDate()).isBefore(LocalDate.parse(filterExpensesDTO.getStartDate())) && 
		! LocalDate.parse(expDTO.getDate()).isAfter(LocalDate.parse(filterExpensesDTO.getEndDate())) && 
			expDTO.getAmount()>=minAmount && expDTO.getAmount()<=maxAmount  )
			.toList();
		
		return expensesDTOList;
	}
	
	
	
	/*
	 * This method will filter the expenses of a user based on category and amountRange 
	 */
	@Override				 
	public List<ExpensesDTO> filterBasedOnCategoryAndAmount(FilterExpensesDTO filterExpensesDTO , int userId) 
	{
		
		//1. split the amount at '-'
		String[] amt = filterExpensesDTO.getAmountRange().split("-");
		
		//2. convert the string into int/double by using parse methods
		double minAmount = Double.parseDouble(amt[0]);
		double maxAmount = Double.parseDouble(amt[1]);
		
		//3. first find the expenses of user by viewExpenses() and then filter the expenses using stream API 
		List<ExpensesDTO> expensesDTOList = viewExpenses(userId).stream().filter( expDTO ->
			expDTO.getAmount()>=minAmount && expDTO.getAmount()<=maxAmount && expDTO.getCategory().equals(filterExpensesDTO.getCategory()) )
			.toList();
		
		return expensesDTOList;
	}
	
}