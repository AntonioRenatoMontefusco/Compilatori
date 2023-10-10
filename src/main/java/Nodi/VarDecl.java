package Nodi;

public class VarDecl extends SyntaxNode{
    public VarDecl next;
    public Type type;
    public IdListInitObbl listObbl;
    public IdListInit listInit;

    public VarDecl(Type t,IdListInit list){
        super("VarDecl");
        add(t);
        add(list);
        this.listInit = list;
        this.type = t;
        this.listInit.type = t;
    }

    public VarDecl(Type t,IdListInitObbl list){
        super("VarDecl");
        add(t);
        add(list);
        this.listObbl = list;
        this.type = t;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
