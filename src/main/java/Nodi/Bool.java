package Nodi;

public class Bool extends SyntaxNode{
    public Boolean value;

    public Bool(Boolean bool){
        super(bool);
        value = bool;
    }

    public Bool(String bool){
        super(bool);
        value = Boolean.parseBoolean(bool);

    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
