package com.fmtech.fmcommerce.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.fmtech.fmcommerce.dto.ProductResponse;

public class ProductResponseTest {
    @Test
void shouldCreateEmptyProductResponse() {

    // Act
    ProductResponse response = new ProductResponse();

    // Assert
    assertNotNull(response);

    assertNull(response.getId());
    assertNull(response.getName());
    assertNull(response.getDescription());
    assertNull(response.getPrice());
    assertNull(response.getStockQuantity());
    assertFalse(response.isActive());
}

}
