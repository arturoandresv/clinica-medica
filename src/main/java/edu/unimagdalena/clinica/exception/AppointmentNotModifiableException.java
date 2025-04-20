package edu.unimagdalena.clinica.exception;

public class AppointmentNotModifiableException extends RuntimeException {
    public AppointmentNotModifiableException(String message) {
        super(message);
    }
}
