package com.nequi.franchise.application.usecase;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.port.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para FranchiseUseCase
 */
@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseUseCase = new FranchiseUseCase(franchiseRepository);
    }

    @Test
    void testCreateFranchise() {
        // Given
        Franchise franchise = new Franchise("Franquicia Test", List.of());
        Franchise savedFranchise = new Franchise("1", "Franquicia Test", List.of());
        
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(savedFranchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.createFranchise(franchise))
                .expectNext(savedFranchise)
                .verifyComplete();
    }

    @Test
    void testGetFranchiseById() {
        // Given
        String franchiseId = "1";
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.getFranchiseById(franchiseId))
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void testGetFranchiseByIdNotFound() {
        // Given
        String franchiseId = "1";
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(franchiseUseCase.getFranchiseById(franchiseId))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void testGetAllFranchises() {
        // Given
        Franchise franchise1 = new Franchise("1", "Franquicia 1", List.of());
        Franchise franchise2 = new Franchise("2", "Franquicia 2", List.of());
        
        when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise1, franchise2));

        // When & Then
        StepVerifier.create(franchiseUseCase.getAllFranchises())
                .expectNext(franchise1, franchise2)
                .verifyComplete();
    }

    @Test
    void testUpdateFranchiseName() {
        // Given
        String franchiseId = "1";
        String newName = "Nuevo Nombre";
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        Franchise updatedFranchise = new Franchise(franchiseId, newName, List.of());
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.updateFranchiseName(franchiseId, newName))
                .expectNext(updatedFranchise)
                .verifyComplete();
    }

    @Test
    void testAddBranchToFranchise() {
        // Given
        String franchiseId = "1";
        Branch branch = new Branch("Sucursal Test", List.of());
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        Franchise updatedFranchise = new Franchise(franchiseId, "Franquicia Test", List.of(branch));
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.addBranchToFranchise(franchiseId, branch))
                .expectNext(updatedFranchise)
                .verifyComplete();
    }

    @Test
    void testAddProductToBranch() {
        // Given
        String franchiseId = "1";
        String branchId = "1";
        Product product = new Product("Producto Test", 10);
        Branch branch = new Branch(branchId, "Sucursal Test", List.of());
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of(branch));
        Franchise updatedFranchise = new Franchise(franchiseId, "Franquicia Test", List.of(branch));
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.addProductToBranch(franchiseId, branchId, product))
                .expectNext(updatedFranchise)
                .verifyComplete();
    }

    @Test
    void testUpdateProductStock() {
        // Given
        String franchiseId = "1";
        String branchId = "1";
        String productId = "1";
        Integer newStock = 50;
        Product product = new Product(productId, "Producto Test", 10);
        Branch branch = new Branch(branchId, "Sucursal Test", List.of(product));
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of(branch));
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.updateProductStock(franchiseId, branchId, productId, newStock))
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void testGetProductsWithMaxStockByFranchise() {
        // Given
        String franchiseId = "1";
        Product product1 = new Product("1", "Producto 1", 10);
        Product product2 = new Product("2", "Producto 2", 20);
        Branch branch1 = new Branch("1", "Sucursal 1", List.of(product1, product2));
        Branch branch2 = new Branch("2", "Sucursal 2", List.of(new Product("3", "Producto 3", 5)));
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of(branch1, branch2));
        
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));

        // When & Then
        StepVerifier.create(franchiseUseCase.getProductsWithMaxStockByFranchise(franchiseId))
                .expectNextCount(2)
                .verifyComplete();
    }
}

