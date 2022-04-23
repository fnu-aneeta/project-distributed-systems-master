package com.distributedsystems.master.repos;

import com.distributedsystems.master.entities.ClientConnectionDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClientConnectionDetailsRepository extends MongoRepository<ClientConnectionDetailsEntity, String> {
    Optional<ClientConnectionDetailsEntity> findFirstByUserEmail(final String email);
}