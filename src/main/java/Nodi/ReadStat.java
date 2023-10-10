package Nodi;

public class ReadStat extends SyntaxNode implements IStat{

    public IdList idList;
    public Expr expr;

    public ReadStat(IdList idList, Expr expr) {
        super("ReadStat");

        add(idList);
        add(expr);

        this.idList = idList;
        this.expr = expr;
    }

    public ReadStat(IdList idList) {
        super("ReadStat");

        add(idList);

        this.idList = idList;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }


    @Override
    public String getStatType() {
        return "ReadStat";
    }
}
