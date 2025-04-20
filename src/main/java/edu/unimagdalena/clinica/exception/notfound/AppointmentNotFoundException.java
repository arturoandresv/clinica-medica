package edu.unimagdalena.clinica.exception.notfound;

import edu.unimagdalena.clinica.exception.ResourceNotFoundException;

public class AppointmentNotFoundException extends ResourceNotFoundException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
