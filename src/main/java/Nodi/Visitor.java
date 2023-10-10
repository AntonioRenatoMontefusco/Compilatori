package Nodi;

public interface Visitor {

    Object visit (AssignStat a);

    Object visit (Bool b);

    Object visit (CallFun c);

    Object visit (Const c);

    Object visit (Else e);

    Object visit (Expr e);

    Object visit (ExprList e);

    Object visit (Fun f);

    Object visit (FunList f);

    Object visit (Identifier i);

    Object visit (IdList i);

    Object visit (IdListInit i);

    Object visit (IdListInitObbl i);

    Object visit (IfStat i);

    Object visit (InOut i);

    Object visit (IntConst i);

    Object visit (Main m);

    Object visit (NonEmptyParamDeclList n);

    Object visit (ParamDeclList p);

    Object visit (ParDecl p);

    Object visit (Program p);

    Object visit (ReadStat r);

    Object visit (RealConst r);

    Object visit (ReturnStat r);

    Object visit (Stat a);

    Object visit (StatList a);

    Object visit (StringConst a);

    Object visit (SyntaxNode a);

    Object visit (Type a);

    Object visit (Var a);

    Object visit (VarDecl a);

    Object visit (VarDeclList a);

    Object visit (WhileStat a);

    Object visit (WriteStat a);

}