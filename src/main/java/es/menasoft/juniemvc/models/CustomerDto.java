package es.menasoft.juniemvc.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CustomerDto(
        Integer id,
        Integer version,
        
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        
        @NotBlank(message = "Phone is required")
        @Size(min = 5, max = 20, message = "Phone must be between 5 and 20 characters")
        String phone,
        
        LocalDateTime createdDate,
        LocalDateTime updateDate
) {}