package Nodi;


public class Fun extends SyntaxNode{
    public Fun next;
    public Identifier identifier;
    public ParamDeclList paramDeclList;
    public Type type;
    public VarDeclList varDeclList;
    public StatList statList;

    public Fun(Identifier id,ParamDeclList pl, Type t, VarDeclList vdl, StatList sl){
        super("Fun");

        if(t != null){
            add(t);
            this.type = t;
        }
        if(pl != null){
            add(pl);
            this.paramDeclList = pl;
        }
        if(vdl != null){
            add(vdl);
            this.varDeclList = vdl;
        }
        if (sl != null) {
            add(sl);
            this.statList = sl;
        }

        add(id);
        this.identifier = id;

    }

    public Fun(Identifier id,ParamDeclList pl, VarDeclList vdl, StatList sl){
        super("Fun");

        if(pl != null){
            add(pl);
            this.paramDeclList = pl;
        }
        if(vdl != null){
            add(vdl);
            this.varDeclList = vdl;
        }
        if (sl != null) {
            add(sl);
            this.statList = sl;
        }

        add(id);
        this.identifier = id;

    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}