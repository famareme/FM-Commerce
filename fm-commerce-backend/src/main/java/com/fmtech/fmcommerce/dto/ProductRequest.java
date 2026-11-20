package com.fmtech.fmcommerce.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Request object used to create or update a product")
public class ProductRequest {

    @Schema(
        description = "Product name. Cannot be blank and must not exceed 150 characters.",
        example = "Laptop Dell",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;

    @Schema(
        description = "Product description. Maximum 2000 characters.",
        example = "Dell business laptop with 16 GB RAM."
    )
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Schema(
        description = "Product price. Must be greater than zero.",
        example = "899.99",
        minimum = "0.01",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    private BigDecimal price;

    @Schema(
        description = "Available stock quantity. Must be zero or greater.",
        example = "25",
        minimum = "0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Schema(
        description = "Indicates whether the product is active.",
        example = "true",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Product active status is required")
    private Boolean active;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}