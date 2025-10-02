package com.nequi.franchise.infrastructure.web.controller;

import com.nequi.franchise.application.usecase.FranchiseUseCase;
import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.infrastructure.web.dto.BranchDto;
import com.nequi.franchise.infrastructure.web.dto.FranchiseDto;
import com.nequi.franchise.infrastructure.web.dto.ProductDto;
import com.nequi.franchise.infrastructure.web.mapper.FranchiseWebMapper;
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
 * Pruebas unitarias para FranchiseController
 */
@ExtendWith(MockitoExtension.class)
class FranchiseControllerTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;

    @Mock
    private FranchiseWebMapper franchiseWebMapper;

    private FranchiseController franchiseController;

    @BeforeEach
    void setUp() {
        franchiseController = new FranchiseController(franchiseUseCase, franchiseWebMapper);
    }

    @Test
    void testCreateFranchise() {
        // Given
        FranchiseDto franchiseDto = new FranchiseDto("Franquicia Test", List.of());
        Franchise franchise = new Franchise("Franquicia Test", List.of());
        Franchise savedFranchise = new Franchise("1", "Franquicia Test", List.of());
        FranchiseDto savedFranchiseDto = new FranchiseDto("1", "Franquicia Test", List.of());
        
        when(franchiseWebMapper.toDomain(franchiseDto)).thenReturn(franchise);
        when(franchiseUseCase.createFranchise(franchise)).thenReturn(Mono.just(savedFranchise));
        when(franchiseWebMapper.toDto(savedFranchise)).thenReturn(savedFranchiseDto);

        // When & Then
        StepVerifier.create(franchiseController.createFranchise(franchiseDto))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testGetAllFranchises() {
        // Given
        Franchise franchise1 = new Franchise("1", "Franquicia 1", List.of());
        Franchise franchise2 = new Franchise("2", "Franquicia 2", List.of());
        FranchiseDto franchiseDto1 = new FranchiseDto("1", "Franquicia 1", List.of());
        FranchiseDto franchiseDto2 = new FranchiseDto("2", "Franquicia 2", List.of());
        
        when(franchiseUseCase.getAllFranchises()).thenReturn(Flux.just(franchise1, franchise2));
        when(franchiseWebMapper.toDto(franchise1)).thenReturn(franchiseDto1);
        when(franchiseWebMapper.toDto(franchise2)).thenReturn(franchiseDto2);

        // When & Then
        StepVerifier.create(franchiseController.getAllFranchises())
                .expectNext(franchiseDto1, franchiseDto2)
                .verifyComplete();
    }

    @Test
    void testGetFranchiseById() {
        // Given
        String franchiseId = "1";
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        FranchiseDto franchiseDto = new FranchiseDto(franchiseId, "Franquicia Test", List.of());
        
        when(franchiseUseCase.getFranchiseById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseWebMapper.toDto(franchise)).thenReturn(franchiseDto);

        // When & Then
        StepVerifier.create(franchiseController.getFranchiseById(franchiseId))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testGetFranchiseByIdNotFound() {
        // Given
        String franchiseId = "1";
        
        when(franchiseUseCase.getFranchiseById(franchiseId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(franchiseController.getFranchiseById(franchiseId))
                .expectNextMatches(response -> response.getStatusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void testUpdateFranchiseName() {
        // Given
        String franchiseId = "1";
        String newName = "Nuevo Nombre";
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        Franchise updatedFranchise = new Franchise(franchiseId, newName, List.of());
        FranchiseDto updatedFranchiseDto = new FranchiseDto(franchiseId, newName, List.of());
        
        when(franchiseUseCase.updateFranchiseName(franchiseId, newName)).thenReturn(Mono.just(updatedFranchise));
        when(franchiseWebMapper.toDto(updatedFranchise)).thenReturn(updatedFranchiseDto);

        // When & Then
        StepVerifier.create(franchiseController.updateFranchiseName(franchiseId, newName))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testAddBranchToFranchise() {
        // Given
        String franchiseId = "1";
        BranchDto branchDto = new BranchDto("Sucursal Test", List.of());
        Branch branch = new Branch("Sucursal Test", List.of());
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        FranchiseDto franchiseDto = new FranchiseDto(franchiseId, "Franquicia Test", List.of());
        
        when(franchiseWebMapper.toDomain(branchDto)).thenReturn(branch);
        when(franchiseUseCase.addBranchToFranchise(franchiseId, branch)).thenReturn(Mono.just(franchise));
        when(franchiseWebMapper.toDto(franchise)).thenReturn(franchiseDto);

        // When & Then
        StepVerifier.create(franchiseController.addBranchToFranchise(franchiseId, branchDto))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testAddProductToBranch() {
        // Given
        String franchiseId = "1";
        String branchId = "1";
        ProductDto productDto = new ProductDto("Producto Test", 10);
        Product product = new Product("Producto Test", 10);
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        FranchiseDto franchiseDto = new FranchiseDto(franchiseId, "Franquicia Test", List.of());
        
        when(franchiseWebMapper.toDomain(productDto)).thenReturn(product);
        when(franchiseUseCase.addProductToBranch(franchiseId, branchId, product)).thenReturn(Mono.just(franchise));
        when(franchiseWebMapper.toDto(franchise)).thenReturn(franchiseDto);

        // When & Then
        StepVerifier.create(franchiseController.addProductToBranch(franchiseId, branchId, productDto))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testUpdateProductStock() {
        // Given
        String franchiseId = "1";
        String branchId = "1";
        String productId = "1";
        Integer newStock = 50;
        Franchise franchise = new Franchise(franchiseId, "Franquicia Test", List.of());
        FranchiseDto franchiseDto = new FranchiseDto(franchiseId, "Franquicia Test", List.of());
        
        when(franchiseUseCase.updateProductStock(franchiseId, branchId, productId, newStock)).thenReturn(Mono.just(franchise));
        when(franchiseWebMapper.toDto(franchise)).thenReturn(franchiseDto);

        // When & Then
        StepVerifier.create(franchiseController.updateProductStock(franchiseId, branchId, productId, newStock))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }
}

