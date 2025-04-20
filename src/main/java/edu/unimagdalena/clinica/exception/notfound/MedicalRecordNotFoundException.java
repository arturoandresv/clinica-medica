package edu.unimagdalena.clinica.exception.notfound;

import edu.unimagdalena.clinica.exception.ResourceNotFoundException;

public class MedicalRecordNotFoundException extends ResourceNotFoundException {
    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
}
