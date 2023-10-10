package Exceptions;

public class FunNotDeclaredException extends RuntimeException{
    public FunNotDeclaredException(String id) {
        super("Fun " + id + " used but not declared");
    }
}