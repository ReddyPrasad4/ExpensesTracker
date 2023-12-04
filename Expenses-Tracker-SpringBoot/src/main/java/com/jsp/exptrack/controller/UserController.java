package com.jsp.exptrack.controller;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.exptrack.dto.ImageDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.serivce.UserService;

@Controller
public class UserController 
{
	
	@Autowired 
	private UserService userService;
	
	
	@PostMapping("/register")
	public String regesterUser(@ModelAttribute UserDTO userDTO,RedirectAttributes attributes)
	{
		
		int userId = userService.registration(userDTO);
		if(userId!=0)
		{
			attributes.addFlashAttribute("msg", userDTO.getUserName()+" registered sucessfully");
			return "redirect:/project/sgnInPage";
		}
		attributes.addFlashAttribute("msg","Enter valid Information");
		return "redirect:/project/sgnUpPage";

	}
	
	@PostMapping("/loginUser")
	public String loginUser(@ModelAttribute UserDTO userDTO,RedirectAttributes attributes,HttpServletRequest request )
	{
		UserDTO verifiedUser = userService.verifyLogin(userDTO.getUserName(), userDTO.getPassword());
		if(verifiedUser!=null)
		{
			
			// to store data in session object
			request.getSession().setAttribute("userDTO", verifiedUser);
			
			if(verifiedUser.getImageDTO()!=null)
			{
				request.getSession().setAttribute("image", Base64.getMimeEncoder().encodeToString(verifiedUser.getImageDTO().getData()));
			}
			return "redirect:/project/homePage";
		}
		attributes.addFlashAttribute("msg","Enter valid Details");
		return "redirect:/project/sgnInPage";
		
	}
	
	
//   @RequestMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") int imageId) 
//   {
//        byte[] imageContent = userService.findByImageId(imageId).getData();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
//    }
	
	
	@PostMapping("/resetPassword")
	public String forgotPassword(@ModelAttribute UserDTO userDTO,RedirectAttributes attributes,HttpServletRequest request)
	{
		
		UserDTO dto =  userService.resetPassword(userDTO);
		if(dto!=null)
		{
			attributes.addFlashAttribute("msg",dto.getUserName() +"'s  Password changed sucessfully ....");
			return "redirect:/project/sgnInPage";
		}
		attributes.addFlashAttribute("msg","Enter valid Details");
		return "redirect:/project/forgotPassword";
	}
	
	@PostMapping("/updateUser")
	public String updateUser(@ModelAttribute UserDTO userDTO,RedirectAttributes attributes,HttpServletRequest request , @RequestParam("imageFile") MultipartFile file)
	{
		// fetching the user details from session object
		UserDTO udto =  (UserDTO) request.getSession().getAttribute("userDTO");
		
		try 
		{
			
			if(udto.getImageDTO()!=null)
			{
				// first finding the user from image details by using the imageId
				ImageDTO imageData = userService.findByImageId(udto.getImageDTO().getId());	
				
				if(! file.isEmpty())
				{
					// updating the new image data of user
					imageData.setData(file.getBytes());
					userDTO.setImageDTO(imageData);
					
					// storing the image data inside the session object
					request.getSession().setAttribute("image", Base64.getMimeEncoder().encodeToString(file.getBytes()));
				}
				
			}
			else
			{
				ImageDTO imageDTO = new ImageDTO();
				imageDTO.setData(file.getBytes());
				userDTO.setImageDTO(imageDTO);
				
				// storing the image data inside the session object
				request.getSession().setAttribute("image", Base64.getMimeEncoder().encodeToString(file.getBytes()));
			}
			
			UserDTO dto =  userService.updateUserDetails(userDTO,udto.getUserId());
			if(dto!=null)
			{
				// updating the user data in the session object
				request.getSession().setAttribute("userDTO", dto);
				
				attributes.addFlashAttribute("msg",userDTO.getUserName() +" details updated....");
				return "redirect:/project/homePage";
			}
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		attributes.addFlashAttribute("msg","Enter valid Details");
		return "redirect:/project/update";
	}
	
	
	@GetMapping("/delete")
	public String deleteUser(HttpServletRequest request,RedirectAttributes attributes)
	{
		UserDTO userDTO =  (UserDTO) request.getSession().getAttribute("userDTO");
		int msg = userService.deleteUser(userDTO);
		if(msg==1)
		{
			attributes.addFlashAttribute("msg", userDTO.getUserName()+" deleted sucessfully sucessfully");
			return "redirect:/project/index";
		}
		attributes.addFlashAttribute("msg","Enter valid Information");
		return "redirect:/project/update";

	}
	
}
