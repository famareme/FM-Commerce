
package com.fmtech.fmcommerce.specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.fmtech.fmcommerce.entity.Product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Expression;


import jakarta.persistence.criteria.Path;

class ProductSpecificationTest {

    @SuppressWarnings("unchecked")
    Expression<String> nameExpression = mock(Expression.class);
    private Root<Product> root = mock(Root.class);

    private CriteriaQuery<?> query = mock(CriteriaQuery.class);

    private CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);


@Test
void shouldFilterProductsByName() {

    // Arrange
    Predicate namePredicate = mock(Predicate.class);

    when(criteriaBuilder.lower(root.get("name")))
    .thenReturn(nameExpression);

when(criteriaBuilder.like(
    eq(nameExpression),
    eq("%laptop%")
)).thenReturn(namePredicate);

    when(criteriaBuilder.and(any(Predicate[].class)))
        .thenReturn(mock(Predicate.class));

    // Act
    ProductSpecification.hasFilters(
        "Laptop",
        null,
        null,
        null
    ).toPredicate(root, query, criteriaBuilder);

    // Assert
    verify(criteriaBuilder).like(
        eq(nameExpression),
        eq("%laptop%")
    );

    verify(criteriaBuilder).and(any(Predicate[].class));
    }

    @Test
void shouldFilterProductsByMinimumPrice() {

    // Arrange
    BigDecimal minPrice = new BigDecimal("500");

    @SuppressWarnings("unchecked")
    jakarta.persistence.criteria.Path<BigDecimal> pricePath =
        mock(jakarta.persistence.criteria.Path.class);

    Predicate pricePredicate = mock(Predicate.class);

    when(root.<BigDecimal>get("price")).thenReturn(pricePath);

    when(criteriaBuilder.greaterThanOrEqualTo(
        pricePath,
        minPrice
    )).thenReturn(pricePredicate);

    when(criteriaBuilder.and(any(Predicate[].class)))
        .thenReturn(mock(Predicate.class));

    // Act
    ProductSpecification.hasFilters(
        null,
        minPrice,
        null,
        null
    ).toPredicate(root, query, criteriaBuilder);

    // Assert
    verify(criteriaBuilder).greaterThanOrEqualTo(
        pricePath,
        minPrice
    );

    verify(criteriaBuilder).and(any(Predicate[].class));
    }

    @Test
void shouldFilterProductsByMaximumPrice() {

    // Arrange
    BigDecimal maxPrice = new BigDecimal("1500");

    @SuppressWarnings("unchecked")
    jakarta.persistence.criteria.Path<BigDecimal> pricePath =
        mock(jakarta.persistence.criteria.Path.class);

    Predicate pricePredicate = mock(Predicate.class);

    when(root.<BigDecimal>get("price")).thenReturn(pricePath);

    when(criteriaBuilder.lessThanOrEqualTo(
        pricePath,
        maxPrice
    )).thenReturn(pricePredicate);

    when(criteriaBuilder.and(any(Predicate[].class)))
        .thenReturn(mock(Predicate.class));

    // Act
    ProductSpecification.hasFilters(
        null,
        null,
        maxPrice,
        null
    ).toPredicate(root, query, criteriaBuilder);

    // Assert
    verify(criteriaBuilder).lessThanOrEqualTo(
        pricePath,
        maxPrice
    );

    verify(criteriaBuilder).and(any(Predicate[].class));
    }

    @Test
