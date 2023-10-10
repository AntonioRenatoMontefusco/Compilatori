package Nodi;

public class ExprList extends SyntaxNode {
    public Scrollable head;


    public int numberOfParams;//Usata nel caso voglio sapere il numero di par in una funzione
    public ExprList (Expr e){
        super("ExprList");
        if(head == null)
            head = e;
        add(e);
    }


    public ExprList (Identifier id) {
        super("ExprList");
        if(head == null)
            head = id;
        add(id);
    }

    public ExprList addNode(Expr current){

        Scrollable last = this.head;

        while(last.getNext() != null){
            last = last.getNext();
        }

        last.setNext(current);
        add(current);
        return this;
    }

    public ExprList addNode(Identifier current){

        Scrollable last = this.head;

        while(last.getNext() != null){
            last = last.getNext();
        }

        last.setNext(current);
        add(current);
        return this;
    }
    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
