package Nodi;

public class Const extends SyntaxNode{

    public IntConst integer;
    public RealConst real;
    public StringConst string;
    public Bool bool;

    public Type type;

    public Const(IntConst ic){
        super("IntConst");
        this.integer = ic;
        this.type = new Type("integer");
        add(ic);
    }

    public Const(RealConst rc){
        super("RealConst");
        this.real = rc;
        this.type = new Type("real");
        add(rc);
    }

    public Const(StringConst sc){
        super("StringConst");
        this.string = sc;
        this.type = new Type("string");
        add(sc);
    }

    public Const(Bool bool){
        super("BoolConst");
        this.bool = bool;
        this.type = new Type("bool");
        add(bool);
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
