package com.jsp.exptrack.serivce;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.exptrack.dto.ImageDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.entity.Expenses;
import com.jsp.exptrack.entity.Image;
import com.jsp.exptrack.entity.User;
import com.jsp.exptrack.repository.ExpensesRepository;
import com.jsp.exptrack.repository.ImageRepository;
import com.jsp.exptrack.repository.UserRepository;


// it contains business logics
@Service
public class UserSeviceImple implements UserService
{

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ExpensesRepository expensesRepository;
	
	@Override
	public UserDTO findUserById(int userId) 
	{
		Optional<User> user =  repository.findById(userId);
		if(user.isPresent())
		{
			UserDTO dto = new UserDTO();
			User verifiedUser = user.get();
			BeanUtils.copyProperties(verifiedUser , dto);
			
			ImageDTO imgDto = new ImageDTO();
			BeanUtils.copyProperties(verifiedUser.getImage(), imgDto);
			dto.setImageDTO(imgDto);
			return dto;
		}
		return null;
	}
	
	@Override
	public int registration(UserDTO dto) 
	{
//		create object of entity class 
		User user = new User();
		
//		transfer data from DTO object to Entity object
		
		/*	
		 *  dto.setFullName(user.getFullName());
		 *	dto.setUserName(user.getUserName());
		 *	dto.setMobile(user.getMobileNo());
		 *	dto.setEmail(user.getEmail());
		 *	dto.setPassword(user.getPassword());
		 *	
		 */
		
		if(dto.getPassword().equals(dto.getRePass()))
		{
	//		use a inbuilt method give by Spring Frame Work for copying the data from one object to another object
			BeanUtils.copyProperties(dto, user);
			
			User insertedUser = repository.save(user);
			
			return insertedUser.getUserId();
		}
		return 0;
	}
	
	@Override
	public UserDTO  verifyLogin(String name, String pass)
	{
		User user =  repository.findByUserNameAndPassword(name, pass);
		
		if(user!=null)
		{
			UserDTO verifiedUser = new UserDTO();
			BeanUtils.copyProperties(user, verifiedUser);
		
			// to store ImageDTO in UserDTO
			if(user.getImage()!=null)
			{
				ImageDTO imageDTO = new ImageDTO();
				BeanUtils.copyProperties(user.getImage(), imageDTO);
				verifiedUser.setImageDTO(imageDTO);
			}
			return verifiedUser;
			
		}
		return null;     
	}
	
	@Override
	public UserDTO updateUserDetails(UserDTO userDTO,int userId)
	{
		
		Optional<User> userOPT =  repository.findById(userId);
		if(userOPT.isPresent())
		{
			User user = userOPT.get();
			
			user.setFullName(userDTO.getFullName());
			user.setUserName(userDTO.getUserName());
			user.setMobileNo(userDTO.getMobileNo());
			user.setEmail(userDTO.getEmail());
			
			// to add image object into user object 
			if(userDTO.getImageDTO()!=null)
			{
				Image image  = new Image();
				BeanUtils.copyProperties(userDTO.getImageDTO(),image);
				
				user.setImage(image);
			}
			User updatedUser = repository.save(user);
			UserDTO updatedUserDTO = new UserDTO();
			BeanUtils.copyProperties(updatedUser, updatedUserDTO);
			
			ImageDTO imgDto = new ImageDTO();
			BeanUtils.copyProperties(updatedUser.getImage(), imgDto);
			updatedUserDTO.setImageDTO(imgDto);
			
			
			return updatedUserDTO;
		}
		return null;
	}
	
	@Override
	public UserDTO resetPassword(UserDTO userDTO) 
	{
		// checking email is present in our data base or not
		User user = repository.findByEmail(userDTO.getEmail());
		
		if(user != null && userDTO.getPassword().equals(userDTO.getRePass()))
		{
			user.setPassword(userDTO.getPassword());
			
			User changedUser  = repository.save(user);
			UserDTO dto = new UserDTO();
			BeanUtils.copyProperties(changedUser, dto);
			return dto;
		}
		return null;
	}
	@Override
	public int deleteUser(UserDTO userDTO) 
	{
		Optional<User> optUser =  repository.findById(userDTO.getUserId());
		if(optUser.isPresent())
		{
			User user = optUser.get();
			
			List<Expenses>  userExpenses =  expensesRepository.findByUser(user);
			
			expensesRepository.deleteAll(userExpenses);
			
			imageRepository.deleteById(userDTO.getImageDTO().getId());
			repository.delete(user);
			
			return 1;
		}
		return 0;
	}
	
	@Override
	public ImageDTO findByImageId(int imageid) 
	{
		if(imageid!=0)
		{
			Optional<Image> image = imageRepository.findById(imageid);
			ImageDTO dto = new ImageDTO();
			BeanUtils.copyProperties(image.get(), dto);
			return dto;
		}
		return null;
	}
	
	@Override
	public ImageDTO findByImageData(byte[] data)
	{
		Image image =  imageRepository.findByData(data);
		ImageDTO imageDTO = new ImageDTO();
		BeanUtils.copyProperties(image, imageDTO);
		return imageDTO;
	}
	
	
}
