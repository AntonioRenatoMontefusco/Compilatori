package scopingTable;

import java.util.HashMap;

public class ScopingNode extends HashMap<String, ScopingItem> {
    private ScopingNode father;
    private String scopeName;

    public ScopingNode(ScopingNode father,String scopeName) {
        super();
        this.father = father;
        this.scopeName = scopeName;
    }

    public ScopingNode getFather() {
        return father;
    }

    public String getScopeName() {
        return scopeName;
    }
}
