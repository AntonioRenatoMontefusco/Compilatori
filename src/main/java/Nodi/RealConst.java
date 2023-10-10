package Nodi;

public class RealConst extends SyntaxNode{
    public float value;
    public RealConst(float f){
        super(f);
        value = f;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
