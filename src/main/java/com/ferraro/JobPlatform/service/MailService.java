package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.model.Account;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendRegistrationMail(Account account, String token) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_NO, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariable("token", token);
        String html = templateEngine.process("mail-template", context);

        helper.setTo(account.getEmail());
        helper.setSubject("Welcome to Job Portal");
        helper.setText(html, true);
        mailSender.send(message);

    }
}
