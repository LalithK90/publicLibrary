package lk.wasity_institute.util.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(String receiverEmail, String subject, String message) throws
            MailException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();


        try {
            mailMessage.setTo(receiverEmail);
            mailMessage.setFrom("-(Success Institute - (not reply))");
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);
        } catch ( Exception e ) {
            System.out.println("Email Exception " + e.getCause().getCause().getMessage());
        }
    }


}
