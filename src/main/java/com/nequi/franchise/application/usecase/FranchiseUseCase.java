package com.nequi.franchise.application.usecase;

import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.model.Franchise.ProductWithBranch;
import com.nequi.franchise.domain.port.FranchiseRepository;
import com.nequi.franchise.domain.port.FranchiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Implementación de los casos de uso para franquicias
 */
@Service
public class FranchiseUseCase implements FranchiseService {

    private static final Logger logger = LoggerFactory.getLogger(FranchiseUseCase.class);
    
    private final FranchiseRepository franchiseRepository;

    public FranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        logger.info("Creando nueva franquicia: {}", franchise.getName());
        
        return Mono.just(franchise)
                .doOnNext(f -> f.setId(UUID.randomUUID().toString()))
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Franquicia creada exitosamente con ID: {}", f.getId()))
                .doOnError(error -> logger.error("Error al crear franquicia: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> getFranchiseById(String id) {
        logger.debug("Obteniendo franquicia por ID: {}", id);
        
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada con ID: " + id)))
                .doOnSuccess(f -> logger.info("Franquicia obtenida: {}", f.getName()))
                .doOnError(error -> logger.error("Error al obtener franquicia: {}", error.getMessage()));
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        logger.debug("Obteniendo todas las franquicias");
        
        return franchiseRepository.findAll()
                .doOnNext(f -> logger.debug("Franquicia obtenida: {}", f.getName()))
                .doOnError(error -> logger.error("Error al obtener franquicias: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String id, String newName) {
        logger.info("Actualizando nombre de franquicia ID: {} a: {}", id, newName);
        
        return getFranchiseById(id)
                .doOnNext(franchise -> franchise.setName(newName))
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Nombre de franquicia actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar nombre de franquicia: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteFranchise(String id) {
        logger.info("Eliminando franquicia ID: {}", id);
        
        return getFranchiseById(id)
                .then(franchiseRepository.deleteById(id))
                .doOnSuccess(unused -> logger.info("Franquicia eliminada exitosamente"))
                .doOnError(error -> logger.error("Error al eliminar franquicia: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> addBranchToFranchise(String franchiseId, Branch branch) {
        logger.info("Agregando sucursal {} a franquicia ID: {}", branch.getName(), franchiseId);
        
        return getFranchiseById(franchiseId)
                .doOnNext(franchise -> {
                    branch.setId(UUID.randomUUID().toString());
                    franchise.addBranch(branch);
                })
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Sucursal agregada exitosamente"))
                .doOnError(error -> logger.error("Error al agregar sucursal: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName) {
        logger.info("Actualizando nombre de sucursal ID: {} a: {}", branchId, newName);
        
        return getFranchiseById(franchiseId)
                .doOnNext(franchise -> {
                    franchise.findBranchById(branchId)
                            .ifPresentOrElse(
                                    branch -> branch.setName(newName),
                                    () -> {
                                        throw new RuntimeException("Sucursal no encontrada con ID: " + branchId);
                                    }
                            );
                })
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Nombre de sucursal actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar nombre de sucursal: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> addProductToBranch(String franchiseId, String branchId, Product product) {
        logger.info("Agregando producto {} a sucursal ID: {}", product.getName(), branchId);
        
        return getFranchiseById(franchiseId)
                .doOnNext(franchise -> {
                    franchise.findBranchById(branchId)
                            .ifPresentOrElse(
                                    branch -> {
                                        product.setId(UUID.randomUUID().toString());
                                        branch.addProduct(product);
                                    },
                                    () -> {
                                        throw new RuntimeException("Sucursal no encontrada con ID: " + branchId);
                                    }
                            );
                })
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Producto agregado exitosamente"))
                .doOnError(error -> logger.error("Error al agregar producto: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> removeProductFromBranch(String franchiseId, String branchId, String productId) {
        logger.info("Eliminando producto ID: {} de sucursal ID: {}", productId, branchId);
        
        return getFranchiseById(franchiseId)
                .doOnNext(franchise -> {
                    franchise.findBranchById(branchId)
                            .ifPresentOrElse(
                                    branch -> branch.removeProduct(productId),
                                    () -> {
                                        throw new RuntimeException("Sucursal no encontrada con ID: " + branchId);
                                    }
                            );
                })
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Producto eliminado exitosamente"))
                .doOnError(error -> logger.error("Error al eliminar producto: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> updateProductStock(String franchiseId, String branchId, String productId, Integer newStock) {
        logger.info("Actualizando stock del producto ID: {} a: {}", productId, newStock);
        
        return getFranchiseById(franchiseId)
                .doOnNext(franchise -> {
                    franchise.findBranchById(branchId)
                            .ifPresentOrElse(
                                    branch -> {
                                        branch.findProductById(productId)
                                                .ifPresentOrElse(
                                                        product -> product.updateStock(newStock),
                                                        () -> {
                                                            throw new RuntimeException("Producto no encontrado con ID: " + productId);
                                                        }
                                                );
                                    },
                                    () -> {
                                        throw new RuntimeException("Sucursal no encontrada con ID: " + branchId);
                                    }
                            );
                })
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Stock de producto actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar stock de producto: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String newName) {
        logger.info("Actualizando nombre del producto ID: {} a: {}", productId, newName);
        
        return getFranchiseById(franchiseId)
                .doOnNext(franchise -> {
                    franchise.findBranchById(branchId)
                            .ifPresentOrElse(
                                    branch -> {
                                        branch.findProductById(productId)
                                                .ifPresentOrElse(
                                                        product -> product.setName(newName),
                                                        () -> {
                                                            throw new RuntimeException("Producto no encontrado con ID: " + productId);
                                                        }
                                                );
                                    },
                                    () -> {
                                        throw new RuntimeException("Sucursal no encontrada con ID: " + branchId);
                                    }
                            );
                })
                .flatMap(franchiseRepository::save)
                .doOnSuccess(f -> logger.info("Nombre de producto actualizado exitosamente"))
                .doOnError(error -> logger.error("Error al actualizar nombre de producto: {}", error.getMessage()));
    }

    @Override
    public Flux<ProductWithBranch> getProductsWithMaxStockByFranchise(String franchiseId) {
        logger.info("Obteniendo productos con mayor stock por sucursal para franquicia ID: {}", franchiseId);
        
        return getFranchiseById(franchiseId)
                .map(Franchise::getProductsWithMaxStockByBranch)
                .flatMapMany(Flux::fromIterable)
                .doOnNext(pwb -> logger.debug("Producto con mayor stock: {} en sucursal: {}", 
                        pwb.getProduct().getName(), pwb.getBranchName()))
                .doOnError(error -> logger.error("Error al obtener productos con mayor stock: {}", error.getMessage()));
    }
}
