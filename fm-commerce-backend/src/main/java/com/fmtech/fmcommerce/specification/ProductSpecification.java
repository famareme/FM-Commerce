
package com.fmtech.fmcommerce.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fmtech.fmcommerce.entity.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {

    public static Specification<Product> hasFilters(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                    )
                );
            }

            if (minPrice != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        minPrice
                    )
                );
            }

            if (maxPrice != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        maxPrice
                    )
                );
            }

            if (active != null) {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("active"),
                        active
                    )
                );
            }

            return criteriaBuilder.and(
                predicates.toArray(new Predicate[0])
            );
        };
    }
}
