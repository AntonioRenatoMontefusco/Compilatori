package Nodi;

public class IntConst extends SyntaxNode{
    public int value;
    public IntConst(int i){
        super(i);
        this.value = i;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
