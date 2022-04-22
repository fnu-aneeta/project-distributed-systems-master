package com.distributedsystems.master.services;

import com.distributedsystems.master.entities.UserEntity;
import com.distributedsystems.master.exceptions.UserAlreadyExistsException;
import com.distributedsystems.master.model.UserResource;
import com.distributedsystems.master.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private UserEntity userResource2Entity(final UserResource userResource){
        return UserEntity.builder()
                .email(userResource.getEmail())
                .firstName(userResource.getFirstName())
                .lastName(userResource.getLastName())
                .build();
    }

    public UserResource createNewUser(UserResource userResource) {
        Optional<UserEntity> optUserEntity = this.userRepository.findFirstByEmail(userResource.getEmail());
        if(optUserEntity.isPresent()){
            final String error = String.format("User with email %s is already exists.", userResource.getEmail());
            throw new UserAlreadyExistsException(error);
        }
        final UserEntity userEntity = this.userRepository.save(userResource2Entity(userResource));
        userResource.setId(userEntity.getId());
        return userResource;
    }

}
