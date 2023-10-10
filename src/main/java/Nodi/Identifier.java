package Nodi;

public class Identifier extends SyntaxNode implements Scrollable{
    public Scrollable next;
    public String name;
    public Type idType;
    public InOut inOut;
    public Identifier (InOut inOut,String s){
        super(s);
        next = null;
        add(inOut);
        name = s;
        this.inOut = inOut;
    }
    public Identifier (String s){
        super(s);
        next = null;
        name = s;
    }

    public Scrollable getNext(){
        return next;
    }

    @Override
    public void setNext(Scrollable next) {
        this.next = next;
    }

    //Usata per settare il tipo dell id
    public void setIDType(Type idType){
        this.idType = idType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
