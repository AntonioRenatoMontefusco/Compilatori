package Nodi;

public class Main extends SyntaxNode{
    public VarDeclList varDeclList;
    public StatList statList;

    public Main(VarDeclList vdl, StatList sl){
        super("Main");
        if(vdl != null) {
            add(vdl);
            this.varDeclList = vdl;
        }
        if(sl!=null) {
            add(sl);
            this.statList = sl;
        }
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
