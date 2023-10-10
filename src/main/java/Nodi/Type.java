package Nodi;

public class Type extends SyntaxNode{
    public String typeName;

    public Type(String t){
        super(t);
        typeName = t;
    }

    public Type(Var v){
        super(v);
        typeName = "VAR";
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() != getClass())
            return false;
        return ((Type)o).typeName.equals(this.typeName);
    }
}
