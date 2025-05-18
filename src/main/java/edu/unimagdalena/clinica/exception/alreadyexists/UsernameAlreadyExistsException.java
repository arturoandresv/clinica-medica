package edu.unimagdalena.clinica.exception.alreadyexists;

import edu.unimagdalena.clinica.exception.ResourceAlreadyExistsException;

public class UsernameAlreadyExistsException extends ResourceAlreadyExistsException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
