package Nodi;

public class FunList extends SyntaxNode{

    public Fun head;

    public FunList(Fun current){
        super("FunList");
        add(current);
        if(head == null)
            head = current;
    }


    public FunList addNode(Fun current){

        Fun last = this.head;
        if(last == null){
            last = current;
        } else {
            while (last.next != null) {
                last = last.next;
            }
            last.next = current;
        }
        add(current);
        return this;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
