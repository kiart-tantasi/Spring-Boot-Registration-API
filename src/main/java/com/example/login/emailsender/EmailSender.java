package com.example.login.emailsender;

public interface EmailSender {
    public void sendEmail(String destination, String emailContent);
}
