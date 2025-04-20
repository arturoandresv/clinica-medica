package edu.unimagdalena.clinica.exception.notfound;

import edu.unimagdalena.clinica.exception.ResourceNotFoundException;

public class PatientNotFoundException extends ResourceNotFoundException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
