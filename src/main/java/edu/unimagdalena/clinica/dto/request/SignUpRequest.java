package edu.unimagdalena.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SignUpRequest(@NotBlank String username,
                            @NotBlank String email,
                            @NotBlank String password,
                            Set<String> roles) {
}
