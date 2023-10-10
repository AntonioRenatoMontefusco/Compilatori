package Exceptions;

public class ReturnNotDefinedException extends RuntimeException{

    public ReturnNotDefinedException(String function){
        super("The method has a return statement while no return type for function "+function+" has been defined");
    }
}
