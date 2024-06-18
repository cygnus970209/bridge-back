package com.project.bridge.service.impl;

import com.project.bridge.config.MailManager;
import com.project.bridge.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailManager mailManager;

    @Override
    public boolean sendEmail(String email, String title, String message) {
        try {
            mailManager.send(email, title, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
