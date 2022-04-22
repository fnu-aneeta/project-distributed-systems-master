package com.distributedsystems.master.services;

import com.distributedsystems.master.entities.UserEntity;
import com.distributedsystems.master.exceptions.UserAlreadyExistsException;
import com.distributedsystems.master.model.UserResource;
import com.distributedsystems.master.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private void userResource2Entity(final UserResource userResource, final UserEntity userEntity) {
        if (ObjectUtils.isEmpty(userResource) || ObjectUtils.isEmpty(userEntity)) {
            return;
        }
        userEntity.setEmail(userResource.getEmail());
        userEntity.setFirstName(userResource.getFirstName());
        userEntity.setLastName(userResource.getLastName());
    }

    public UserResource createNewUser(UserResource userResource) {
        log.info("Creating user: {}", userResource.getEmail());
        Optional<UserEntity> optUserEntity = this.userRepository.findFirstByEmail(userResource.getEmail());
        if (optUserEntity.isPresent()) {
            final String error = String.format("User with email %s is already exists.", userResource.getEmail());
            throw new UserAlreadyExistsException(error);
        }
        final UserEntity userEntity = new UserEntity();
        userResource2Entity(userResource, userEntity);
        final UserEntity newlyCreatedUserEntity = this.userRepository.save(userEntity);
        userResource.setId(newlyCreatedUserEntity.getId());
        return userResource;
    }


    public UserResource updateUser(UserResource userResource) {
        log.info("Updating user: {}", userResource.getEmail());
        Optional<UserEntity> optUserEntity = this.userRepository.findFirstByEmail(userResource.getEmail());
        UserEntity userEntity = new UserEntity();
        if (optUserEntity.isPresent()) {
            userEntity = optUserEntity.get();
        }
        userResource2Entity(userResource, userEntity);
        final UserEntity newlyCreatedUserEntity = this.userRepository.save(userEntity);
        userResource.setId(newlyCreatedUserEntity.getId());
        return userResource;
    }
}
