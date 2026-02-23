package com.urbanEats.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
	public Map uploadFile(MultipartFile file);

	public boolean deleteFile(String publicId);
}
