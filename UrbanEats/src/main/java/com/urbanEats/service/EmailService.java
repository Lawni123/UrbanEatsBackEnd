package com.urbanEats.service;

public interface EmailService {
	public void sendMail(String to, String subject, String body);
}
