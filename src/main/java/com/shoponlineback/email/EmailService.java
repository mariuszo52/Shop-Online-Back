package com.shoponlineback.email;

import com.shoponlineback.exceptions.order.OrderNotFoundException;
import com.shoponlineback.order.Order;
import com.shoponlineback.order.OrderRepository;
import com.shoponlineback.orderProduct.OrderProduct;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.orderProduct.activationCode.ActivationCode;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeRepository;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeUpdateDto;
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
    private final OrderRepository orderRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private Session session;
    @Value("${EMAIL_SERVER}")
    private String mailServer;
    @Value("${EMAIL_USERNAME}")
    private String username;
    @Value("${EMAIL_PASSWORD}")
    private String password;

    public EmailService(OrderProductRepository orderProductRepository, OrderRepository orderRepository, ActivationCodeRepository activationCodeRepository) {
        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;
        this.activationCodeRepository = activationCodeRepository;
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
    public void sendActivationCodeEmail(ActivationCodeUpdateDto codeUpdateDto) throws MessagingException {
        OrderProduct orderProduct = orderProductRepository.findById(codeUpdateDto.getOrderProductId())
                .orElseThrow(OrderNotFoundException::new);
        List<OrderProduct> orderProducts = orderProductRepository.
                findOrderProductsByOrderId(orderProduct.getOrder().getId());
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("kontakt@mowebcreations.pl"));
        mimeMessage.setSubject("ACTIVATION CODES TO ORDER NR: " + orderProduct.getOrder().getId());
        mimeMessage.setContent(createActivationCodeEmailText(orderProducts), "text/html");
        InternetAddress email = new InternetAddress(orderProduct.getOrder().getUser().getEmail());
        mimeMessage.setRecipient(Message.RecipientType.TO, email);
        Transport.send(mimeMessage);

    }
    private String createActivationCodeEmailText(List<OrderProduct> orderProducts){
        StringBuilder text = new StringBuilder();
        for (OrderProduct orderProduct : orderProducts) {
            List<String> codes = activationCodeRepository.findAllByOrderProductId(orderProduct.getId()).stream()
                    .map(ActivationCode::getCode).toList();
            text.append("<p>PRODUCT NAME: ").append(orderProduct.getProduct().getName())
                    .append(" ACTIVATION CODES: ").append(codes).append("</p>");
        }
        return text.toString();
    }
    public void sendOrderConfirmationEmail(Long orderId, String subject) throws MessagingException {
        List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
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
                            <p style='font-weight: bold;'>ORDER STATUS: %s</p>
                            <p>PRODUCTS:</p>
                            <table>
                                <tr>
                                    <th>Product Name</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Activation Code</th>
                                </tr>
                                %s
                            </table>
                            <h2>TOTAL PRICE: %s
                        </body>
                        """, sa.getAddress(), sa.getCity(), sa.getCountry(), sa.getPostalCode(), sa.getPhoneNumber(),
                order.getPaymentMethod(), order.getOrderStatus(), generateProductTable(orderProducts), order.getTotalPrice());
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom("kontakt@mowebcreations.pl");
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(content, "text/html");
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(getLoggedUser().getEmail()));
        Transport.send(mimeMessage);
    }

    private String generateProductTable(List<OrderProduct> productList) {
        StringBuilder tableRows = new StringBuilder();
        for (OrderProduct orderProduct : productList) {
            BigDecimal price = orderProduct.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(orderProduct.getQuantity()));
            String codesText;
            List<String> codes = activationCodeRepository.findAllByOrderProductId(orderProduct.getId()).stream()
                    .map(ActivationCode::getCode).toList();
            if(codes.isEmpty()){
                codesText = "CODE IN REALIZATION";
            }else {
                codesText = codes.toString();
            }
            tableRows.append("<tr>")
                    .append("<td>").append(orderProduct.getProduct().getName()).append("</td>")
                    .append("<td>").append(orderProduct.getQuantity()).append("</td>")
                    .append("<td>").append(price).append("</td>")
                    .append("<td>").append(codesText).append("</td>")
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
        properties.put("mail.mime.charset", "UTF-8");
        return Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );
    }

}

