package Nodi;

public class Var extends SyntaxNode{
    Type type;

    public Var(){
        super("VAR");
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
