package edu.unimagdalena.clinica.dto.request;

import java.util.Set;

public record SignUpRequest(String name, String email, String password, Set<String> roles) {
}
