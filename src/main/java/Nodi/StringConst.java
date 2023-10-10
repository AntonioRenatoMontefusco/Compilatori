package Nodi;

public class StringConst extends SyntaxNode{
    public String value;
    public StringConst(String s){
        super(s);
        value = "\"" + s.substring(1,s.length()-1) + "\"";

    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
