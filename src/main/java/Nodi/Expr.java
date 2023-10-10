package Nodi;

//Aggiungo ogni non terminale con add
//Ogni terminale con super
public class Expr extends SyntaxNode implements Scrollable, IStat {
    public Scrollable next;
    public Type type;//Tipo dell'expr oppure nel caso degli opUnari e binari indica il tipo restituito dall operazione

    public String operation;//Per riconoscere che operazione sto facendo +,-,! ecc
    public Expr e1, e2;
    public Identifier id;
    public Const aConst;
    public CallFun callFun;

    //Costruttore per true e false
    public Expr(Bool bool) {
        super("Expr");
        add(bool);
        next = null;
        type = new Type("bool");
        this.aConst = new Const(bool);
    }

    public Expr(IntConst i) {
        super("Expr");
        add(i);
        next = null;
        type = new Type("integer");
        this.aConst = new Const(i);
    }

    public Expr(StringConst s) {
        super("Expr");
        add(s);
        next = null;
        type = new Type("string");
        this.aConst = new Const(s);
    }

    public Expr(RealConst r) {
        super("Expr");
        add(r);
        next = null;
        type = new Type("real");
        this.aConst = new Const(r);
    }

    public Expr(CallFun callFun) {
        super("Expr");
        add(callFun);
        this.callFun = callFun;
        next = null;
    }

    //Operatori binari
    public Expr(Expr e1, String name, Expr e2) {
        super("Expr " + name);
        add(e1);
        add(e2);
        this.e1 = e1;
        this.e2 = e2;
        this.operation = name;
        next = null;
    }

    //Operatori unari MINUS NOT
    public Expr(String name, Expr e) {
        super("Expr " + name);
        add(e);
        this.e1 = e;
        if (name.equals("-"))
            this.operation = "minus_unary";
        else
            this.operation = name;
        next = null;
    }

    //Per LPAR Expr:expr RPAR
    public Expr(Expr e) {
        super("Expr");
        add(e);
        next = null;

    }

    //Per Identificatori
    public Expr(Identifier i) {
        super("Expr");
        add(i);
        this.id = i;
        next = null;
    }

    public Scrollable getNext() {
        return next;
    }

    public void setNext(Scrollable next) {
        this.next = next;
    }

    //Usata per settare il tipo di ritorno di una op unaria o binaria
    public void setOpType(Type opType) {
        this.type = opType;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String getStatType() {
        return "Expr";
    }

}


