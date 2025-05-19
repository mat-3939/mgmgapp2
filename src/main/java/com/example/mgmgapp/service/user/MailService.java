package com.example.mgmgapp.service.user;

import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.mgmgapp.entity.CartItems;
import com.example.mgmgapp.form.OrderForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendOrderMail(OrderForm form, List<CartItems> cartItems, int total) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(form.getEmail());
            helper.setSubject("ご注文ありがとうございます");

            Context context = new Context();
            context.setVariable("form", form);
            context.setVariable("cartItems", cartItems);
            context.setVariable("total", total);

            String htmlContent = templateEngine.process("mail/order_mail", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("メール送信に失敗しました", e);
        }
    }
}
