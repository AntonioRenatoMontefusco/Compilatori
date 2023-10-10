package esercitazione5;

import javax.swing.*;
import javax.swing.tree.MutableTreeNode;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TreeGenerator {
    public static void main(String[] args) throws Exception {
    try {
        esercitazione5.Lexer lexer = new esercitazione5.Lexer(new FileReader(args[0]));
        parser p = new parser(lexer);
        //p.parse();
        MutableTreeNode tree = (MutableTreeNode) p.parse().value;
        JTree jtree = new JTree(tree);
        expandAllNodes(jtree, 0, jtree.getRowCount());
        JFrame frame = new JFrame();
        frame.setTitle("Tree");
        JScrollPane pane = new JScrollPane(jtree);
        frame.add(pane);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private static void expandAllNodes(JTree tree, int startingIndex, int rowCount){
        for(int i=startingIndex;i<rowCount;++i){
            tree.expandRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }
}
