package com.auth.advice.validators.unique;

import com.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserExistsValidationService implements FieldValueExists {

    private final UserRepository userRepository;

    @Autowired
    public UserExistsValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) {
        return switch (fieldName) {
            case "username" -> userRepository.existsByUsername(value.toString());
            case "email" -> userRepository.existsByEmail(value.toString());
            default -> throw new UnsupportedOperationException("Field not supported: " + fieldName);
        };
    }

}
