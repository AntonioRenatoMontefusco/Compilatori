package Nodi;

public class VarDeclList extends SyntaxNode{
    public VarDecl head;

    public VarDeclList(VarDecl head){
        super("VarDeclList");
        if(this.head == null){
              this.head = head;
              add(head);
        }
    }


    public VarDeclList addNode(VarDecl current){
        VarDecl last = this.head;
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
