package com.nequi.franchise.infrastructure.persistence.mapper;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.infrastructure.persistence.document.BranchDocument;
import com.nequi.franchise.infrastructure.persistence.document.ProductDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Branch y BranchDocument
 */
@Component
public class BranchMapper {

    private final ProductMapper productMapper;

    public BranchMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public BranchDocument toDocument(Branch branch, String franchiseId) {
        if (branch == null) {
            return null;
        }
        List<ProductDocument> productDocuments = branch.getProducts().stream()
                .map(productMapper::toDocument)
                .collect(Collectors.toList());
        
        return new BranchDocument(
                branch.getId(),
                branch.getName(),
                franchiseId,
                productDocuments
        );
    }

    public Branch toDomain(BranchDocument document) {
        if (document == null) {
            return null;
        }
        List<Product> products = document.getProducts().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
        
        return new Branch(
                document.getId(),
                document.getName(),
                products
        );
    }
}
