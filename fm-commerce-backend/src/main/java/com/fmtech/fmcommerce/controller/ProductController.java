package com.fmtech.fmcommerce.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fmtech.fmcommerce.dto.ProductRequest;
import com.fmtech.fmcommerce.dto.ProductResponse;
import com.fmtech.fmcommerce.exception.GlobalExceptionHandler;
import com.fmtech.fmcommerce.service.ProductService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/products")
@Tag(
    name = "Products",
    description = "API for managing products in FM-Commerce"
)
public class ProductController {

    private final ProductService productService;

    // Injection de dépendances
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ==========================================
    // GET : rechercher tous les produits
    // ==========================================

    @Operation(
        summary = "Search products",
        description = "Returns a paginated list of products. "
            + "Optional filters include name, minimum price, "
            + "maximum price and active status."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                schema = @Schema(
                    implementation = GlobalExceptionHandler.ApiError.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(
                    implementation = GlobalExceptionHandler.ApiError.class
                )
            )
        )
    })
    @GetMapping
    public Page<ProductResponse> searchProducts(

        @Parameter(description = "Filter by product name")
        @RequestParam(required = false) String name,

        @Parameter(description = "Minimum product price")
        @RequestParam(required = false) BigDecimal minPrice,


        @Parameter(description = "Maximum product price")
        @RequestParam(required = false) BigDecimal maxPrice,

        @Parameter(description = "Filter by active status")
        @RequestParam(required = false) Boolean active,

        @ParameterObject
        @PageableDefault(page = 0, size = 10) Pageable pageable) {

        return productService.searchProducts(
            name,
            minPrice,
            maxPrice,
            active,
            pageable
        );
    }

    // ==========================================
    // GET : récupérer un produit par son ID
    // ==========================================

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a product using its unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product retrieved successfully",
            content = @Content(
                schema = @Schema(implementation = ProductResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    @GetMapping("/{id}")
    public ProductResponse getProductById(
        @Parameter(description = "Product ID", example = "1")
        @PathVariable Long id) {

        return productService.getProductById(id);
    }

    // ==========================================
    // POST : créer un nouveau produit
    // ==========================================

    @Operation(
        summary = "Create a product",
        description = "Creates a new product in the catalog."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product created successfully",
            content = @Content(
                schema = @Schema(implementation = ProductResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product data"
        )
    })
    @PostMapping
    public ProductResponse createProduct(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product information to create",
            required = true
        )
        @Valid @RequestBody ProductRequest request) {

        return productService.createProduct(request);
    }

    // ==========================================
    // PUT : modifier un produit existant
    // ==========================================

    @Operation(
        summary = "Update a product",
        description = "Updates an existing product using its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(
                schema = @Schema(implementation = ProductResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product data"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
        @Parameter(description = "Product ID", example = "1")
        @PathVariable Long id,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated product information",
            required = true
        )
        @Valid @RequestBody ProductRequest request) {

        return productService.updateProduct(id, request);
    }

    // ==========================================
    // DELETE : supprimer un produit
    // ==========================================

    @Operation(
        summary = "Delete a product",
        description = "Deletes a product using its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(
        @Parameter(description = "Product ID", example = "1")
        @PathVariable Long id) {

        productService.deleteProduct(id);
    }
}