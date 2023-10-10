package Exceptions;

import Nodi.Type;

public class TypeAssignException extends RuntimeException{

    public TypeAssignException(String id, Type varType, Type exprType){
        super("Cannot assign type "+ exprType.typeName+" to a VAR of type "+ varType.typeName);
    }

    public TypeAssignException(Type varType, Type exprType){
        super("Cannot assign type "+ exprType.typeName+" to a variable of type "+ varType.typeName);
    }
}
