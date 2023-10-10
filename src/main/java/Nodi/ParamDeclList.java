package Nodi;

public class ParamDeclList extends SyntaxNode{
    public NonEmptyParamDeclList nepdl;

    public ParamDeclList(NonEmptyParamDeclList nepdl){
        super("ParamDeclList");
        this.nepdl=nepdl;
        add(nepdl);
    }

    public ParamDeclList(){
        super("ParamDeclList");
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return nepdl.toString();
    }

}
