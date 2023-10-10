package Nodi;

public class InOut extends SyntaxNode {
    public String mode;
    public InOut(String mode){
        super(mode);
        this.mode = mode;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
