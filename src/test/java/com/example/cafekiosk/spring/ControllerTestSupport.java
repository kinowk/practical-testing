package com.example.cafekiosk.spring;

import com.example.cafekiosk.spring.api.controller.order.OrderController;
import com.example.cafekiosk.spring.api.controller.product.ProductController;
import com.example.cafekiosk.spring.api.service.order.OrderService;
import com.example.cafekiosk.spring.api.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {
        OrderController.class,
        ProductController.class
})
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected OrderService orderService;

    @MockitoBean
    protected ProductService productService;
}
