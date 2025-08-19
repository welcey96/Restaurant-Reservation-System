package nbcc.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String subject, String text, String from, String to) {
        var message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(from);
        message.setTo(to);

        mailSender.send(message);
    }
}
