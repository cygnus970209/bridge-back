package com.project.bridge.service.Impl;

import com.project.bridge.service.MailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public boolean sendEmail(String from, String to, String subject, String content) {
        SimpleMailMessage emailForm = getSimpleMailMessage(from, to, subject, content);
        try{
            //이메일 정보를 받아 전송
            mailSender.send(emailForm);
            return true;
        }catch (RuntimeException e){
            log.debug(e.getMessage());
            return false;
        }
    }

    private SimpleMailMessage getSimpleMailMessage(String from, String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content.toString());
        return message;
    }
}
