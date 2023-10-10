package Nodi;

public class ReturnStat extends SyntaxNode implements IStat{

    public Expr toReturn;

    public ReturnStat (Expr expr){
        super("Return Statement");
        this.toReturn = expr;
    }

    @Override
    public String getStatType() {
        return "ReturnStat";
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
