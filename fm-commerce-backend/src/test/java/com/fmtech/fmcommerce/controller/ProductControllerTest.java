
package com.fmtech.fmcommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fmtech.fmcommerce.dto.ProductRequest;
import com.fmtech.fmcommerce.dto.ProductResponse;
import com.fmtech.fmcommerce.exception.ProductNotFoundException;
import com.fmtech.fmcommerce.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturnAllProductsSuccessfully() throws Exception {

        // Arrange
        ProductResponse product = new ProductResponse(
            1L,
            "Laptop Dell",
            "Dell business laptop",
            new BigDecimal("899.99"),
            25,
            true
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(productService.searchProducts(
            isNull(String.class),
            isNull(BigDecimal.class),
            isNull(BigDecimal.class),
            isNull(Boolean.class),
            any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(product), pageable, 1));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Laptop Dell"))
            .andExpect(jsonPath("$.content[0].price").value(899.99))
            .andExpect(jsonPath("$.content[0].stockQuantity").value(25))
            .andExpect(jsonPath("$.content[0].active").value(true))
            .andExpect(jsonPath("$.totalElements").value(1));

        // Verify
        verify(productService).searchProducts(
            isNull(String.class),
            isNull(BigDecimal.class),
            isNull(BigDecimal.class),
            isNull(Boolean.class),
            eq(pageable)
        );
    }

    @Test
void shouldSearchProductsWithFiltersSuccessfully() throws Exception {

    // Arrange
    ProductResponse product = new ProductResponse(
        1L,
        "Laptop Dell",
        "Dell business laptop",
        new BigDecimal("899.99"),
        25,
        true
    );

    Pageable pageable = PageRequest.of(0, 10);

    when(productService.searchProducts(
        eq("Laptop"),
        eq(new BigDecimal("500")),
        eq(new BigDecimal("1500")),
        eq(true),
        any(Pageable.class)
    )).thenReturn(new PageImpl<>(List.of(product), pageable, 1));

    // Act & Assert
    mockMvc.perform(get("/api/products")
            .param("name", "Laptop")
            .param("minPrice", "500")
            .param("maxPrice", "1500")
            .param("active", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].name").value("Laptop Dell"))
        .andExpect(jsonPath("$.totalElements").value(1));

    // Verify
    verify(productService).searchProducts(
        eq("Laptop"),
        eq(new BigDecimal("500")),
        eq(new BigDecimal("1500")),
        eq(true),
        eq(pageable)
    );
    }

    @Test
    void shouldReturnProductByIdSuccessfully() throws Exception {

        // Arrange
        ProductResponse product = new ProductResponse(
            1L,
            "Laptop Dell",
            "Dell business laptop",
            new BigDecimal("899.99"),
            25,
            true
        );

        when(productService.getProductById(1L))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Laptop Dell"))
            .andExpect(jsonPath("$.description").value("Dell business laptop"))
            .andExpect(jsonPath("$.price").value(899.99))
            .andExpect(jsonPath("$.stockQuantity").value(25))
            .andExpect(jsonPath("$.active").value(true));

        // Verify
        verify(productService).getProductById(1L);
    }

    @Test
void shouldReturn404WhenProductDoesNotExist() throws Exception {

    // Arrange
    when(productService.getProductById(999L))
            .thenThrow(new ProductNotFoundException(999L));

    // Act & Assert
    mockMvc.perform(get("/api/products/{id}", 999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message")
            .value("Product not found with id: 999"));

    // Verify
    verify(productService).getProductById(999L);
    }

    @Test
void shouldCreateProductSuccessfully() throws Exception {

    // Arrange
    ProductResponse response = new ProductResponse(
        10L,
        "MacBook Pro",
        "Apple laptop",
        new BigDecimal("1999.99"),
        10,
        true
    );

    when(productService.createProduct(any(ProductRequest.class)))
            .thenReturn(response);

    String requestJson = """
        {
            "name": "MacBook Pro",
            "description": "Apple laptop",
            "price": 1999.99,
            "stockQuantity": 10,
            "active": true
        }
        """;

    // Act & Assert
    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.name").value("MacBook Pro"))
        .andExpect(jsonPath("$.price").value(1999.99))
        .andExpect(jsonPath("$.stockQuantity").value(10))
        .andExpect(jsonPath("$.active").value(true));

    // Verify
    verify(productService).createProduct(any(ProductRequest.class));
    }

    @Test
void shouldReturn400WhenProductDataIsInvalid() throws Exception {

    // Arrange
    String requestJson = """
        {
            "name": "",
            "description": "Invalid product",
            "price": -10,
            "stockQuantity": -5,
            "active": true
        }
        """;

    // Act & Assert
    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").exists());

    // Verify
    verify(productService, never())
            .createProduct(any(ProductRequest.class));
    }

    @Test
void shouldUpdateProductSuccessfully() throws Exception {

    // Arrange
    ProductResponse response = new ProductResponse(
        1L,
        "Laptop Dell Pro",
        "Updated description",
        new BigDecimal("1299.99"),
        15,
        true
    );

    when(productService.updateProduct(
        eq(1L),
        any(ProductRequest.class)
    )).thenReturn(response);

    String requestJson = """
        {
            "name": "Laptop Dell Pro",
            "description": "Updated description",
            "price": 1299.99,
            "stockQuantity": 15,
            "active": true
        }
        """;

    // Act & Assert
    mockMvc.perform(put("/api/products/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Laptop Dell Pro"))
        .andExpect(jsonPath("$.description").value("Updated description"))
        .andExpect(jsonPath("$.price").value(1299.99))
        .andExpect(jsonPath("$.stockQuantity").value(15))
        .andExpect(jsonPath("$.active").value(true));

    // Verify
    verify(productService).updateProduct(
        eq(1L),
        any(ProductRequest.class)
    );
    }

    @Test
void shouldReturn404WhenUpdatingNonExistingProduct() throws Exception {

    // Arrange
    when(productService.updateProduct(
        eq(999L),
        any(ProductRequest.class)
    )).thenThrow(new ProductNotFoundException(999L));

    String requestJson = """
        {
            "name": "Laptop Dell Pro",
            "description": "Updated description",
            "price": 1299.99,
            "stockQuantity": 15,
            "active": true
        }
        """;

    // Act & Assert
    mockMvc.perform(put("/api/products/{id}", 999L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"));

    // Verify
    verify(productService).updateProduct(
        eq(999L),
        any(ProductRequest.class)
    );
    }


    @Test
void shouldReturn400WhenUpdatingProductWithInvalidData() throws Exception {

    // Arrange
    String requestJson = """
        {
            "name": "",
            "description": "Invalid product",
            "price": -100,
            "stockQuantity": -5,
            "active": true
        }
        """;

    // Act & Assert
    mockMvc.perform(put("/api/products/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"));

    // Verify
    verify(productService, never()).updateProduct(
        eq(1L),
        any(ProductRequest.class)
    );
    }

    @Test
void shouldDeleteProductSuccessfully() throws Exception {

    // Act & Assert
    mockMvc.perform(delete("/api/products/{id}", 1L))
        .andExpect(status().isOk());

    // Verify
    verify(productService).deleteProduct(1L);
    }

    @Test
void shouldReturn404WhenDeletingNonExistingProduct() throws Exception {

    // Arrange
    doThrow(new ProductNotFoundException(999L))
        .when(productService).deleteProduct(999L);

    // Act & Assert
    mockMvc.perform(delete("/api/products/{id}", 999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"));

    // Verify
    verify(productService).deleteProduct(999L);
    }
}

