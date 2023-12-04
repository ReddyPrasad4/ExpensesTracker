package com.jsp.exptrack.serivce;

import com.jsp.exptrack.dto.ImageDTO;
import com.jsp.exptrack.dto.UserDTO;
import com.jsp.exptrack.entity.User;

public interface UserService
{
	UserDTO findUserById(int userId);
	
	int registration(UserDTO dto);
	
	UserDTO verifyLogin(String name, String pass);
	
	UserDTO resetPassword(UserDTO userDTO);

	UserDTO updateUserDetails(UserDTO userDTO,int userId);
	
	ImageDTO findByImageId(int imageid);
	
	ImageDTO findByImageData(byte[] data);
	
	int deleteUser(UserDTO userDTO);
}
