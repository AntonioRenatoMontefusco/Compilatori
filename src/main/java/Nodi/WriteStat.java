package Nodi;

public class WriteStat extends SyntaxNode implements IStat{
    public Expr expr;
    public WriteType writeType;

    public WriteStat (Expr expr,WriteType writeType){
        super("WriteStat");
        add(expr);
        this.expr = expr;
        this.writeType = writeType;
    }

//    public WriteStat(String str, Expr expr){
//        super(str);
//        add(expr);
//    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String getStatType() {
        return "WriteStat";
    }
}
