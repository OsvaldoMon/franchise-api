package com.nequi.franchise.infrastructure.web.mapper;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.model.Franchise.ProductWithBranch;
import com.nequi.franchise.infrastructure.web.dto.BranchDto;
import com.nequi.franchise.infrastructure.web.dto.FranchiseDto;
import com.nequi.franchise.infrastructure.web.dto.ProductWithBranchDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Franchise y FranchiseDto
 */
@Component
public class FranchiseWebMapper {

    private final BranchWebMapper branchWebMapper;
    private final ProductWebMapper productWebMapper;

    public FranchiseWebMapper(BranchWebMapper branchWebMapper, ProductWebMapper productWebMapper) {
        this.branchWebMapper = branchWebMapper;
        this.productWebMapper = productWebMapper;
    }

    public FranchiseDto toDto(Franchise franchise) {
        if (franchise == null) {
            return null;
        }
        List<BranchDto> branchDtos = franchise.getBranches().stream()
                .map(branchWebMapper::toDto)
                .collect(Collectors.toList());
        
        return new FranchiseDto(
                franchise.getId(),
                franchise.getName(),
                branchDtos
        );
    }

    public Franchise toDomain(FranchiseDto dto) {
        if (dto == null) {
            return null;
        }
        List<Branch> branches = dto.getBranches().stream()
                .map(branchWebMapper::toDomain)
                .collect(Collectors.toList());
        
        return new Franchise(
                dto.getId(),
                dto.getName(),
                branches
        );
    }

    public ProductWithBranchDto toDto(ProductWithBranch productWithBranch) {
        if (productWithBranch == null) {
            return null;
        }
        return new ProductWithBranchDto(
                productWebMapper.toDto(productWithBranch.getProduct()),
                productWithBranch.getBranchName()
        );
    }
}
