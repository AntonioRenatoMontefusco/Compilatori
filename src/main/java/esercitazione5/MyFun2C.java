package esercitazione5;

import Nodi.Program;
import cTranspiler.CTranspilerVisitor;
import scopingTable.ScopingTable;
import scopingTable.SemanticAnalyzer;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;


public class MyFun2C {
    public static void main(String[] args) {
        File infile = new File(args[0]);
        String fileName = infile.getName();

//        String ext = fileName.split("\\.")[fileName.split("\\.").length - 1];
//        if(!ext.equals("fun")) {
//            System.err.println("You must specify a .fun file as argument!");
//            return;
//        }

        String dirPath = "test_files/c_out";
        String name = fileName.split("\\.")[0];
        String cName = name + ".c";

        try {
            Lexer lexer = new Lexer(new FileReader(infile));
            parser p = new parser(lexer);

            Program astRoot = (Program) p.parse().value;

            ScopingTable scopingTable = new ScopingTable();
            SemanticAnalyzer scopingVisitor = new SemanticAnalyzer(scopingTable);
            astRoot.accept(scopingVisitor);

            CTranspilerVisitor transpilerVisitor = new CTranspilerVisitor();
            astRoot.accept(transpilerVisitor);

            transpilerVisitor.printToFile(Path.of(dirPath, cName).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
