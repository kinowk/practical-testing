package com.example.cafekiosk.spring.api.service.mail;

import com.example.cafekiosk.spring.client.MailSendClient;
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendEmail(String fromEmail, String email, String subject, String content) {
        boolean result = mailSendClient.sendEmail(fromEmail, email, subject, content);

        if (result) {
            mailSendHistoryRepository.save(
                    MailSendHistory.builder()
                            .fromEmail(fromEmail)
                            .toEmail(email)
                            .subject(subject)
                            .content(content)
                            .build()
            );
        }

        return result;
    }
}
