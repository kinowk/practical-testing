package com.example.cafekiosk.spring.api.service.order;

import com.example.cafekiosk.spring.api.service.mail.MailService;
import com.example.cafekiosk.spring.domain.order.Order;
import com.example.cafekiosk.spring.domain.order.OrderRepository;
import com.example.cafekiosk.spring.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate localDate, String email) {
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.plusDays(1).atStartOfDay();

        List<Order> orders = orderRepository.findOrdersBy(
                startDateTime,
                endDateTime,
                OrderStatus.PAYMENT_COMPLETED
        );

        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        String subject = String.format("[매출 통계] %s", localDate);
        String content = String.format("매출 총액: %s", totalAmount);

        boolean result = mailService.sendEmail(
                "fromEmail",
                email,
                subject,
                content
        );

        if (!result) {
            throw new IllegalStateException("일일 매출 통계 메일 전송에 실패했습니다.");
        }

        return true;
    }

}
