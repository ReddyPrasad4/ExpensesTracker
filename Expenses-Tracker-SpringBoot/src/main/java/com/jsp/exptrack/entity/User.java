package com.jsp.exptrack.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
@Entity
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	private String fullName;
	private String userName;

	@Column(unique = true)
	private String mobileNo;
	private String email;
	private String password;

	// Bi-directional traversing
	@OneToMany(mappedBy = "user")
	private List<Expenses> expenses;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id")
	private Image image;

	public User(String fullName, String userName, String mobileNo, String email, String password) {
		super();
		this.fullName = fullName;
		this.userName = userName;
		this.mobileNo = mobileNo;
		this.email = email;
		this.password = password;
	}
	
	

}
