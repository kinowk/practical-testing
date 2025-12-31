package com.example.cafekiosk.spring.api.controller.order;

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.example.cafekiosk.spring.api.service.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private static final String END_POINT_CREATE_ORDER = "/api/v1/orders/new";

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() throws Exception {
        // given
        List<String> productNumbers = List.of("001");
        OrderCreateRequest request = new OrderCreateRequest(productNumbers);

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_ORDER)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("상품번호 없이, 주문을 생성한다.")
    @Test
    void createOrderWithoutProductNumbers() throws Exception {
        // given
        List<String> productNumbers = List.of();
        OrderCreateRequest request = new OrderCreateRequest(productNumbers);

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_ORDER)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품번호 목록은 필수입니다."));
    }
}