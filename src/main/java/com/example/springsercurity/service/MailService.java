package com.example.springsercurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    public JavaMailSender emailSender;
    public void sendEmail(String user,String subject,String content){
            // Create a Simple MailMessage.
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(user);
            message.setSubject(subject);
            message.setText(content);

            // Send Message!
            emailSender.send(message);

        }


}
