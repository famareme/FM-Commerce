
package com.fmtech.fmcommerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// import org.springframework.mcock.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fmtech.fmcommerce.controller.ProductController;
import com.fmtech.fmcommerce.exception.GlobalExceptionHandler;
import com.fmtech.fmcommerce.service.ProductService;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturn500WhenUnexpectedExceptionOccurs() throws Exception {

        when(productService.getProductById(999L))
            .thenThrow(new RuntimeException("Unexpected database error"));

        mockMvc.perform(
                get("/api/products/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.message")
                .value("An unexpected error occurred. Please try again later."))
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
