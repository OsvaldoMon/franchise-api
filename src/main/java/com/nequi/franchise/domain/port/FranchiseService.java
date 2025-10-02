package com.nequi.franchise.domain.port;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.model.Franchise.ProductWithBranch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto para el servicio de franquicias
 */
public interface FranchiseService {
    
    // Operaciones de franquicia
    Mono<Franchise> createFranchise(Franchise franchise);
    
    Mono<Franchise> getFranchiseById(String id);
    
    Flux<Franchise> getAllFranchises();
    
    Mono<Franchise> updateFranchiseName(String id, String newName);
    
    Mono<Void> deleteFranchise(String id);
    
    // Operaciones de sucursal
    Mono<Franchise> addBranchToFranchise(String franchiseId, Branch branch);
    
    Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName);
    
    // Operaciones de producto
    Mono<Franchise> addProductToBranch(String franchiseId, String branchId, Product product);
    
    Mono<Franchise> removeProductFromBranch(String franchiseId, String branchId, String productId);
    
    Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, Integer newStock);
    
    Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String newName);
    
    // Consultas especiales
    Flux<ProductWithBranch> getProductsWithMaxStockByFranchise(String franchiseId);
}
