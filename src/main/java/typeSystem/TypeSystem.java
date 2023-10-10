package typeSystem;

import Nodi.Expr;
import Nodi.Type;

public class TypeSystem {

    /**
     * Metodo per il controllo di tipo per gli operatori unari
     * @param opType l'operatore da controllare
     * @param expression operando dell'operazione unaria
     * @return ritorna il tipo di dell'operazione in base all'operando, null altrimenti
     */
    public static Type getTypeForUnaryOp(String opType, Expr expression) {

        if(opType.equals(Constants.MINUS_UNARY)){
            if(expression.type.typeName.equals("integer")) return new Type("integer");
            if(expression.type.typeName.equals("real")) return new Type("real");
        }
        else if(opType.equals("not")){
            if (expression.type.typeName.equals("bool")) return new Type("bool");
        }
        return null;
    }

    public static Type getTypeForBinaryOp(Expr left, String opType, Expr right){
        switch (opType){
            case "+", "*", "-", "/", "^":
                return getTypeForNumericOp(left,opType,right);
            case "div":
                return getTypeForDivInt(left,right);
            case "and","or":
                return getTypeForAndOr(left,right);
            case "=",">=","<=",">","<","!=","<>":
                return getTypeForLogicOp(left,right);
            case "&":
                return getTypeForStrCat(left,right);
        }
        return null;
    }

    public static Type getTypeForNumericOp(Expr left, String opType, Expr right){
        if(left.type.typeName.equals("integer") && right.type.typeName.equals("integer")){
            if(opType.equals("/"))
                return new Type("real");
            else
                return new Type("integer");
        }
        if((left.type.typeName.equals("integer") && right.type.typeName.equals("real"))||
            (left.type.typeName.equals("real") && right.type.typeName.equals("integer"))||
            (left.type.typeName.equals("real") && right.type.typeName.equals("real"))) {
            return new Type("real");
        }

        return null;
    }


    public static Type getTypeForDivInt(Expr left, Expr right) {
        return (left.type.typeName.equals("integer") && right.type.typeName.equals("integer")) ||
                (left.type.typeName.equals("integer") && right.type.typeName.equals("real")) ||
                (left.type.typeName.equals("integer") && right.type.typeName.equals("real")) ||
                (left.type.typeName.equals("real") && right.type.typeName.equals("integer")) ||
                (left.type.typeName.equals("real") && right.type.typeName.equals("real")) ? new Type("integer") : null;
    }

    public static Type getTypeForAndOr(Expr left, Expr right) {
        if(left.type.typeName.equals("bool") && right.type.typeName.equals("bool")){
            return new Type("bool");
        }
        return null;
    }

    public static Type getTypeForLogicOp(Expr left, Expr right) {
        if(left.type.typeName.equals("integer") && right.type.typeName.equals("integer")||
           left.type.typeName.equals("integer") && right.type.typeName.equals("real")||
           left.type.typeName.equals("real") && right.type.typeName.equals("integer")||
           left.type.typeName.equals("real") && right.type.typeName.equals("real") ||
           left.type.typeName.equals("string") && right.type.typeName.equals("string")) {
                return new Type("bool");
        }
        return null;
    }

    public static Type getTypeForStrCat(Expr left, Expr right) {
        if(!left.type.typeName.equals(null)&&!right.type.typeName.equals(null))
            return new Type("string");
        return null;
    }
}
