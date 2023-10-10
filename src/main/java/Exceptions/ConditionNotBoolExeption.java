package Exceptions;

import Nodi.Type;

public class ConditionNotBoolExeption extends RuntimeException{
    public ConditionNotBoolExeption(String instructionName, Type type) {
            super("The condition in " + instructionName + " statement is " + type.typeName + ", but it must be bool");
    }
}
