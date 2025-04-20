package edu.unimagdalena.clinica.exception.notfound;

import edu.unimagdalena.clinica.exception.ResourceNotFoundException;

public class DoctorNotFoundException extends ResourceNotFoundException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
