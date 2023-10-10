package Nodi;

public class IdList extends SyntaxNode {
    public Identifier head;

    public IdList(Identifier id){
        super("IdList");
        if(head == null)
            head = id;
        add(id);
    }

    public IdList addNode(Identifier current){
        Identifier last = this.head;

        if(head == null){
            head = current;
        } else {
            while (last.next != null) {
                last = (Identifier) last.next;
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
