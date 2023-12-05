package com.shoponlineback.email;

import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
public class EmailService {
    private Session session;
    @Value("${EMAIL_SERVER}")
    private String mailServer;
    @Value("${EMAIL_USERNAME}")
    private String username;
    @Value("${EMAIL_PASSWORD}")
    private String password;

    @PostConstruct
    public void init(){
        this.session  = setEmailConfigurationProperties();
    }

    public void sendActivationLink(String link, String recipientEmail) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("kontakt@mowebcreations.pl"));
        message.setSubject("Email confirmation");
        message.setContent("<a href=\"" + link + "\">" + "Click here to activate account</a>", "text/html");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        Transport.send(message);

    }

    public void sendPasswordResetEmail(String link, String emailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("kontakt@mowebcreations.pl"));
        mimeMessage.setSubject("Password reset");
        mimeMessage.setContent("<a href=\"" + link + "\">" + "Click here to activate account</a>", "text/html");
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        Transport.send(mimeMessage);

    }

    private Session setEmailConfigurationProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", mailServer);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.host", mailServer);
        properties.put("mail.smtp.port", 465);
        return Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );
    }

}

