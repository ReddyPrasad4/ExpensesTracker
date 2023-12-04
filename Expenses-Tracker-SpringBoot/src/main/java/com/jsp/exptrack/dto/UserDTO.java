package com.jsp.exptrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO 
{
	private int userId;
	private String fullName;
	private String userName;
	
	private String mobileNo;
	private String email;
	private String password;
	private String rePass;
	
	private ImageDTO imageDTO;
	
//	private byte[] data;
}
	