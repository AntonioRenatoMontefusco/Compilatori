package Nodi;

public class WhileStat extends SyntaxNode implements IStat {
    public Expr expr;
    public VarDeclList varDeclList;
    public StatList statList;

    public WhileStat(Expr e,VarDeclList vdl, StatList sl) {
        super("WhileStat");
        add(e);
        this.expr = e;
        if(vdl != null) {
            add(vdl);
            this.varDeclList = vdl;
        }
        if(sl != null) {
            add(sl);
            this.statList = sl;
        }
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String getStatType() {
        return "WhileStat";
    }
}