package com.nequi.franchise.infrastructure.web.controller;

import com.nequi.franchise.application.usecase.FranchiseUseCase;
import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.model.Franchise.ProductWithBranch;
import com.nequi.franchise.infrastructure.web.dto.BranchDto;
import com.nequi.franchise.infrastructure.web.dto.FranchiseDto;
import com.nequi.franchise.infrastructure.web.dto.ProductDto;
import com.nequi.franchise.infrastructure.web.dto.ProductWithBranchDto;
import com.nequi.franchise.infrastructure.web.mapper.FranchiseWebMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para franquicias
 */
@RestController
@RequestMapping("/api/v1/franchises")
@CrossOrigin(origins = "*")
public class FranchiseController {

    private static final Logger logger = LoggerFactory.getLogger(FranchiseController.class);
    
    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseWebMapper franchiseWebMapper;

    public FranchiseController(FranchiseUseCase franchiseUseCase, FranchiseWebMapper franchiseWebMapper) {
        this.franchiseUseCase = franchiseUseCase;
        this.franchiseWebMapper = franchiseWebMapper;
    }

    /**
     * Crear una nueva franquicia
     */
    @PostMapping
    public Mono<ResponseEntity<FranchiseDto>> createFranchise(@Valid @RequestBody FranchiseDto franchiseDto) {
        logger.info("Creando nueva franquicia: {}", franchiseDto.getName());
        
        return Mono.just(franchiseDto)
                .map(franchiseWebMapper::toDomain)
                .flatMap(franchiseUseCase::createFranchise)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> logger.info("Franquicia creada exitosamente"))
                .doOnError(error -> logger.error("Error al crear franquicia: {}", error.getMessage()));
    }

    /**
     * Obtener todas las franquicias
     */
    @GetMapping
    public Flux<FranchiseDto> getAllFranchises() {
        logger.debug("Obteniendo todas las franquicias");
        
        return franchiseUseCase.getAllFranchises()
                .map(franchiseWebMapper::toDto)
                .doOnNext(franchise -> logger.debug("Franquicia obtenida: {}", franchise.getName()))
                .doOnError(error -> logger.error("Error al obtener franquicias: {}", error.getMessage()));
    }

    /**
     * Obtener franquicia por ID
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FranchiseDto>> getFranchiseById(@PathVariable String id) {
        logger.debug("Obteniendo franquicia por ID: {}", id);
        
        return franchiseUseCase.getFranchiseById(id)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Franquicia obtenida exitosamente"))
                .doOnError(error -> logger.error("Error al obtener franquicia: {}", error.getMessage()));
    }

    /**
     * Actualizar nombre de franquicia
     */
    @PutMapping("/{id}/name")
    public Mono<ResponseEntity<FranchiseDto>> updateFranchiseName(
            @PathVariable String id, 
            @RequestBody String newName) {
        logger.info("Actualizando nombre de franquicia ID: {} a: {}", id, newName);
        
        return franchiseUseCase.updateFranchiseName(id, newName)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Nombre de franquicia actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar nombre de franquicia: {}", error.getMessage()));
    }

    /**
     * Eliminar franquicia
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFranchise(@PathVariable String id) {
        logger.info("Eliminando franquicia ID: {}", id);
        
        return franchiseUseCase.deleteFranchise(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Franquicia eliminada exitosamente"))
                .doOnError(error -> logger.error("Error al eliminar franquicia: {}", error.getMessage()));
    }

    /**
     * Agregar sucursal a franquicia
     */
    @PostMapping("/{franchiseId}/branches")
    public Mono<ResponseEntity<FranchiseDto>> addBranchToFranchise(
            @PathVariable String franchiseId,
            @Valid @RequestBody BranchDto branchDto) {
        logger.info("Agregando sucursal {} a franquicia ID: {}", branchDto.getName(), franchiseId);
        
        return Mono.just(branchDto)
                .map(franchiseWebMapper::toDomain)
                .flatMap(branch -> franchiseUseCase.addBranchToFranchise(franchiseId, branch))
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Sucursal agregada exitosamente"))
                .doOnError(error -> logger.error("Error al agregar sucursal: {}", error.getMessage()));
    }

    /**
     * Actualizar nombre de sucursal
     */
    @PutMapping("/{franchiseId}/branches/{branchId}/name")
    public Mono<ResponseEntity<FranchiseDto>> updateBranchName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @RequestBody String newName) {
        logger.info("Actualizando nombre de sucursal ID: {} a: {}", branchId, newName);
        
        return franchiseUseCase.updateBranchName(franchiseId, branchId, newName)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Nombre de sucursal actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar nombre de sucursal: {}", error.getMessage()));
    }

    /**
     * Agregar producto a sucursal
     */
    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public Mono<ResponseEntity<FranchiseDto>> addProductToBranch(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @Valid @RequestBody ProductDto productDto) {
        logger.info("Agregando producto {} a sucursal ID: {}", productDto.getName(), branchId);
        
        return Mono.just(productDto)
                .map(franchiseWebMapper::toDomain)
                .flatMap(product -> franchiseUseCase.addProductToBranch(franchiseId, branchId, product))
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Producto agregado exitosamente"))
                .doOnError(error -> logger.error("Error al agregar producto: {}", error.getMessage()));
    }

    /**
     * Eliminar producto de sucursal
     */
    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<FranchiseDto>> removeProductFromBranch(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId) {
        logger.info("Eliminando producto ID: {} de sucursal ID: {}", productId, branchId);
        
        return franchiseUseCase.removeProductFromBranch(franchiseId, branchId, productId)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Producto eliminado exitosamente"))
                .doOnError(error -> logger.error("Error al eliminar producto: {}", error.getMessage()));
    }

    /**
     * Actualizar stock de producto
     */
    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<ResponseEntity<FranchiseDto>> updateProductStock(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody Integer newStock) {
        logger.info("Actualizando stock del producto ID: {} a: {}", productId, newStock);
        
        return franchiseUseCase.updateProductStock(franchiseId, branchId, productId, newStock)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Stock de producto actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar stock de producto: {}", error.getMessage()));
    }

    /**
     * Actualizar nombre de producto
     */
    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/name")
    public Mono<ResponseEntity<FranchiseDto>> updateProductName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestBody String newName) {
        logger.info("Actualizando nombre del producto ID: {} a: {}", productId, newName);
        
        return franchiseUseCase.updateProductName(franchiseId, branchId, productId, newName)
                .map(franchiseWebMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> logger.info("Nombre de producto actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar nombre de producto: {}", error.getMessage()));
    }

    /**
     * Obtener productos con mayor stock por sucursal para una franquicia
     */
    @GetMapping("/{franchiseId}/products/max-stock")
    public Flux<ProductWithBranchDto> getProductsWithMaxStockByFranchise(@PathVariable String franchiseId) {
        logger.info("Obteniendo productos con mayor stock por sucursal para franquicia ID: {}", franchiseId);
        
        return franchiseUseCase.getProductsWithMaxStockByFranchise(franchiseId)
                .map(franchiseWebMapper::toDto)
                .doOnNext(pwb -> logger.debug("Producto con mayor stock: {} en sucursal: {}", 
                        pwb.getProduct().getName(), pwb.getBranchName()))
                .doOnError(error -> logger.error("Error al obtener productos con mayor stock: {}", error.getMessage()));
    }

    /**
     * Manejador de excepciones global
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        logger.error("Error no manejado: {}", ex.getMessage(), ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage()));
    }
}
