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
        
        @NotBlank(message = "Address line 1 is required")
        @Size(max = 255, message = "Address line 1 must be less than 255 characters")
        String addressLine1,
        
        @Size(max = 255, message = "Address line 2 must be less than 255 characters")
        String addressLine2,
        
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be less than 100 characters")
        String city,
        
        @NotBlank(message = "State is required")
        @Size(max = 50, message = "State must be less than 50 characters")
        String state,
        
        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must be less than 20 characters")
        String postalCode,
        
        LocalDateTime createdDate,
        LocalDateTime updateDate
) {}