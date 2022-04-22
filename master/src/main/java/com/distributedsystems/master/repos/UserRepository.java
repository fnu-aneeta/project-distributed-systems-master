package com.distributedsystems.master.repos;

import com.distributedsystems.master.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findFirstByEmail(final String email);
}