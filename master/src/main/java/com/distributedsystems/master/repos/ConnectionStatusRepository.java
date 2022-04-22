package com.distributedsystems.master.repos;

import com.distributedsystems.master.entities.ConnectionStatusEntity;
import com.distributedsystems.master.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConnectionStatusRepository extends MongoRepository<ConnectionStatusEntity, String> {
    Optional<ConnectionStatusEntity> findFirstByUserEmail(final String email);
}