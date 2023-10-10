package Exceptions;

public class IdDeclaredException extends RuntimeException {
    public IdDeclaredException(String id) {
        super("Identifier " + id + " already declared in the current scope");
    }
}
