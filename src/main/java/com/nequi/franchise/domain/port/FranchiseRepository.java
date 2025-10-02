package com.nequi.franchise.domain.port;

import com.nequi.franchise.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto para el repositorio de franquicias
 */
public interface FranchiseRepository {
    
    Mono<Franchise> save(Franchise franchise);
    
    Mono<Franchise> findById(String id);
    
    Flux<Franchise> findAll();
    
    Mono<Void> deleteById(String id);
    
    Mono<Boolean> existsById(String id);
}
