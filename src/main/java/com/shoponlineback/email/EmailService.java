package com.shoponlineback.email;

import com.shoponlineback.order.Order;
import com.shoponlineback.orderProduct.OrderProduct;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.shippingAddress.ShippingAddress;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import static com.shoponlineback.user.UserService.*;

@Service
public class EmailService {
    private final OrderProductRepository orderProductRepository;
    private Session session;
    @Value("${EMAIL_SERVER}")
    private String mailServer;
    @Value("${EMAIL_USERNAME}")
    private String username;
    @Value("${EMAIL_PASSWORD}")
    private String password;

    public EmailService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @PostConstruct
    public void init() {
        this.session = setEmailConfigurationProperties();
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

    public void sendOrderConfirmationEmail(Order order) throws MessagingException {
        List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(order.getId());
        ShippingAddress sa = order.getShippingAddress();
        final String content = String.format("""
                <body>
                    <h1>ORDER SUMMARY</h1>
                    <p>BILLING ADDRESS:</p>
                     <table>
                        <tr>
                            <th>Address</th>
                            <th>City</th>
                            <th>Country</th>
                            <th>Postal code</th>
                            <th>Phone number</th>
                        </tr>
                        <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        </tr>
                    </table>
                    <p>PAYMENT METHOD: %s</p>
                    <p>PRODUCTS:</p>
                    <table>
                        <tr>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Price</th>
                        </tr>
                        %s
                    </table>
                    <h2>TOTAL PRICE: %s
                </body>
                """, sa.getAddress(), sa.getCity(), sa.getCountry(), sa.getPostalCode(), sa.getPhoneNumber(),
                order.getPaymentMethod(),generateProductTable(orderProducts), order.getTotalPrice());
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom("kontakt@mowebcreations.pl");
        mimeMessage.setSubject("Order nr: " + order.getId() + " summary.");
        mimeMessage.setContent(content, "text/html");
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(getLoggedUser().getEmail()));
        Transport.send(mimeMessage);
    }

    private String generateProductTable(List<OrderProduct> productList) {
        StringBuilder tableRows = new StringBuilder();
        for (OrderProduct orderProduct : productList) {
            BigDecimal price = orderProduct.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(orderProduct.getQuantity()));
            tableRows.append("<tr>")
                    .append("<td>").append(orderProduct.getProduct().getName()).append("</td>")
                    .append("<td>").append(orderProduct.getQuantity()).append("</td>")
                    .append("<td>").append(price).append("</td>")
                    .append("</tr>");
        }
        return tableRows.toString();
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

