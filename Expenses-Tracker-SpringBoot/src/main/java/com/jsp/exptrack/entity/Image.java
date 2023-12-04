package com.jsp.exptrack.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Image
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Lob
	private byte[] data;
}
