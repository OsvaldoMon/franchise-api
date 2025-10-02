package com.nequi.franchise.infrastructure.persistence.mapper;

import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.infrastructure.persistence.document.ProductDocument;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Product y ProductDocument
 */
@Component
public class ProductMapper {

    public ProductDocument toDocument(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDocument(
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }

    public Product toDomain(ProductDocument document) {
        if (document == null) {
            return null;
        }
        return new Product(
                document.getId(),
                document.getName(),
                document.getStock()
        );
    }
}
