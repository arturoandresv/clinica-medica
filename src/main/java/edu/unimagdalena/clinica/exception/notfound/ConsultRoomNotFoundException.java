package edu.unimagdalena.clinica.exception.notfound;

import edu.unimagdalena.clinica.exception.ResourceNotFoundException;

public class ConsultRoomNotFoundException extends ResourceNotFoundException {
    public ConsultRoomNotFoundException(String message) {
        super(message);
    }
}
