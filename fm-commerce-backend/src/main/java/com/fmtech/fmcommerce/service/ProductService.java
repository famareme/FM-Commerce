
package com.fmtech.fmcommerce.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fmtech.fmcommerce.dto.ProductRequest;
import com.fmtech.fmcommerce.dto.ProductResponse;
import com.fmtech.fmcommerce.entity.Product;
import com.fmtech.fmcommerce.exception.ProductNotFoundException;
import com.fmtech.fmcommerce.repository.ProductRepository;
import com.fmtech.fmcommerce.specification.ProductSpecification;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Injection de dépendances : on conserve ProductRepository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ==========================================
    // GET : récupérer tous les produits
    // ==========================================

    // Nouvelle méthode : retourne une liste de ProductResponse with pagination support
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toProductResponse);
    }

// ==========================================
    // GET : rechercher des produits avec des filtres
    // ==========================================

    public Page<ProductResponse> searchProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            Pageable pageable) {

        return productRepository.findAll(
                ProductSpecification.hasFilters(
                        name,
                        minPrice,
                        maxPrice,
                        active
                ),
                pageable
        ).map(this::toProductResponse);
    }



    // ==========================================
    // GET : récupérer un produit par son ID
    // ==========================================


    // Nouvelle méthode : retourne ProductResponse
    public ProductResponse getProductById(Long id) {
        Product product = findProductOrThrow(id);

        return toProductResponse(product);
    }

    // ==========================================
    // POST : créer un nouveau produit
    // ==========================================

    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();

        copyRequestToProduct(request, product);

        Product savedProduct = productRepository.save(product);

        return toProductResponse(savedProduct);
    }

    // ==========================================
    // PUT : modifier un produit existant
    // ==========================================


    // Nouvelle méthode : reçoit ProductRequest et retourne ProductResponse
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product existingProduct = findProductOrThrow(id);

        copyRequestToProduct(request, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        return toProductResponse(updatedProduct);
    }

    // ==========================================
    // DELETE : supprimer un produit
    // ==========================================


    public void deleteProduct(Long id) {

        Product product = findProductOrThrow(id);

        productRepository.delete(product);
    }

    // ==========================================
    // MÉTHODES PRIVÉES : conversion et recherche
    // ==========================================

    // Recherche un produit ou déclenche une exception
    private Product findProductOrThrow(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                    new ProductNotFoundException(id));
    }

    // Copie les données du DTO vers l'entité Product
    private void copyRequestToProduct(ProductRequest request, Product product) {

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setActive(request.getActive());
    }

    // Convertit une entité Product en ProductResponse
    private ProductResponse toProductResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.isActive()
        );
    }
}
