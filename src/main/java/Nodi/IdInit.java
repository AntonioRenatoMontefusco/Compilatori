package Nodi;

public class IdInit extends SyntaxNode{

    public IdInit next;
    public Identifier id;
    public Expr expr;
    public Const aConst;
    public Type type;

    public IdInit(Identifier id, Expr expr){
        this.id=id;
        this.expr=expr;
        this.aConst = null;
        this.next = null;
    }

    public IdInit(Identifier id, Const aConst){
        this.aConst = aConst;
        this.id=id;
        this.expr = null;
        this.next= null;
    }

    public IdInit(Identifier id) {
        this.aConst = null;
        this.id=id;
        this.expr = null;
        this.next= null;
    }

    @Override
    public Object accept(Visitor v) {
        return null;
    }

    public void setType(Type type){
        this.type = type;
    }

}
