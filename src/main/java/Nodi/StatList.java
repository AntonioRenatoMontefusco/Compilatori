package Nodi;

public class StatList extends SyntaxNode {
    public Stat head;
    
    public StatList(Stat stat){
        super("StatList");
        if(head == null)
            head = stat;
        add(stat);
    }

    public StatList(){super("StatList");}


    public StatList addNode(Stat current){
        Stat last = this.head;

        if(head == null){
            head = current;
        } else {
            while (last.next != null) {
                last = last.next;
            }
            last.next = current;
//            last = current;
        }
        add(current);
        return this;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
