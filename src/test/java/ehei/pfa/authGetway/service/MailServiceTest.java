package ehei.pfa.authGetway.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Test
    void sendHtmlRealEmail() throws Exception {

        String html = """
            <div style="font-family:Arial;padding:20px">
                <h1>Hello Simo 👋</h1>
                <p>Brevo SMTP is WORKING 🔥</p>
            </div>
        """;

        mailService.sendHtml(
                "simofff0@gmail.com",
                "Brevo SMTP Test 🚀",
                html
        );
    }
}