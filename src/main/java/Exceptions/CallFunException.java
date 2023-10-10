package Exceptions;

import Nodi.Expr;
import Nodi.ExprList;
import Nodi.ParamDeclList;
import Nodi.Type;

public class CallFunException extends RuntimeException{
    public CallFunException(String funName){
        super("Actual Params found for function " + funName + " but it doesn't have formal params");
    }
    public CallFunException(int form, int att){
        super("Actual Params found " + att + " but function have " + form + " formal params");
    }
    public CallFunException(int num, Type t1, Type t2){
        super("Param number " + num + " are not equals: Formal " + t1 + " Actual:" + t2);
    }
}
