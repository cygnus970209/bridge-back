package com.project.bridge.service;

public interface MailService {

    boolean sendEmail(String from, String to, String subject, String content);
}
