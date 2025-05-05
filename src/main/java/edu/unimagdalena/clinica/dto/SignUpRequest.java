package edu.unimagdalena.clinica.dto;

import java.util.Set;

public record SignUpRequest(String name, String email, String password, Set<String> roles) {
}
