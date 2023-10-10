package Nodi;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class SyntaxNode extends DefaultMutableTreeNode {

//    public Type nodeType;
    public SyntaxNode (){
        super();
    }
    public SyntaxNode(Object obj) {
            super(obj);
    }
    public String functionIdName;
//    public Type getNodeType() {
//        return nodeType;
//    }
//
//    public void setNodeType(Type nodeType) {
//        this.nodeType = nodeType;
//    }

    abstract public Object accept(Visitor v);
}
