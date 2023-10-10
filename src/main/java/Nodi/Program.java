package Nodi;

public class Program extends SyntaxNode {

    public VarDeclList varDeclList;
    public FunList funList;
    public Main main;

    public Program(VarDeclList vdl, FunList fl, Main m){
        super("Program");
        if(vdl != null) {
            add(vdl);
            this.varDeclList = vdl;
        }
        if(fl != null) {
            add(fl);
            this.funList = fl;
        }

        add(m);
        this.main = m;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }



}
