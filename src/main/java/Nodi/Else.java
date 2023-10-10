package Nodi;

public class Else extends SyntaxNode{
    public VarDeclList varDeclList;
    public StatList statList;

    public Else(VarDeclList vdl, StatList sl){
        super("Else");
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
}
