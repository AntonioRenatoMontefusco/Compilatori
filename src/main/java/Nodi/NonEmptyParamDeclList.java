package Nodi;

import java.util.ArrayList;

public class NonEmptyParamDeclList extends SyntaxNode{

    public ParDecl head;

    public int numberOfParams;

    public NonEmptyParamDeclList(ParDecl current){
        super("NonEmptyParamDeclList");
        add(current);
        if(head == null)
            head = current;
    }

    public NonEmptyParamDeclList addNode(ParDecl current){

        ParDecl last = this.head;

        while(last.next != null){
            last = last.next;
        }

        last.next = current;
        add(current);
        return this;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        ParDecl current = head;
        String toPrint = "";
        while(current != null){
            toPrint += "Type: "+current.type+" Id: "+ current.id.name;
            current = current.next;
        }
        return "{"+toPrint+"}";
    }


}
