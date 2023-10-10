package Nodi;

public class IdListInit extends SyntaxNode{
    public IdInit head;
    public Type type;

    public IdListInit(IdInit identifier){
        super("IdListInit");
        add(identifier.id);
        if(head == null)
            head = identifier;
    }

    public IdListInit addNode(IdInit current){


        IdInit last = this.head;

        while(last.next != null){
            last = last.next;
        }
        last.next = current;
        add(current.id);
        if(current.expr != null)
            add(current.expr);
        return this;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
