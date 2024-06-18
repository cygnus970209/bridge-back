package com.project.bridge.service;

public interface EmailService {
    public boolean sendEmail(String email, String title, String message);
}
