package Exceptions;

public class VariableNotDeclaredException extends RuntimeException{
    public VariableNotDeclaredException(String id) {
        super("Variable " + id + " was not declared");
    }
}