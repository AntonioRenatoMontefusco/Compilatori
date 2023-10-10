package Nodi;

public class AssignStat extends SyntaxNode implements IStat{

    public Identifier identifier;
    public Expr expr;

    public AssignStat(Identifier id, Expr expr) {
        super("Assign Statement");

        add(id);
        add(expr);

        this.identifier = id;
        this.expr = expr;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }


    @Override
    public String getStatType() {
        return "AssignStat";
    }

}
