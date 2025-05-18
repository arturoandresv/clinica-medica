package edu.unimagdalena.clinica.exception.alreadyexists;

import edu.unimagdalena.clinica.exception.ResourceAlreadyExistsException;

public class EmailAlreadyExistsException extends ResourceAlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
