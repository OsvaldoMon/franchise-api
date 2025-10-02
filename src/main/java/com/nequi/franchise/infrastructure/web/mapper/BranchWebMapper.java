package com.nequi.franchise.infrastructure.web.mapper;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.infrastructure.web.dto.BranchDto;
import com.nequi.franchise.infrastructure.web.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Branch y BranchDto
 */
@Component
public class BranchWebMapper {

    private final ProductWebMapper productWebMapper;

    public BranchWebMapper(ProductWebMapper productWebMapper) {
        this.productWebMapper = productWebMapper;
    }

    public BranchDto toDto(Branch branch) {
        if (branch == null) {
            return null;
        }
        List<ProductDto> productDtos = branch.getProducts().stream()
                .map(productWebMapper::toDto)
                .collect(Collectors.toList());
        
        return new BranchDto(
                branch.getId(),
                branch.getName(),
                productDtos
        );
    }

    public Branch toDomain(BranchDto dto) {
        if (dto == null) {
            return null;
        }
        List<Product> products = dto.getProducts().stream()
                .map(productWebMapper::toDomain)
                .collect(Collectors.toList());
        
        return new Branch(
                dto.getId(),
                dto.getName(),
                products
        );
    }
}
