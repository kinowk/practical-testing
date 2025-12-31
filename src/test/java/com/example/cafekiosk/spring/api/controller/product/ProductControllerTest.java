package com.example.cafekiosk.spring.api.controller.product;

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.ProductService;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private static final String END_POINT_CREATE_PRODUCT = "/api/v1/products/new";
    private static final String END_POINT_SELLING_PRODUCTS = "/api/v1/products/selling";

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                HANDMADE,
                SELLING,
                "아메리카노",
                2000
        );

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_PRODUCT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("상품타입 없이, 상품을 등록한다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                null,
                SELLING,
                "아메리카노",
                2000
        );

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_PRODUCT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("상품 판매상태 없이, 상품을 등록한다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                HANDMADE,
                null,
                "아메리카노",
                2000
        );

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_PRODUCT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("상품명 없이, 상품을 등록한다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                HANDMADE,
                SELLING,
                null,
                2000
        );

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_PRODUCT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품명은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("상품가격을 0보다 작게, 상품을 등록한다.")
    @Test
    void createProductWithLessThanOrEqualToZeroPrice() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                HANDMADE,
                SELLING,
                "아메리카노",
                -1
        );

        // when & then
        mockMvc.perform(
                        post(END_POINT_CREATE_PRODUCT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("가격은 0원 이상이어야 됩니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("키오스크에서 조회되는 상품 목록을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> data = List.of();
        when(productService.getSellingProducts()).thenReturn(data);

        // when & then
        mockMvc.perform(
                        get(END_POINT_SELLING_PRODUCTS)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

}