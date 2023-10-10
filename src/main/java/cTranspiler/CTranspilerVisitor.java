package cTranspiler;

import Nodi.*;
import typeSystem.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CTranspilerVisitor implements Visitor {
    private StringBuffer program;
    private int currentIdIndex;
    private Map<String, String> idMap;
    private final int bufferSize = 256;
    private int currentStringIndex;
    private Map<String, String> functionBeforeReturnOps;
    private static final String strConcatName = "str_concat";
    private static final String intToStringName = "int_to_string";
    private static final String realToStringName = "real_to_string";
    private static final String boolToStringName = "bool_to_string";

    public CTranspilerVisitor() {
        this.program = new StringBuffer();
        this.idMap = new HashMap<>();
        this.currentIdIndex = 0;
        this.currentStringIndex = 0;
        this.functionBeforeReturnOps = new HashMap<>();
    }

    public void printToFile(String path) throws IOException {
        BufferedWriter bwr = new BufferedWriter(new FileWriter(path));
        bwr.write(program.toString());
        bwr.flush();
        bwr.close();
    }

    @Override
    public Object visit(Program p) {
        StringBuffer programBuff = new StringBuffer();
        if (p.varDeclList != null) {
            String vdl = (String) p.varDeclList.accept(this);
            programBuff.append(vdl);
        }
        if (p.funList != null) {
            String funList = (String) p.funList.accept(this);
            programBuff.append(funList);
        }
        String main = (String) p.main.accept(this);

        programBuff.append(main);

        program.append(customOps());
        program.append(programBuff);
        String libs = "#include <stdio.h>\n#include <string.h>\n#include <math.h>\n";
        program.insert(0, libs);


        return null;
    }


    @Override
    public Object visit(Main m) {
        StringBuffer mainBuff = new StringBuffer();

        mainBuff.append("int main() {\n");
        //mainBuff.append("int main() {\n" + "body" + "return 0;\n}");
        if (m.varDeclList != null) {
            String vdl = (String) m.varDeclList.accept(this);
            mainBuff.append(vdl);
        }
        if (m.statList != null) {
            String sl = (String) m.statList.accept(this);
            mainBuff.append(sl);
        }
        mainBuff.append("\nreturn 0;\n}");


        return mainBuff.toString();
    }

    @Override
    public Object visit(VarDeclList vdl) {
        StringBuffer vdlBuff = new StringBuffer();

        VarDecl vd = vdl.head;
        while (vd != null) {
            String decl = (String) vd.accept(this);
            vdlBuff.append(decl);
            vd = vd.next;
        }
        return vdlBuff.toString();
    }

    @Override
    public Object visit(VarDecl vd) {
        StringBuffer vdBuff = new StringBuffer();
        //Controlla se è di tipo VAR
        if (vd != null && vd.type.typeName.equals("VAR")) {
            IdListInitObbl ob = vd.listObbl;
            String decl = (String) ob.accept(this);
            vdBuff.append(decl);
        } else {  //Ho una IdListInit, quindi itero sulla lista e metto nella tabella gli identificatori dichiarati
            IdListInit idl = vd.listInit;
            idl.type = vd.type;//Mi porto il tipo
            String decl = (String) idl.accept(this);
            vdBuff.append(decl);
        }
        return vdBuff.toString();
    }

    @Override
    public Object visit(IdListInitObbl i) {
        IdInit head = i.head;
        String idListInitObblBuff = "";

        while (head != null) {
            if (head.type.typeName.equals("string")) {
                String string = createNewString();
                idMap.put(head.id.name, string);
                String init = "";
                if (head.expr != null) {
                    String expr = (String) head.expr.accept(this);
                    init = "strcpy(" + string + "," + expr + ");\n";
                } else if (head.aConst != null) {
                    String con = (String) head.aConst.accept(this);
                    init = "strcpy(" + string + "," + con + ");\n";
                }
                idListInitObblBuff += init;
            } else {
                String init = "";
                if (head.aConst != null) {
//                    String expr = (String) head.expr.accept(this);
                    String value = String.valueOf(head.aConst.accept(this));
                    init = "=" + value;
                }
                idListInitObblBuff += getDeclString(head.type, head.id.name) + init + ";\n";
            }
            head = head.next;
        }
        return idListInitObblBuff;
    }

    @Override
    public Object visit(IdListInit i) {

        IdInit head = i.head;
        String idListInitBuffer = "";
        while (head != null) {
            if (head.type.typeName.equals("string")) {
                String string = createNewString();
                idMap.put(head.id.name, string);
                String init = "";
                if (head.expr != null) {
                    String expr = (String) head.expr.accept(this);
                    init = "strcpy(" + string + "," + expr + ");\n";
                }
                idListInitBuffer += init;
            } else {
                String init = "";
                if (head.expr != null) {
                    String expr = String.valueOf(head.expr.accept(this));
                    init = "=" + expr;
                }
                idListInitBuffer += getDeclString(head.type, head.id.name) + init + ";\n";
            }

            head = head.next;

        }
        return idListInitBuffer;
    }

    @Override
    public Object visit(FunList f) {
        StringBuffer fLBuff = new StringBuffer();

        if (f != null) {
            Fun fun = f.head;
            while (fun != null) {
                String res = (String) fun.accept(this);
                fLBuff.append(res);
                fun = fun.next;
            }
        }
        return fLBuff.toString();
    }

    //TODO da finire
    @Override
    public Object visit(Fun f) {
        StringBuffer funBuff = new StringBuffer();
        if (f.type == null)
            funBuff.append("void " + getID(f.identifier.name) + "(");
        else
            funBuff.append(getType(f.type.typeName) + " " + getID(f.identifier.name) + "(");

        StringBuffer initOutPars = new StringBuffer();
        StringBuffer assignOutPars = new StringBuffer();
        ParamDeclList list = f.paramDeclList;
        //Parametri fun
        if (list != null) {
            ArrayList<String> parDecl = new ArrayList<>();
//          Gestione parametri out
            ParDecl p = list.nepdl.head;
            while (p != null) {
                parDecl.add((String) p.accept(this));
                if (p.inOut != null && !p.type.equals(new Type("string"))) {
                    initOutPars.append(getDeclString(p.type, p.id.name))
                            .append("=*").append(getID(p.id.name)).append("_out;\n");
                    assignOutPars.append("*").append(getID(p.id.name)).append("_out")
                            .append("=").append(getID(p.id.name)).append(";\n");
                }
                p = p.next;
            }
            funBuff.append(String.join(", ", parDecl));
        }

        functionBeforeReturnOps.put(f.identifier.name, assignOutPars.toString());

        String varDecl = "";
        if (f.varDeclList != null)
            varDecl = (String) f.varDeclList.accept(this);

        String statList = "";

        if (f.statList != null)
            statList = (String) f.statList.accept(this);

        funBuff.append(") {\n")
                .append(initOutPars)
                .append(varDecl)
                .append(statList)
                .append(functionBeforeReturnOps.get(f.identifier.name) != null ? functionBeforeReturnOps.get(f.identifier.name) : "")
                .append("}\n");
        return funBuff.toString();
    }

    @Override
    public Object visit(Bool b) {
        if (b.value)
            return "1";
        else
            return "0";
    }

    @Override
    public Object visit(CallFun c) {
        StringBuffer callFunBuff = new StringBuffer();
        callFunBuff.append(getID(c.identifier.name)).append("(");
        if (c.exprList != null) {
            String res = (String) c.exprList.accept(this);
            callFunBuff.append(res);
        }

        callFunBuff.append(")");

        return callFunBuff.toString();
    }

    @Override
    public Object visit(Const c) {
        //StringBuffer constBuff = new StringBuffer();
        switch (c.type.typeName) {
            case "integer":
                return c.integer.accept(this);
            case "real":
                return c.real.accept(this);
            case "bool":
                return c.bool.accept(this);
            case "string":
                return c.string.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Expr e) {
        String op = e.operation;

        if (op != null) {
            switch (op) {
                case Constants.NOT_UNARY -> {
                    String expr = (String) e.e1.accept(this);
                    return "(! " + expr + ")";
                }
                case Constants.MINUS_UNARY -> {
                    String expr = (String) e.e1.accept(this);
                    return "(- " + expr + ")";
                }

                case Constants.SUM_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + first + " + " + second + ")";

                }
                case Constants.DIV_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + first + " / " + second + ")";

                }
                case Constants.MUL_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + first + " * " + second + ")";

                }
                case Constants.POW_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + "pow(" + first + "," + second + "))";

                }
                case Constants.SUB_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + first + " - " + second + ")";

                }
                case Constants.DIVINT_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "((int)" + first + " / " + second + ")";

                }
                case Constants.AND_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + first + "&&" + second + ")";

                }
                case Constants.OR_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    return "(" + first + "||" + second + ")";

                }
                case Constants.EQ_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    if (e.e1.type.typeName.equals("string") && e.e2.type.typeName.equals("string"))
                        return "(strcmp(" + first + ", " + second + ") == 0)";
                    return "(" + first + "==" + second + ")";

                }
                case Constants.GE_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    if (e.e1.type.typeName.equals("string") && e.e2.type.typeName.equals("string"))
                        return "(strcmp(" + first + ", " + second + ") >= 0)";
                    return "(" + first + ">=" + second + ")";

                }
                case Constants.LE_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    if (e.e1.type.typeName.equals("string") && e.e2.type.typeName.equals("string"))
                        return "(strcmp(" + first + ", " + second + ") <= 0)";
                    return "(" + first + "<=" + second + ")";

                }
                case Constants.GT_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    if (e.e1.type.typeName.equals("string") && e.e2.type.typeName.equals("string"))
                        return "(strcmp(" + first + ", " + second + ") > 0)";
                    return "(" + first + ">" + second + ")";

                }
                case Constants.LT_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    if (e.e1.type.typeName.equals("string") && e.e2.type.typeName.equals("string"))
                        return "(strcmp(" + first + ", " + second + ") < 0)";
                    return "(" + first + "<" + second + ")";

                }
                case Constants.NOTEQ_BINARY1, Constants.NOTEQ_BINARY2 -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);
                    if (e.e1.type.typeName.equals("string") && e.e2.type.typeName.equals("string"))
                        return "(strcmp(" + first + ", " + second + ") != 0)";
                    return "(" + first + "" + second + ")";
                }
                case Constants.STRCAT_BINARY -> {
                    String first = (String) e.e1.accept(this);
                    String second = (String) e.e2.accept(this);

                    String s1 = exprToString(first, e.e1.type);
                    String s2 = exprToString(second, e.e2.type);

                    String newString = createNewString();

                    return "str_concat(" + s1 + "," + s2 + "," + newString + ")";
                }
            }
        }

        //Call Fun
        else if (e.callFun != null) {
            return (String) e.callFun.accept(this);
        }
        //Id
        else if (e.id != null) {
            return (String) e.id.accept(this);
        } else { //Expr di tipi costanti
            switch (e.type.typeName) {
                case "integer" -> {
                    return e.aConst.integer.accept(this);
                }
                case "bool" -> {
                    return e.aConst.bool.accept(this);
                }
                case "real" -> {
                    return e.aConst.real.accept(this);
                }
                case "string" -> {
                    return e.aConst.string.accept(this);
                }
            }
        }

        return null;
    }

    @Override
    public Object visit(ExprList e) {
        Scrollable current = e.head;
        StringBuffer exprListBuffer = new StringBuffer();
        while (current != null) {
            if (current instanceof Expr) {
                String expr = String.valueOf(((Expr) current).accept(this));
                exprListBuffer.append(expr);
            } else if (current instanceof Identifier) {
                String identfier = (String) ((Identifier) current).accept(this);
                exprListBuffer.append(identfier);
            }
            if (current.getNext() != null)
                exprListBuffer.append(",");
            current = current.getNext();
            e.numberOfParams++;
        }

        return exprListBuffer.toString();
    }


    @Override
    public Object visit(StatList sl) {
        StringBuffer slBuff = new StringBuffer();

        Stat s = sl.head;
        while (s != null) {
            String decl = (String) s.accept(this);
            slBuff.append(decl);
            s = s.next;
        }
        return slBuff.toString();
    }

    @Override
    public Object visit(Stat a) {
        StringBuffer statBuff = new StringBuffer();
        String res = "";
        switch (a.current.getStatType()) {
            case "WhileStat":
                WhileStat wh = (WhileStat) a.current;
                res = (String) wh.accept(this);
                break;
            case "WriteStat":
                WriteStat wr = (WriteStat) a.current;
                res = (String) wr.accept(this);
                break;
            case "ReadStat":
                ReadStat r = (ReadStat) a.current;
                res = (String) r.accept(this);
                break;
            case "AssignStat":
                AssignStat as = (AssignStat) a.current;
                res = (String) as.accept(this);
                break;
            case "IfStat":
                IfStat i = (IfStat) a.current;
                res = (String) i.accept(this);
                break;
            case "CallFun":
                CallFun c = (CallFun) a.current;
                res = c.accept(this) + ";\n";
                break;
            case "ReturnStat":
                ReturnStat e = (ReturnStat) a.current;
                res = (String) e.accept(this);
                break;
        }
        statBuff.append(res);
        return statBuff.toString();
    }

    @Override
    public Object visit(WhileStat a) {
        StringBuffer whileBuff = new StringBuffer();
        String vdList, statList;

        whileBuff.append("while(");
        String cond = (String) a.expr.accept(this);
        whileBuff.append(cond).append(") {\n");
        if (a.varDeclList != null) {
            vdList = (String) a.varDeclList.accept(this);
            whileBuff.append(vdList);
        }
        if (a.statList != null) {
            statList = (String) a.statList.accept(this);
            whileBuff.append(statList);
        }

        whileBuff.append("\n}\n");

        return whileBuff.toString();
    }

    @Override
    public Object visit(IfStat i) {
        StringBuffer ifBuff = new StringBuffer();
        String vdList, statList, anElse;

        ifBuff.append("if(");
        String cond = (String) i.expr.accept(this);
        ifBuff.append(cond + ") {\n");
        if (i.varDeclList != null) {
            vdList = (String) i.varDeclList.accept(this);
            ifBuff.append(vdList);
        }
        if (i.statList != null) {
            statList = (String) i.statList.accept(this);
            ifBuff.append(statList);
        }
        ifBuff.append("}");
        if (i.anElse != null) {
            anElse = (String) i.anElse.accept(this);
            ifBuff.append(anElse);
        }
        return ifBuff.toString();
    }

    @Override
    public Object visit(Else e) {
        StringBuffer elseBuff = new StringBuffer();
        String vdList, statList;

        elseBuff.append("else {\n");
        if (e.varDeclList != null) {
            vdList = (String) e.varDeclList.accept(this);
            elseBuff.append(vdList);
        }
        if (e.statList != null) {
            statList = (String) e.statList.accept(this);
            elseBuff.append(statList);
        }
        elseBuff.append("}");
        return elseBuff.toString();
    }

    @Override
    public Object visit(WriteStat a) {
        StringBuffer writeBuff = new StringBuffer();
        String writeMode = switch (a.writeType) {
            case WRITE -> "";
            case WRITELN -> "\\r\\n";
            case WRITEB -> " ";
            case WRITET -> "\\t";
        };
        String expr = (String) a.expr.accept(this);

        String mode = switch (a.expr.type.typeName) {
            case "integer" -> "%ld";
            case "string" -> "%s";
            case "real" -> "%f";
            case "bool" -> "%d";
            default -> "%s";
        };

        writeBuff.append("printf(\"")
                .append(mode)
                .append(writeMode)
                .append("\",")
                .append(expr)
                .append(");\n");
        return writeBuff.toString();
    }

    @Override
    public Object visit(ReadStat r) {
        StringBuffer readBuff = new StringBuffer();



        if (r.expr != null) {
            String mode = switch (r.expr.type.typeName) {
                case "integer" -> "%ld";
                case "string" -> "%s";
                case "real" -> "%f";
                case "bool" -> "%d";
                default -> "%s";
            };


            String message = (String) r.expr.accept(this);
            readBuff.append("printf(\"")
                    .append(mode)
                    .append("\",")
                    .append(message)
                    .append(");\n");
        }

        String idl = (String) r.idList.accept(this);
        readBuff.append(idl);

        return readBuff.toString();
    }

    @Override
    public Object visit(AssignStat a) {
        String expr = String.valueOf(a.expr.accept(this));
        String id = (String) a.identifier.accept(this);
        a.identifier.setIDType(a.expr.type);
        if (a.expr.type.typeName.equals("string"))
            return "strcpy(" + id + ", " + expr + ");\n";

        return id + "=" + expr + ";\n";
    }


    @Override
    public Object visit(Identifier i) {
        String out = i.inOut == null ? "" : "&";
        return out + getID(i.name);
    }

    @Override
    public Object visit(IdList idL) {

        StringBuffer buffer = new StringBuffer();
        Identifier i = idL.head;
        while (i != null) {
            i.accept(this);
            buffer.append("scanf(\"")
                    .append(printfType(i.idType.typeName))
                    .append("\"");
            if (i.idType.typeName.equals("string"))
                buffer.append("," + getID(i.name));
            else
                buffer.append("," + "&" + getID(i.name));
            buffer.append(");\n");

            i = (Identifier) i.next;

        }
        return buffer.toString();
    }

    @Override
    public Object visit(InOut i) {
        return null;
    }

    @Override
    public Object visit(IntConst i) {
        return Integer.toString(i.value);
    }

    @Override
    public Object visit(ParamDeclList p) {
        if (p != null)
            return p.nepdl.accept(this);
        return null;
    }

    @Override
    public Object visit(NonEmptyParamDeclList n) {
        ArrayList<String> list = new ArrayList<>();
        ParDecl p = n.head;
        while (p != null) {
            list.add((String) p.accept(this));

            p = p.next;
        }
        return list;
    }

    @Override
    public Object visit(ParDecl p) {
        String mode = " ";
        String out = "";
        if (p.inOut != null) {

            //Da C out non è supportato, bisogna passare il puntatore
            if (!p.type.typeName.equals("string") && p.inOut.mode.equals("out")) {
                mode = " *"; //puntatore
                out = "_out"; //per creare una variabile temporanea
            }
        }
        return getType(p.type.typeName) + mode + getID(p.id.name) + out;
    }

    @Override
    public Object visit(RealConst r) {
        return Float.toString(r.value);
    }

    @Override
    public Object visit(ReturnStat returnStat) {

        String expr = (String) returnStat.toReturn.accept(this);
        String ops = "";
        if (functionBeforeReturnOps.containsKey(returnStat.functionIdName)) {
            ops = functionBeforeReturnOps.get(returnStat.functionIdName);
            functionBeforeReturnOps.remove(returnStat.functionIdName);
        }
        return ops + "return " + expr + ";\n";
    }

    @Override
    public Object visit(StringConst a) {
        return a.value;
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

    private String getID(String id) {
        if (idMap.containsKey(id))//TODO A che serve questo controllo? È una mappa globale potrei trovare più variabili con lo stesso nome ma con scope diverso
            return idMap.get(id);
        String newId = "id_" + currentIdIndex;
        currentIdIndex++;
        idMap.put(id, newId);
        return newId;
        //return id;
    }

    private String createNewString() {
        String id = "string_" + currentStringIndex;
        currentStringIndex++;
        program.append("char " + id + "[" + bufferSize + "];\n");
        return id;
    }

    private String getDeclString(Type t, String id) {
        return switch (t.typeName) {
            case "integer", "bool" -> "int " + getID(id);
            case "real" -> "float " + getID(id);
            case "string" -> "char " + getID(id) + "[" + bufferSize + "]";
            default -> throw new RuntimeException("Unexpected c type declaration");
        };
    }

    private String printfType(String t) {
        return switch (t) {
            case "integer", "bool" -> "%d";
            case "real" -> "%f";
            case "string" -> "%s";
            default -> throw new RuntimeException("Unexpected c type conversion");
        };
    }

    private String getType(String t) {
        //Usata in fun per trovare il tipo della funzione
        //Usata in VarDecl per trovare il tipo del parametro
        if (t == null) return "void";
        return switch (t) {
            case "integer", "bool" -> "int";
            case "real" -> "float";
            case "string" -> "char*";
            default -> throw new RuntimeException("Cannot get type for parDecl");
        };
    }

    private String customOps() {
        // return the content of the file custom_ops.c as string
        return "char* " + strConcatName + "(char *s1, char *s2, char *dest) {\n" +
                "    char _s1[" + bufferSize + "];\n" +
                "    char _s2[" + bufferSize + "];\n" +
                "    strcpy(_s1, s1);\n" +
                "    strcpy(_s2, s2);\n" +
                "    strcat(_s1, _s2);\n" +
                "    strcpy(dest, _s1);\n" +
                "    return dest;\n" +
                "}\n" +
                "\n" +
                "char* " + intToStringName + "(int a, char *dest) {\n" +
                "    sprintf(dest, \"%d\", a);\n" +
                "    return dest;\n" +
                "}\n" +
                "\n" +
                "char* " + realToStringName + "(float a, char *dest) {\n" +
                "    sprintf(dest, \"%f\", a);\n" +
                "    return dest;\n" +
                "}\n" +
                "\n" +
                "char* " + boolToStringName + "(int a) {\n" +
                "    if(a) return \"true\";\n" +
                "    return \"false\";\n" +
                "}";
    }

    private String exprToString(String expr, Type type) {
        String id;
        switch (type.typeName) {
            case "real":
                id = createNewString();
                return realToStringName + "(" + expr + "," + id + ")";
            case "integer":
                id = createNewString();
                return intToStringName + "(" + expr + "," + id + ")";
            case "bool":
                return boolToStringName + "(" + expr + ")";
            case "string":
                return expr;
            default:
                throw new RuntimeException("Unespected type to string conversion!");
        }
    }
}
