
package com.fmtech.fmcommerce.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Product information returned by the FM-Commerce API")
public class ProductResponse {

    @Schema(
        description = "Unique product identifier",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Product name",
        example = "Laptop Dell"
    )
    private String name;

    @Schema(
        description = "Product description",
        example = "Dell business laptop with 16 GB RAM."
    )
    private String description;

    @Schema(
        description = "Product price",
        example = "899.99"
    )
    private BigDecimal price;

    @Schema(
        description = "Available stock quantity",
        example = "25"
    )
    private Integer stockQuantity;

    @Schema(
        description = "Indicates whether the product is active",
        example = "true"
    )
    private boolean active;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stockQuantity,
            boolean active) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public boolean isActive() {
        return active;
    }
}
