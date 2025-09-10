package com.auth.controllers;

import com.auth.advice.validators.unique.FieldValueExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/validation/users")
public class UserValidationController {

    private final FieldValueExists validationService;

    @Autowired
    public UserValidationController(FieldValueExists validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/unique")
    public Map<String, Boolean> isUnique(@RequestParam String field, @RequestParam String value) {
        boolean exists = validationService.fieldValueExists(value.trim(), field);
        return Map.of("exists", exists);
    }
}

