package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.model.document.User;
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

    public void sendRegistrationMail(User user, String token) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_NO, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariable("fullName", user.getNome() + " " + user.getCognome());
        context.setVariable("token", token);
        String html = templateEngine.process("mail-template", context);

        helper.setTo(user.getEmail());
        helper.setSubject("Welcome ".concat(user.getNome()));
        helper.setText(html, true);
        mailSender.send(message);

    }
}
