
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
import com.fmtech.fmcommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Injection de dépendances
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ==========================================
    // GET : récupérer tous les produits
    // ==========================================


    // Méthode with pagination support, returning ProductResponse DTOs ans search functionality:

    @GetMapping
    public Page<ProductResponse> searchProducts(

        @RequestParam(required = false) String name,

        @RequestParam(required = false) BigDecimal minPrice,

        @RequestParam(required = false) BigDecimal maxPrice,

        @RequestParam(required = false) Boolean active,

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

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // ==========================================
    // POST : créer un nouveau produit
    // ==========================================

   @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {

        return productService.createProduct(request);
    }

    // ==========================================
    // PUT : modifier un produit existant
    // ==========================================

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        return productService.updateProduct(id, request);
    }

    // ==========================================
    // DELETE : supprimer un produit
    // ==========================================

    // Cette méthode ne nécessite pas de DTO pour le moment.
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
