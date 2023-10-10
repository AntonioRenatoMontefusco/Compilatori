package Exceptions;

import Nodi.Expr;
import Nodi.Type;

public class ExprTypeException extends RuntimeException{
    //Exception for binary op
    public ExprTypeException(Expr e1, String s, Expr e2){
        super("Error " + s + " between " + e1.toString() + " and " + e2.toString());
    }
    public ExprTypeException(String s, Type e1Type){
        super("Error using unary operator " + s + " with " + e1Type.toString());
    }

    /**
     *
     * @param t1 tipo aspettato
     * @param t2 tipo ricevuto
     */
    public ExprTypeException(Type t1,Type t2){
        super("Error: the return statement has a return type "+t1.typeName+" while "+t2.typeName+" expected");
    }
}
