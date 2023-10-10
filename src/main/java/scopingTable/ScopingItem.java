package scopingTable;

import Nodi.*;

import java.util.List;

public class ScopingItem {
    public enum ItemType { FUNCTION, VARIABLE }

    private ItemType itemType;
    private String idName;
    private ParamDeclList params;
    private Type returnType;
    private Type variableType;
    private boolean hasReturn;

    // Constructor for variables
    public ScopingItem(String idName, Type variableType) {
        this.itemType = ItemType.VARIABLE;
        this.idName = idName;
        this.variableType = variableType;
        this.params = null;
        this.returnType = null;
    }

    // Constructor for functions
    public ScopingItem(String idName, ParamDeclList params, Type returnType) {
        this.itemType = ItemType.FUNCTION;
        this.idName = idName;
        this.variableType = null;
        this.params = params;
        if(returnType != null) {
            this.returnType = returnType;
            this.hasReturn = true;
        } else{
            this.returnType = null;
            this.hasReturn = false;
        }
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getIdName() {
        return idName;
    }

    public Type getVariableType() {
        return variableType;
    }

    public ParamDeclList getParams() {
        return params;
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean getHasReturn() {
        return hasReturn;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    @Override
    public String toString() {
        return "ScopingItem{" +
                "itemType=" + itemType +
                ", idName='" + idName + '\'' +
                ", params=" + params +
                ", returnType=" + returnType +
                ", variableType=" + variableType +
                ", hasReturn=" + hasReturn +
                '}';
    }
}
