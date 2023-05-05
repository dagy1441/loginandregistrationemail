package com.dagy.loginandregistrationemail.email;

import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendMail(NotificationEmail notificationEmail);

    void sendMail(String to, String email) throws MessagingException;
}
