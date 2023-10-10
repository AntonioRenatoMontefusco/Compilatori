package scopingTable;

import Exceptions.*;
import Nodi.*;
import typeSystem.Constants;
import typeSystem.TypeSystem;

public class SemanticAnalyzer implements Visitor {

    private ScopingTable scopingTable;

    public SemanticAnalyzer(ScopingTable scopingTable) {
        this.scopingTable = scopingTable;
    }


    @Override
    public Object visit(Program p) {
        scopingTable.enterScope("Tabella Globale");

        VarDeclList vdl = p.varDeclList;
        if (vdl != null) {
            vdl.accept(this);
        }

        FunList fun = p.funList;
        if (fun != null) {
            fun.accept(this);
        }

        p.main.accept(this);

        printScopingTable(scopingTable);
        //in caso di problemi di scope spostare lo scope dentro il main alla fine di tutto
        scopingTable.exitScope();

        return null;
    }

    @Override
    public Object visit(VarDeclList vdl) {
        VarDecl current = vdl.head;

        while (current != null) {
            current.accept(this);
            current = current.next;
        }

        return null;
    }

    @Override
    public Object visit(VarDecl vd) {
        //Controlla se è di tipo VAR
        if (vd != null && vd.type.typeName.equals("VAR")) {
            IdListInitObbl ob = vd.listObbl;
            ob.accept(this);
        } else {  //Ho una IdListInit, quindi itero sulla lista e metto nella tabella gli identificatori dichiarati
            IdListInit idl = vd.listInit;
            idl.type = vd.type;//Mi porto il tipo
            idl.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(IdListInitObbl ob) {
        IdInit idInit = ob.head;
        while (idInit != null) {
            if (scopingTable.probe(idInit.id.name))
                throw new IdDeclaredException(idInit.id.name);
            scopingTable.addId(new ScopingItem(idInit.id.name, idInit.aConst.type));
            idInit.setType(idInit.aConst.type);
            idInit = idInit.next;
        }

        return null;
    }


    @Override
    public Object visit(IdListInit i) {
        IdInit head = i.head;

        while (head != null) {
            if (scopingTable.probe(head.id.getName()))//Vedo se è dichiarato nello scope
                throw new IdDeclaredException(head.id.getName());
            if(head.expr != null){
                head.expr.accept(this);
                Expr actual = head.expr;
                if(actual.type.equals(i.type)) {
                    scopingTable.addId(new ScopingItem(head.id.getName(), i.type));
                    head.setType(i.type);
                }
                else
                    throw new TypeAssignException(i.type,actual.type);
            } else {
                scopingTable.addId(new ScopingItem(head.id.getName(), i.type));
                head.setType(i.type);
            }
            head = head.next;
        }

        return null;
    }


    @Override
    public Object visit(FunList f) {
        Fun current = f.head;

        while (current != null) {
            current.accept(this);
            current = current.next;
        }

        return null;
    }

    @Override
    public Object visit(Fun f) {

        //Controllo se già c'è nella tabella
        if (scopingTable.probe(f.identifier.name))
            throw new IdDeclaredException(f.identifier.name);

        //Aggiungo la funlist alla scopingTable della Program
        if (f.type == null)
            scopingTable.addId(new ScopingItem(f.identifier.name, f.paramDeclList, null));
        else
            scopingTable.addId(new ScopingItem(f.identifier.name, f.paramDeclList, f.type));

        //Entro nello scope della tabella fun
        scopingTable.enterScope("Tabella Fun "+ f.identifier.name);


        if (f.paramDeclList != null) {
            f.paramDeclList.accept(this);
        }

        if (f.varDeclList != null)
            f.varDeclList.accept(this);

        if (f.statList != null) {
            f.statList.functionIdName = f.identifier.name;
            f.statList.accept(this);
        }

        if (f.type != null && !scopingTable.lookup(f.identifier.name).getHasReturn())
            throw new RuntimeException("Function " + f.identifier + " must return a " + f.type.toString());


        printScopingTable(scopingTable);
        scopingTable.exitScope();

        return null;
    }

    @Override
    public Object visit(Main m) {
        scopingTable.enterScope("Tabella Main");

        VarDeclList vdl = m.varDeclList;
        StatList sl = m.statList;

        if (vdl != null)
            vdl.accept(this);

        if (sl != null)
            sl.accept(this);

        printScopingTable(scopingTable);

        scopingTable.exitScope();
        return null;
    }

    @Override
    public Object visit(AssignStat a) {
        //Controllo se l'identificatore è stato dichiarato
        ScopingItem scopingItem = scopingTable.lookup(a.identifier.name);
        if (scopingItem == null)
            throw new VariableNotDeclaredException(a.identifier.name);
        a.expr.accept(this);
        Type varType = scopingItem.getVariableType();
        Type exprType = a.expr.type;
        a.identifier.setIDType(varType);

        if (!varType.equals(exprType) && !(varType.typeName.equals("integer") && varType.typeName.equals("real"))) {
            throw new TypeAssignException(a.identifier.name, varType, exprType);
        }
        return null;
    }


    @Override
    public Object visit(CallFun c) {
        ScopingItem scopingItem = scopingTable.lookup(c.identifier.name);//Controllo se è gia stata dichiarata
        if (scopingItem == null)
            throw new FunNotDeclaredException(c.identifier.name);//Funzione usata ma non dichiarta

        if (scopingItem.getReturnType() != null)
            c.setCallFunType(scopingItem.getReturnType());

        c.identifier.accept(this);

        ExprList el = c.exprList;//parametri attuali
        if (el != null)
            el.accept(this);

        ParamDeclList pdl = scopingItem.getParams();//Parametri formali
        if (pdl == null && el != null) {
            throw new CallFunException(c.identifier.name); //Ci sono dei parametri attuali, ma la funzione non aveva parametri formali
        }
        // nel caso la call fun venga chiamata senza passare espressioni dentro (es: funz())
        if (el != null) {
            if (pdl.nepdl.numberOfParams != el.numberOfParams) {
                throw new CallFunException(pdl.nepdl.numberOfParams, el.numberOfParams);
            }
            int parNum = 0;
            Scrollable e = el.head;
            for (ParDecl p = pdl.nepdl.head; e != null; e = e.getNext(), p = p.next) {//Itero i parametri
                parNum++;
                if (e instanceof Identifier && !p.type.equals(((Identifier)e).idType))
                    throw new CallFunException(parNum, p.type, ((Identifier)e).idType);
                else if(e instanceof Expr && !p.type.equals(((Expr)e).type))
                    throw new CallFunException(parNum, p.type, ((Expr)e).type);
            }
        } else if (pdl != null && pdl.nepdl.numberOfParams != 0)
            throw new CallFunException(pdl.nepdl.numberOfParams, 0);

        return null;
    }



    @Override
    public Object visit(Else e) {

        scopingTable.enterScope("Tabella Else");
        if(e.varDeclList != null)
            e.varDeclList.accept(this);

        if (e.statList != null)
            e.statList.accept(this);

        printScopingTable(scopingTable);
        scopingTable.exitScope();
        return null;
    }


    @Override
    public Object visit(ExprList e) {
        Scrollable current = e.head;

        while (current != null) {
            if(current instanceof Expr){
                ((Expr)current).accept(this);
            } else if(current instanceof Identifier){
                ((Identifier)current).accept(this);
            }
            current = current.getNext();
            e.numberOfParams++;
        }

        return null;
    }

    @Override
    public Object visit(Expr e) {

        String op = e.operation;

        if (op != null) {
            switch (op) {
                case Constants.NOT_UNARY, Constants.MINUS_UNARY -> {
                    e.e1.accept(this);
                    Type opType = TypeSystem.getTypeForUnaryOp(e.operation, e.e1);
                    if (opType == null)
                        throw new ExprTypeException(e.operation, e.e1.type);
                    e.setOpType(opType);
                }

                case Constants.SUM_BINARY, Constants.DIV_BINARY,
                        Constants.MUL_BINARY, Constants.POW_BINARY, Constants.SUB_BINARY,
                        Constants.DIVINT_BINARY,Constants.AND_BINARY,Constants.OR_BINARY,Constants.EQ_BINARY,
                        Constants.GE_BINARY,Constants.LE_BINARY, Constants.GT_BINARY, Constants.LT_BINARY,
                        Constants.NOTEQ_BINARY1, Constants.NOTEQ_BINARY2, Constants.STRCAT_BINARY -> {

                    e.e1.accept(this);
                    e.e2.accept(this);

                    Type opType = TypeSystem.getTypeForBinaryOp(e.e1, e.operation, e.e2);
                    if (opType == null)
                        throw new ExprTypeException(e.e1, e.operation, e.e2);
                    e.setOpType(opType);
                }
            }
        }
        //Call Fun
        else if (e.callFun != null) {
            e.callFun.accept(this);
            e.setOpType(e.callFun.callFunType);
        }
        //Id
        else if (e.id != null) {
            ScopingItem item = scopingTable.lookup(e.id.name);
            e.id.accept(this);
            e.setOpType(e.id.idType);
        } else //Expr di tipi costanti
            if (!(e.type.equals("bool") || e.type.equals("integer") || e.type.equals("real") || e.type.equals("string")))
                new RuntimeException("Expr non corretta");//ERRORE in expr

        return null;
    }

    @Override
    public Object visit(Identifier i) {
        ScopingItem scopingItem = scopingTable.lookup(i.name);
        if (scopingItem == null || scopingItem.getIdName() == null)//Variabile usata ma non dichiarata
            throw new VariableNotDeclaredException(i.name);

        i.setIDType(scopingItem.getVariableType());

        return null;
    }


    @Override
    public Object visit(Const c) {
        return null;
    }

    @Override
    public Object visit(Bool b) {
        return null;
    }

    @Override
    public Object visit(IntConst i) {
        return null;
    }

    @Override
    public Object visit(RealConst r) {
        return null;
    }

    @Override
    public Object visit(ReturnStat r) {
        ScopingItem scopingItem = scopingTable.lookup(r.functionIdName);
        if(scopingItem.getReturnType() == null)
            throw new ReturnNotDefinedException(r.functionIdName);

        if(r.toReturn != null)
            r.toReturn.accept(this);

        if(!scopingItem.getReturnType().typeName.equals(r.toReturn.type.typeName))
            throw new ExprTypeException(scopingItem.getReturnType(),r.toReturn.type);

        if(r.toReturn.id != null) {
            if (scopingTable.lookup(r.toReturn.id.name) == null)
                throw new VariableNotDeclaredException(r.toReturn.id.name);
        }

        return null;
    }

    @Override
    public Object visit(StringConst a) {
        return null;
    }


    @Override
    public Object visit(IdList i) {

        Identifier id = i.head;
        while(id != null){
            id.accept(this);
            id = (Identifier) id.next;
        }

        return null;
    }

    @Override
    public Object visit(IfStat i) {
        scopingTable.enterScope("Tabella If ");
        i.expr.accept(this);

        if(!i.expr.type.typeName.equals("bool"))//Controllo se la condizione dell'if è un bool
            throw new ConditionNotBoolExeption("IF", i.expr.type);

        if (i.varDeclList != null) {
            i.varDeclList.accept(this);
        }
        if (i.statList != null) {
            i.statList.functionIdName = i.functionIdName;
            i.statList.accept(this);
        }
        printScopingTable(scopingTable);
        scopingTable.exitScope();
        if (i.anElse != null) {
            i.anElse.accept(this);
        }



        return null;
    }

    @Override
    public Object visit(NonEmptyParamDeclList n) {
        ParDecl current = n.head;
        while (current != null) {
            current.accept(this);
            current = current.next;
            n.numberOfParams++;
        }

        return null;
    }

    @Override
    public Object visit(ParamDeclList p) {
        NonEmptyParamDeclList nepdl = p.nepdl;
        nepdl.accept(this);

        return null;
    }

    @Override
    public Object visit(ParDecl p) {
        boolean isAlreadyDeclared = scopingTable.probe(p.id.name);

        if (isAlreadyDeclared)
            throw new IdDeclaredException(p.id.name);

        if(p.inOut != null)
            p.inOut.accept(this);
        p.type.accept(this);
        //p.id.accept(this);


        scopingTable.addId(new ScopingItem(p.id.name, p.type));
        return null;
    }

    @Override
    public Object visit(ReadStat r) {
        r.idList.accept(this);
        if(r.expr != null)
            r.expr.accept(this);
        return null;
    }


    @Override
    public Object visit(Stat a) {

        switch (a.current.getStatType()) {

            case "WhileStat":
                WhileStat wh = (WhileStat) a.current;
                wh.functionIdName = a.functionIdName;
                wh.accept(this);
                break;
            case "WriteStat":
                WriteStat wr = (WriteStat) a.current;
                wr.functionIdName = a.functionIdName;
                wr.accept(this);
                break;
            case "ReadStat":
                ReadStat r = (ReadStat) a.current;
                r.functionIdName = a.functionIdName;
                r.accept(this);
                break;
            case "AssignStat":
                AssignStat as = (AssignStat) a.current;
                as.functionIdName = a.functionIdName;
                as.accept(this);
                break;
            case "IfStat":
                IfStat i = (IfStat) a.current;
                i.functionIdName = a.functionIdName;
                i.accept(this);
                break;
            case "CallFun":
                CallFun c = (CallFun) a.current;
                c.functionIdName = a.functionIdName;
                c.accept(this);
                break;
            case "ReturnStat":
                ReturnStat e = (ReturnStat) a.current;
                e.functionIdName = a.functionIdName;
                e.accept(this);
                break;

        }

        return null;
    }

    @Override
    public Object visit(StatList s) {
        Stat current = s.head;
        while (current != null) {
            current.functionIdName = s.functionIdName;
            current.accept(this);
            current = current.next;
        }
        return null;
    }


    @Override
    public Object visit(WhileStat w) {
        scopingTable.enterScope("Tabella While ");


        w.expr.accept(this);

        if(!w.expr.type.typeName.equals("bool"))//Controllo se la condizione è bool
            throw new ConditionNotBoolExeption("WHILE", w.expr.type);


        if (w.varDeclList != null) {
            w.varDeclList.accept(this);
        }
        if (w.statList != null) {
            w.statList.accept(this);
        }


        printScopingTable(scopingTable);
        scopingTable.exitScope();
        return null;
    }

    @Override
    public Object visit(WriteStat a) {
        a.expr.accept(this);
        return null;
    }

    @Override
    public Object visit(SyntaxNode a) {
        return null;
    }

    @Override
    public Object visit(Type a) {
        return null;
    }

    @Override
    public Object visit(Var a) {
        return null;
    }

    @Override
    public Object visit(InOut i) {
        return null;
    }


    public void printScopingTable(ScopingTable scopingTable) {

        if(false) {
            //System.out.println(scopingTable.activeScope.getScopeName());
            ScopingNode it = scopingTable.activeScope;
            StringBuilder name = new StringBuilder();
            while (it != null) {
                name.append(" -> ").append(it.getScopeName());
                it = it.getFather();
            }

            System.out.println(name);
            scopingTable.activeScope.entrySet().forEach(
                    (a) -> {
                        System.out.println(a.getKey() + " " + a.getValue());
                    });
            System.out.println("");
        }
    }



}


