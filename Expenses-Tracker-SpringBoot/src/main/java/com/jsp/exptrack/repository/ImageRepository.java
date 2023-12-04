package com.jsp.exptrack.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.jsp.exptrack.entity.Image;

public interface ImageRepository extends JpaRepositoryImplementation<Image, Integer> 
{
	Image findByData(byte[] data);
}
