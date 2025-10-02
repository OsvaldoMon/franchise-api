package com.nequi.franchise.infrastructure.persistence.repository;

import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.port.FranchiseRepository;
import com.nequi.franchise.infrastructure.persistence.document.FranchiseDocument;
import com.nequi.franchise.infrastructure.persistence.mapper.FranchiseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación reactiva del repositorio de franquicias
 */
@Repository
public class ReactiveFranchiseRepository implements FranchiseRepository {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveFranchiseRepository.class);
    
    private final ReactiveMongoTemplate mongoTemplate;
    private final FranchiseMapper franchiseMapper;

    public ReactiveFranchiseRepository(ReactiveMongoTemplate mongoTemplate, FranchiseMapper franchiseMapper) {
        this.mongoTemplate = mongoTemplate;
        this.franchiseMapper = franchiseMapper;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        logger.debug("Guardando franquicia: {}", franchise.getName());
        
        return Mono.just(franchise)
                .map(franchiseMapper::toDocument)
                .flatMap(mongoTemplate::save)
                .map(franchiseMapper::toDomain)
                .doOnSuccess(saved -> logger.info("Franquicia guardada exitosamente: {}", saved.getId()))
                .doOnError(error -> logger.error("Error al guardar franquicia: {}", error.getMessage()));
    }

    @Override
    public Mono<Franchise> findById(String id) {
        logger.debug("Buscando franquicia por ID: {}", id);
        
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, FranchiseDocument.class)
                .map(franchiseMapper::toDomain)
                .doOnSuccess(franchise -> {
                    if (franchise != null) {
                        logger.info("Franquicia encontrada: {}", franchise.getName());
                    } else {
                        logger.warn("Franquicia no encontrada con ID: {}", id);
                    }
                })
                .doOnError(error -> logger.error("Error al buscar franquicia: {}", error.getMessage()));
    }

    @Override
    public Flux<Franchise> findAll() {
        logger.debug("Buscando todas las franquicias");
        
        return mongoTemplate.findAll(FranchiseDocument.class)
                .map(franchiseMapper::toDomain)
                .doOnNext(franchise -> logger.debug("Franquicia encontrada: {}", franchise.getName()))
                .doOnError(error -> logger.error("Error al buscar franquicias: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        logger.debug("Eliminando franquicia por ID: {}", id);
        
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, FranchiseDocument.class)
                .then()
                .doOnSuccess(unused -> logger.info("Franquicia eliminada exitosamente: {}", id))
                .doOnError(error -> logger.error("Error al eliminar franquicia: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        logger.debug("Verificando existencia de franquicia por ID: {}", id);
        
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, FranchiseDocument.class)
                .doOnNext(exists -> logger.debug("Franquicia existe: {}", exists))
                .doOnError(error -> logger.error("Error al verificar existencia de franquicia: {}", error.getMessage()));
    }
}
