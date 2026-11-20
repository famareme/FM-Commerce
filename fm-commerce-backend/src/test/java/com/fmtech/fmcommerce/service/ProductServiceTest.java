
package com.fmtech.fmcommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.fmtech.fmcommerce.dto.ProductRequest;
import com.fmtech.fmcommerce.dto.ProductResponse;
import com.fmtech.fmcommerce.entity.Product;
import com.fmtech.fmcommerce.exception.ProductNotFoundException;
import com.fmtech.fmcommerce.repository.ProductRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnProductWhenProductExists() {

        // Arrange : préparer le produit
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop Dell");
        product.setDescription("Dell business laptop");
        product.setPrice(new BigDecimal("899.99"));
        product.setStockQuantity(25);
        product.setActive(true);

        when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));

        // Act : appeler le service
        ProductResponse response = productService.getProductById(1L);

        // Assert : vérifier le résultat
        assertEquals(1L, response.getId());
        assertEquals("Laptop Dell", response.getName());
        assertEquals(new BigDecimal("899.99"), response.getPrice());
        assertEquals(25, response.getStockQuantity());
        assertEquals(true, response.isActive());

        verify(productRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        // Arrange
        when(productRepository.findById(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            ProductNotFoundException.class,
            () -> productService.getProductById(999L)
        );

        verify(productRepository).findById(999L);
    }

    @Test
    void shouldCreateProductSuccessfully() {

        // Arrange : préparer les données
        ProductRequest request = new ProductRequest();
        request.setName("MacBook Pro");
        request.setDescription("Apple laptop");
        request.setPrice(new BigDecimal("1999.99"));
        request.setStockQuantity(10);
        request.setActive(true);

        Product savedProduct = new Product();
        savedProduct.setId(10L);
        savedProduct.setName("MacBook Pro");
        savedProduct.setDescription("Apple laptop");
        savedProduct.setPrice(new BigDecimal("1999.99"));
        savedProduct.setStockQuantity(10);
        savedProduct.setActive(true);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // Act : exécuter la méthode
        ProductResponse response = productService.createProduct(request);

        // Assert : vérifier le résultat
        assertEquals(10L, response.getId());
        assertEquals("MacBook Pro", response.getName());
        assertEquals(new BigDecimal("1999.99"), response.getPrice());
        assertEquals(10, response.getStockQuantity());
        assertEquals(true, response.isActive());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldUpdateProductSuccessfully() {

    // Arrange : produit déjà existant dans la base simulée
    Product existingProduct = new Product();
    existingProduct.setId(1L);
    existingProduct.setName("Laptop Dell");
    existingProduct.setDescription("Old description");
    existingProduct.setPrice(new BigDecimal("899.99"));
    existingProduct.setStockQuantity(5);
    existingProduct.setActive(true);

    // Nouvelles données à appliquer
    ProductRequest request = new ProductRequest();
    request.setName("Laptop Dell Pro");
    request.setDescription("Updated description");
    request.setPrice(new BigDecimal("1299.99"));
    request.setStockQuantity(15);
    request.setActive(true);

    when(productRepository.findById(1L))
            .thenReturn(Optional.of(existingProduct));

    when(productRepository.save(any(Product.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    // Act : modifier le produit
    ProductResponse response = productService.updateProduct(1L, request);

    // Assert : vérifier les nouvelles valeurs
    assertEquals(1L, response.getId());
    assertEquals("Laptop Dell Pro", response.getName());
    assertEquals("Updated description", response.getDescription());
    assertEquals(new BigDecimal("1299.99"), response.getPrice());
    assertEquals(15, response.getStockQuantity());
    assertEquals(true, response.isActive());

    verify(productRepository).findById(1L);
    verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {

        // Arrange
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        ProductRequest request = new ProductRequest();
        request.setName("New Product");
        request.setDescription("Test description");
        request.setPrice(new BigDecimal("100.00"));
        request.setStockQuantity(10);
        request.setActive(true);

        // Act & Assert
        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(999L, request));

        // Verify
        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductSuccessfully() {

    // Arrange
    Product product = new Product();
    product.setId(1L);
    product.setName("Laptop Dell");

    when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));

    // Act
    productService.deleteProduct(1L);

    // Assert
    verify(productRepository).findById(1L);
    verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {

        // Arrange
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(999L));

        // Verify
        verify(productRepository).findById(999L);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldReturnAllProductsWithPagination() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(new BigDecimal("1200.00"));
        product.setStockQuantity(5);
        product.setActive(true);

        Page<Product> productPage = new PageImpl<>(
            List.of(product),
            pageable,
            1
        );

        when(productRepository.findAll(pageable))
            .thenReturn(productPage);

        // Act
        Page<ProductResponse> result =
            productService.getAllProducts(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Laptop", result.getContent().get(0).getName());
        assertEquals(
            new BigDecimal("1200.00"),
            result.getContent().get(0).getPrice()
        );

        verify(productRepository).findAll(pageable);
    }

    @Test
void shouldSearchProductsWithFilters() {

    // Arrange
    Pageable pageable = PageRequest.of(0, 10);

    BigDecimal minPrice = new BigDecimal("500");
    BigDecimal maxPrice = new BigDecimal("1500");

    Product product = new Product();
    product.setId(1L);
    product.setName("Laptop");
    product.setDescription("Gaming laptop");
    product.setPrice(new BigDecimal("1200.00"));
    product.setStockQuantity(5);
    product.setActive(true);

    Page<Product> productPage = new PageImpl<>(
        List.of(product),
        pageable,
        1
    );

    when(productRepository.findAll(
        ArgumentMatchers.<Specification<Product>>any(),
        eq(pageable)
    )).thenReturn(productPage);

    // Act
    Page<ProductResponse> result =
        productService.searchProducts(
            "Laptop",
            minPrice,
            maxPrice,
            true,
            pageable
        );

    // Assert
    assertEquals(1, result.getTotalElements());
    assertEquals("Laptop", result.getContent().get(0).getName());

    verify(productRepository).findAll(
        ArgumentMatchers.<Specification<Product>>any(),
        eq(pageable)
    );
    }

}
