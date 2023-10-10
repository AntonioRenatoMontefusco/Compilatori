package scopingTable;

public class ScopingTable {
    public ScopingNode activeScope = null;

    //start a new nested scope
    public void enterScope(String name) {
        ScopingNode table = new ScopingNode(activeScope,name);
        activeScope = table;
    }

    //exit current scope
    public void exitScope() {
        if(activeScope != null && activeScope.getFather() != null) {
            activeScope = activeScope.getFather();
        }
    }

    //finds current x (or null)
    public ScopingItem lookup(String idName) {
        ScopingNode currentLooking = activeScope;
        while(currentLooking != null) {
            if(currentLooking.containsKey(idName))
                return currentLooking.get(idName);
            currentLooking = currentLooking.getFather();
        }
        return null;
    }

    //true if x defined in current scope
    public boolean probe(String idName) {
        return activeScope.containsKey(idName);
    }

    //adds a symbol x to the table
    public void addId(ScopingItem item) {
        if(!activeScope.containsKey(item.getIdName())) {
            activeScope.put(item.getIdName(), item);
        }
    }
}
