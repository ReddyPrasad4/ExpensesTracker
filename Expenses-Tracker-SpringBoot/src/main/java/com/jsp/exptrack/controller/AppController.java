package com.jsp.exptrack.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.exptrack.dto.ExpensesDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.entity.User;
import com.jsp.exptrack.serivce.ExpensesService;

@Controller
@RequestMapping("/project")
public class AppController
{
	@Autowired
	private ExpensesService expensesService;
	
	
	@RequestMapping("/index")
	public String showIndex()
	{
		return "index";
	}
	
	@RequestMapping("/sgnUpPage") 
	public String showSignUpPage(@ModelAttribute("msg") String message )
	{
		return "SignUpPage";
	}
	
	@RequestMapping("/sgnInPage") 
	public String showSignInPage(@ModelAttribute("msg") String message,HttpServletRequest request)
	{
		return "SignInPage";
	}
	
	
	@RequestMapping("/forgotPassword") 
	public String showForgotPassowrdPage()
	{
		return "ForgotPasswordPage";
	}
	
	
	@RequestMapping("/update")
	public String showUpdateProfile()
	{
		return "UpdateProfilePage";
	}
	
	
	@RequestMapping("/homePage")
	public String showHomePage()
	{
		return "HomePage";
	}
	
	@RequestMapping("/addExpenses") 
	public String showAddExp()
	{
		return "AddExpensesPage";
	}
	
	
	@RequestMapping("/viewExp")
	public String showViewExp(@ModelAttribute("listOfExpensesDTO") List<ExpensesDTO> expensesDTOList,@ModelAttribute("msg") String messsage)
	{
		return "ViewExpensesPage";
	}
	
	// to display UpdateExpensesPage with expenses details
	@RequestMapping("updateExpenses/{expId}")
	public String updateExpenses(@PathVariable("expId") int expensesId,Model model)
	{
		ExpensesDTO expensesDTO =  expensesService.findByExpensesId(expensesId);
		model.addAttribute("expensesDTO", expensesDTO);
		return "UpdateExpensesPage";
	}
	
	@RequestMapping("/totalExp")
	public String showTotalExp()
	{
		return "TotalExpensesPage" ;
	}
	
	@RequestMapping("/filterExp")
	public String showFilterExp()
	{
		return "FilterExpensesPage";
	}
	@RequestMapping("/aboutUs")    
	public String showAboutUsPage()    
	{                                
		return "AboutUSPage";    
	}                                
}                                    
