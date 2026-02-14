package ehei.pfa.authGetway.service;

import ehei.pfa.authGetway.DTO.email.RegisterEmailDTO;
import ehei.pfa.authGetway.exception.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    public void sendHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException("Email could not be sent to " + to);
        }
    }

    @Async
    public void sendVerificationEmail(String to, RegisterEmailDTO dto)  {
        Context ctx = new Context();
        ctx.setVariable("actionUrl", dto.getActionUrl());
        ctx.setVariable("name", dto.getName());
        ctx.setVariable("lastName", dto.getLastName());

        String html = templateEngine.process("mail/verifyEmail", ctx);

        sendHtml(to, "Vérifiez votre adresse e-mail", html);
    }
}