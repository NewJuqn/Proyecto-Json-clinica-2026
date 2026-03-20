package co.edu.uptc.Exceptions;

public class IDNotFoundException extends RuntimeException {
    public IDNotFoundException(String message) {
        super(message);
    }
}