package com.shoponlineback.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {


    public void sendActivationLink(String link, String recipientEmail) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.host", "mail-serwer233426.lh.pl");
        properties.put("mail.smtp.port", 465);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("kontakt@mowebcreations.pl", "Lechpoznan1!");
            }}
        );

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("kontakt@mowebcreations.pl"));
        message.setSubject("Email confirmation");
        message.setText("Click on link to activate account: " + link);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        Transport.send(message);

        }
    }

