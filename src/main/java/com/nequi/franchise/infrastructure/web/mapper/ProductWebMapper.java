package com.nequi.franchise.infrastructure.web.mapper;

import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.infrastructure.web.dto.ProductDto;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Product y ProductDto
 */
@Component
public class ProductWebMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }

    public Product toDomain(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getStock()
        );
    }
}
