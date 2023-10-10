package Nodi;

public class ParDecl extends SyntaxNode{

    public ParDecl next;
    public Identifier id;
    public InOut inOut;

    public Type type;
    public ParDecl(Type type, Identifier id){
        super("ParDecl");
        add(type);
        add(id);
        this.type = type;
        this.id = id;
        next = null;
    }

    public ParDecl(InOut inout, Type type, Identifier id){
        super("ParDecl");
        add(inout);
        add(type);
        add(id);
        this.type = type;
        this.id = id;
        this.inOut = inout;
        next = null;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}


