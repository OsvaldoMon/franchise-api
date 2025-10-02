package com.nequi.franchise.infrastructure.persistence.mapper;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.infrastructure.persistence.document.BranchDocument;
import com.nequi.franchise.infrastructure.persistence.document.FranchiseDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Franchise y FranchiseDocument
 */
@Component
public class FranchiseMapper {

    private final BranchMapper branchMapper;

    public FranchiseMapper(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    public FranchiseDocument toDocument(Franchise franchise) {
        if (franchise == null) {
            return null;
        }
        List<BranchDocument> branchDocuments = franchise.getBranches().stream()
                .map(branch -> branchMapper.toDocument(branch, franchise.getId()))
                .collect(Collectors.toList());
        
        return new FranchiseDocument(
                franchise.getId(),
                franchise.getName(),
                branchDocuments
        );
    }

    public Franchise toDomain(FranchiseDocument document) {
        if (document == null) {
            return null;
        }
        List<Branch> branches = document.getBranches().stream()
                .map(branchMapper::toDomain)
                .collect(Collectors.toList());
        
        return new Franchise(
                document.getId(),
                document.getName(),
                branches
        );
    }
}
