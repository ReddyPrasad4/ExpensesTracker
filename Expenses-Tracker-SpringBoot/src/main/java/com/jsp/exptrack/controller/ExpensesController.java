package com.jsp.exptrack.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.exptrack.dto.ExpensesDTO;
import com.jsp.exptrack.dto.FilterExpensesDTO;
import com.jsp.exptrack.dto.TotEXPDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.serivce.ExpensesService;

@Controller
public class ExpensesController
{
	@Autowired
	private ExpensesService expensesService;
	
	// adding user Expenses to data base
	@PostMapping("/addExpen/{userId}")
	public String addExpensesToDB(@ModelAttribute ExpensesDTO expensesDTO ,@PathVariable("userId") int userId, RedirectAttributes attributes)
	{
		int expId = expensesService.addExpenses(expensesDTO,userId);
		if(expId!=0)
		{
			attributes.addFlashAttribute("msg","Expenses added sucessfully");
			
			// it is ExpensesController viewExpenses() not AppController viewExpenses()
			return "redirect:/viwExpenses/"+userId;
		}
		attributes.addFlashAttribute("msg","Enter valid details");
		return "redirect:/project/addExpenses";
	}
	
	
	// fetching all the expenses details of user based on user Id
	@GetMapping("/viwExpenses/{userId}")
	public String viewExpenses(@PathVariable("userId") int userId,RedirectAttributes attributes)
	{	
		List<ExpensesDTO> expenses = expensesService.viewExpenses(userId);
		if(!expenses.isEmpty())
		{
			
			attributes.addFlashAttribute("listOfExpensesDTO",expenses);
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("msg","No Expenses Found..!!!!");
		attributes.addFlashAttribute("listOfExpensesDTO",expenses);
		return "redirect:/project/viewExp";
	}
	
	// finding the total of expenses cost between two dates
	@GetMapping("/totExpenses/{userId}")
	public String totalExpenses(@ModelAttribute  TotEXPDTO totEXPDTO,@PathVariable("userId") int userId,RedirectAttributes attributes)
	{
		if(totEXPDTO.getStartDate().compareTo(totEXPDTO.getEndDate())<0)
		{
			double totalExpen = expensesService.sumExpenses(totEXPDTO.getStartDate(),totEXPDTO.getEndDate(),userId);
			List<ExpensesDTO> exp =  expensesService.filterBetweenDates(totEXPDTO.getStartDate(), totEXPDTO.getEndDate(), userId);
			if(totalExpen>0)
			{
				attributes.addFlashAttribute("msg","Total Expenses Between "+totEXPDTO.getStartDate()+" and "+totEXPDTO.getEndDate()+" is : "+totalExpen);
				attributes.addFlashAttribute("listOfExpensesDTO",exp);
				return "redirect:/project/viewExp";
			}
			attributes.addFlashAttribute("msg","No Expenses found Between "+totEXPDTO.getStartDate()+" and "+totEXPDTO.getEndDate()+"..???");
			attributes.addFlashAttribute("listOfExpensesDTO",exp);
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("msg","Enter proper details ");
		return "redirect:/project/totalExp";
	}
	
	
	
	// updating the user details
	@RequestMapping("/updateExpense/{expId}")
	public String updateExpense(@PathVariable("expId") int expenseId ,@ModelAttribute ExpensesDTO expensesDTO , HttpServletRequest request ) 
	{
		ExpensesDTO expDTO =  expensesService.updateExpenses(expenseId, expensesDTO);
		
		UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");
		if(expDTO!=null)
		{
			return "redirect:/viwExpenses/"+userDTO.getUserId();
		}
		return "redirect:/addExpen/"+userDTO.getUserId();
	}
	
	// deleting the user expense
	@GetMapping("/deleteExpense/{expId}")
	public String deleteExpense(@PathVariable("expId") int expenseId , HttpServletResponse response ,RedirectAttributes attributes,HttpServletRequest request)
	{
		int expId = expensesService.deleteExpenses(expenseId);
		UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");
		return "redirect:/viwExpenses/"+userDTO.getUserId();
	}

	
	@GetMapping("/filterExpensesAccordingTOData")
	public String filterExpenses(@ModelAttribute FilterExpensesDTO expensesDTO,RedirectAttributes attributes)
	{
		// conditions for filter
		// 1. If dates data is entered
		if(!expensesDTO.getStartDate().isBlank() && !expensesDTO.getEndDate().isBlank())
		{
			LocalDate fromDate = LocalDate.parse(expensesDTO.getStartDate());
			LocalDate toDate = LocalDate.parse(expensesDTO.getEndDate());
			
			if( fromDate.isAfter(toDate))
			{
				attributes.addFlashAttribute("msg"," Enter the End Date greater then Start Date..");
				return "redirect:/project/filterExp";
			}
			else
			{
				attributes.addFlashAttribute("filterExpensesDTO",expensesDTO);
				
				// a. checking all the fields are entered
				if(!expensesDTO.getAmountRange().isBlank() && !expensesDTO.getCategory().isBlank()&& !expensesDTO.getStartDate().isBlank() && !expensesDTO.getEndDate().isBlank())
				{
					return "redirect:/filter/Expense1";
				}
				// b. checking dates and category fields are entered
				else if( !expensesDTO.getStartDate().isBlank() && !expensesDTO.getEndDate().isBlank()&& !expensesDTO.getCategory().isBlank())
				{
					return "redirect:/filter/expenses2";
				}
				// c. checking dates and amount range fields are entered
				else if( !expensesDTO.getStartDate().isBlank() && !expensesDTO.getEndDate().isBlank()&& !expensesDTO.getAmountRange().isBlank())
				{
					return "redirect:/filter/expenses3";
				}
				// d. only dates are entered
				else
				{
					return "redirect:/filter/expenses5";
				}
			}
		}
		// 2. checking only category and amountRange fields are entered
		else if( !expensesDTO.getCategory().isBlank()&&!expensesDTO.getAmountRange().isBlank())
		{
			if( ! expensesDTO.getAmountRange().contains("-"))
			{
				attributes.addFlashAttribute("msg"," Enter Proper Range of amount ");
				return "redirect:/project/filterExp";
			}
			attributes.addFlashAttribute("filterExpensesDTO",expensesDTO);
			return "redirect:/filter/expenses4";
		}
		// 3. checking only category field is entered
		else if(!expensesDTO.getCategory().isBlank())
		{
			attributes.addFlashAttribute("filterExpensesDTO",expensesDTO);
			return "redirect:/filter/expenses6";
		}
		// 4. checking only amountRange field is entered
		else if(!expensesDTO.getAmountRange().isBlank())
		{
			if( ! expensesDTO.getAmountRange().contains("-"))
			{
				attributes.addFlashAttribute("msg"," Enter Proper Range of amount ");
				return "redirect:/project/filterExp";
			}
			return "redirect:/filter/expenses7/"+expensesDTO.getAmountRange();
		}
		// 5. Nothing is entered
		else
		{
			attributes.addFlashAttribute("msg"," Enter Any of the details");
			return "redirect:/project/filterExp";
		}
	}
	
	// fetching data based on dates , category and amount range
	@GetMapping("/filter/Expense1")
	public String filterBasedOnDateAndCategoryAndAmount(@ModelAttribute("filterExpensesDTO") FilterExpensesDTO filterDTO,HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO =  (UserDTO)request.getSession().getAttribute("userDTO");
		List<ExpensesDTO> expensesDTOList =  expensesService.filterBasedOnCategoryAmountDate(filterDTO, userDTO.getUserId());
		if(!expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("msg","Total No of  Expneses = "+expensesDTOList.size());
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	}
	
	
	// fetching data based on dates and category 
	@GetMapping("/filter/expenses2")
	public String filterExpensesByDateAndCategory(@ModelAttribute("filterExpensesDTO") FilterExpensesDTO filterDTO ,HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");
		List<ExpensesDTO> expensesDTOList =  expensesService.filterBasedOnDateAndCategory(filterDTO, userDTO.getUserId());
		if(!expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			attributes.addFlashAttribute("msg","Total No of Expneses = "+expensesDTOList.size());
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	}
		
	
	// fetching data based on dates and  amount range
	@GetMapping("/filter/expenses3")
	public String filterExpensesByDateAndAmount(@ModelAttribute("filterExpensesDTO") FilterExpensesDTO filterDTO ,HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");
		List<ExpensesDTO> expensesDTOList =  expensesService.filterBasedOnDateAndAmount(filterDTO , userDTO.getUserId());
		if(!expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			attributes.addFlashAttribute("msg","Total No of Expneses = "+expensesDTOList.size());
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	
	}
	
	
	// fetching data based on category and amount range
	@GetMapping("/filter/expenses4")
	public String filterExpensesByCategoryAndAmount(@ModelAttribute("filterExpensesDTO") FilterExpensesDTO filterDTO ,HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");
		List<ExpensesDTO> expensesDTOList =  expensesService.filterBasedOnCategoryAndAmount(filterDTO, userDTO.getUserId());
		if(!expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			attributes.addFlashAttribute("msg","Total No of Expneses = "+expensesDTOList.size());
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	
	}
	
	
	// fetching data based on dates
	@GetMapping("/filter/expenses5")
	public String filterExpensesByDate(@ModelAttribute("filterExpensesDTO") FilterExpensesDTO filterDTO ,HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");
		List<ExpensesDTO> expensesDTOList =  expensesService.filterBasedOnDate(filterDTO, userDTO.getUserId());
		if(!expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			attributes.addFlashAttribute("msg","Total No of Expneses = "+expensesDTOList.size());
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	
	}
	
	// fetching data based on category
	@GetMapping("/filter/expenses6")
	public String filterExpensesByCategory(@ModelAttribute("filterExpensesDTO") FilterExpensesDTO filterDTO, HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO =  (UserDTO) request.getSession().getAttribute("userDTO");
		List<ExpensesDTO>  expensesDTOList = expensesService.filterBasedOnCategory(filterDTO.getCategory(),userDTO.getUserId()); 
		if(! expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("msg","Total No of Expneses = "+expensesDTOList.size());
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	}
	
	
	// fetching data based on amount range
	@GetMapping("/filter/expenses7/{range}")
	public String filterExpensesByAmount(@PathVariable("range") String amountRange, HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO =  (UserDTO) request.getSession().getAttribute("userDTO");
		List<ExpensesDTO>  expensesDTOList = expensesService.filterBasedOnAmount(amountRange,userDTO.getUserId()); 
		if(! expensesDTOList.isEmpty())
		{
			attributes.addFlashAttribute("msg","Total No of Expneses = "+expensesDTOList.size());
			attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
			return "redirect:/project/viewExp";
		}
		attributes.addFlashAttribute("listOfExpensesDTO",expensesDTOList);
		attributes.addFlashAttribute("msg","No Expenses Found....!!!!");
		return "redirect:/project/viewExp";
	}
	
	
	
	
	
	
	
	
	
}