void shouldFilterProductsByActiveStatus() {

    // Arrange
    Predicate activePredicate = mock(Predicate.class);

    when(criteriaBuilder.equal(
        root.get("active"),
        true
    )).thenReturn(activePredicate);

    when(criteriaBuilder.and(any(Predicate[].class)))
        .thenReturn(mock(Predicate.class));

    // Act
    ProductSpecification.hasFilters(
        null,
        null,
        null,
        true
    ).toPredicate(root, query, criteriaBuilder);

    // Assert
    verify(criteriaBuilder).equal(
        root.get("active"),
        true
    );

    verify(criteriaBuilder).and(any(Predicate[].class));
    }


    @Test
    void shouldCombineAllFilters() {

        // Arrange
        BigDecimal minPrice = new BigDecimal("500");
        BigDecimal maxPrice = new BigDecimal("1500");

        Predicate namePredicate = mock(Predicate.class);
        Predicate minPricePredicate = mock(Predicate.class);
        Predicate maxPricePredicate = mock(Predicate.class);
        Predicate activePredicate = mock(Predicate.class);

        @SuppressWarnings("unchecked")
        Expression<String> nameExpression = mock(Expression.class);

        @SuppressWarnings("unchecked")
        Path<BigDecimal> pricePath = mock(Path.class);

        // Name
        when(criteriaBuilder.lower(root.<String>get("name")))
            .thenReturn(nameExpression);

        when(criteriaBuilder.like(
            nameExpression,
            "%laptop%"
        )).thenReturn(namePredicate);

        // Minimum price
        when(root.<BigDecimal>get("price"))
            .thenReturn(pricePath);

        when(criteriaBuilder.greaterThanOrEqualTo(
            pricePath,
            minPrice
        )).thenReturn(minPricePredicate);

        // Maximum price
        when(criteriaBuilder.lessThanOrEqualTo(
            pricePath,
            maxPrice
        )).thenReturn(maxPricePredicate);

        // Active status
        when(criteriaBuilder.equal(root.get("active"), true))
            .thenReturn(activePredicate);

        // Combine predicates
        when(criteriaBuilder.and(any(Predicate[].class)))
            .thenReturn(mock(Predicate.class));

        // Act
        ProductSpecification.hasFilters(
            "Laptop",
            minPrice,
            maxPrice,
            true
        ).toPredicate(root, query, criteriaBuilder);

        // Assert
        verify(criteriaBuilder).like(
            nameExpression,
            "%laptop%"
        );

        verify(criteriaBuilder).greaterThanOrEqualTo(
            pricePath,
            minPrice
        );

        verify(criteriaBuilder).lessThanOrEqualTo(
            pricePath,
            maxPrice
        );

        verify(criteriaBuilder).equal(root.get("active"), true);

        verify(criteriaBuilder).and(any(Predicate[].class));
        }

        @Test
        void shouldReturnTruePredicateWhenNoFiltersAreProvided() {

            // Arrange
            Predicate truePredicate = mock(Predicate.class);

            when(criteriaBuilder.and(any(Predicate[].class)))
                .thenReturn(truePredicate);

            // Act
            Predicate result = ProductSpecification.hasFilters(
                null,
                null,
                null,
                null
            ).toPredicate(root, query, criteriaBuilder);

            // Assert
            assertEquals(truePredicate, result);

            verify(criteriaBuilder).and(any(Predicate[].class));
        }


        @Test
        void shouldIgnoreBlankProductName() {

            // Arrange
            when(criteriaBuilder.and(any(Predicate[].class)))
                .thenReturn(mock(Predicate.class));

            // Act
            ProductSpecification.hasFilters(
                "   ",
                null,
                null,
                null
            ).toPredicate(root, query, criteriaBuilder);

            // Assert
            verify(criteriaBuilder, never()).lower(any());

            verify(criteriaBuilder, never()).like(
            ArgumentMatchers.<Expression<String>>any(),
            ArgumentMatchers.<String>any()
        );

            verify(criteriaBuilder).and(any(Predicate[].class));
        }

        @Test
        void shouldFilterInactiveProducts() {

            // Arrange
            Predicate inactivePredicate = mock(Predicate.class);

            when(criteriaBuilder.equal(root.get("active"), false))
                .thenReturn(inactivePredicate);

            when(criteriaBuilder.and(any(Predicate[].class)))
                .thenReturn(mock(Predicate.class));

            // Act
            ProductSpecification.hasFilters(
                null,
                null,
                null,
                false
            ).toPredicate(root, query, criteriaBuilder);

            // Assert
            verify(criteriaBuilder).equal(root.get("active"), false);

            verify(criteriaBuilder).and(any(Predicate[].class));
        }

        @Test
        void shouldInstantiateProductSpecification() {

            // Act
            ProductSpecification specification = new ProductSpecification();

            // Assert
            assertNotNull(specification);
        }
    }

