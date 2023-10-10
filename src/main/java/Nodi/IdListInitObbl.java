package Nodi;

public class IdListInitObbl extends SyntaxNode{

//    public Identifier head;
//    public Const constant;

    public IdInit head;

//    public IdListInitObbl(Identifier id,Const cost){
//        super("IdListInitObbl");
//        head = id;
//        add(id);
//        add(cost);
//        this.constant = cost;
//    }

    public IdListInitObbl(IdInit idInit){
        super("IdListInitObbl");
        this.head = idInit;
        add(idInit.id);
        add(idInit.aConst);
    }


    public IdListInitObbl addNode(IdInit idInit){
        IdInit last = this.head;

        while(last.next != null){
            last = last.next;
        }

        last.next = idInit;
        add(idInit.id);
        add(idInit.aConst);
        return this;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
