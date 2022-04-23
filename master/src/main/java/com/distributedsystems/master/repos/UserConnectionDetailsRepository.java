package com.distributedsystems.master.repos;

import com.distributedsystems.master.entities.UserConnectionDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserConnectionDetailsRepository extends MongoRepository<UserConnectionDetailsEntity, String> {
    Optional<UserConnectionDetailsEntity> findFirstByUserEmail(final String email);
}