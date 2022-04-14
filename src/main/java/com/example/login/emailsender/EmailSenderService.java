package com.example.login.emailsender;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailSenderService implements EmailSender{

    // logger
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);
    // injecting mail sender
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String destination, String emailContent) {
        try {
            // mineMessage from JavaMailSender
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // helper
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            // config helper
            mimeMessageHelper.setText(emailContent, true); // email param and set content type to html
            mimeMessageHelper.setTo(destination); // destination param
            mimeMessageHelper.setSubject("Email Confirmation");
            mimeMessageHelper.setFrom("company@example.com");

            // send the mime-message after finishing config
            mailSender.send(mimeMessage);

        } catch (MessagingException ex) {
            LOGGER.error("failed sending e-mal.", ex);
            ex.printStackTrace();
            System.out.println(ex);
            throw new IllegalStateException("failed to send e-mail"); // for 500 Interval Server Error
        }
    }

}
