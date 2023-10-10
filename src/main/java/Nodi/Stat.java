package Nodi;

public class Stat extends SyntaxNode{
   public Stat next;
   public IStat current;

    public Stat(IfStat stat){
        super("Stat");
        add(stat);
        this.current = stat;
    }

    public Stat(WhileStat stat){
        super("Stat");
        add(stat);
        this.current = stat;
    }
    public Stat(ReadStat stat){
        super("Stat");
        add(stat);
        this.current = stat;

    }
    public Stat(WriteStat stat){
        super("Stat");
        add(stat);
        this.current = stat;
    }

    public Stat(AssignStat stat){
        super("Stat");
        add(stat);
        this.current = stat;
    }

    public Stat(CallFun stat){
        super("Stat");
        add(stat);
        this.current = stat;
    }

    public Stat(ReturnStat stat){
        super("Stat");
        add(stat);
        this.current = stat;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
