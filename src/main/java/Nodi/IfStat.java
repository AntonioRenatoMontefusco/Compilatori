package Nodi;

public class IfStat extends SyntaxNode implements IStat{

    public Expr expr;
    public VarDeclList varDeclList;
    public StatList statList;
    public Else anElse;

    public IfStat(Expr e, VarDeclList vdl, StatList sl, Else el){
        super("IfStat");
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
        if(el != null) {
            add(el);
            this.anElse = el;
        }
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String getStatType() {
        return "IfStat";
    }
}
