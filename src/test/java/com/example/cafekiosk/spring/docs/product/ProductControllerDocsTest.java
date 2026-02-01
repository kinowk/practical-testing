package com.example.cafekiosk.spring.docs.product;

import com.example.cafekiosk.spring.api.controller.product.ProductController;
import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.ProductService;
import com.example.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerDocsTest extends RestDocsSupport {

    private final ProductService productService = mock(ProductService.class);

    private static final String END_POINT_CREATE_PRODUCT = "/api/v1/products/new";

    @Override
    protected Object initController() {
        return new ProductController(productService);
    }

    @DisplayName("신규 상품을 등록하는 API")
    @Test
    void createProduct() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                HANDMADE,
                SELLING,
                "아메리카노",
                2000
        );

        given(productService.createProduct(any(ProductCreateServiceRequest.class)))
                .willReturn(new ProductResponse(
                        1L,
                        "001",
                        HANDMADE,
                        SELLING,
                        "아메리카노",
                        2000
                ));

        mockMvc.perform(
                        post(END_POINT_CREATE_PRODUCT)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("상품 타입"),
                                fieldWithPath("sellingStatus").type(JsonFieldType.STRING).description("상품 판매 상태"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("data.productNumber").type(JsonFieldType.STRING).description("상품 번호"),
                                fieldWithPath("data.type").type(JsonFieldType.STRING).description("상품 타입"),
                                fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING).description("상품 판매 상태"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품 가격")
                        )
                ));
    }

}
