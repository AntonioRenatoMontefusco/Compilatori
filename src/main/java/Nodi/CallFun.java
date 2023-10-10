package Nodi;

public class CallFun extends SyntaxNode implements IStat{

    public Identifier identifier;
    public ExprList exprList;

    public Type callFunType;
    public CallFun(Identifier s, ExprList el) {
        super("CallFun " + s);
        add(el);
        this.exprList = el;
        this.identifier = s;
    }

    public CallFun(Identifier s){
        super(s);
        this.identifier=s;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    //Usata per settare il tipo di ritorno di una op unaria o binaria
    public void setCallFunType(Type callFunType){
        this.callFunType = callFunType;
    }

    @Override
    public String getStatType() {
        return "CallFun";
    }
}
