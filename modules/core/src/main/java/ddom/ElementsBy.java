package ddom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ElementsBy implements NodeList {
    private final DocumentImpl document;
    private Iterator<? extends Node> iterator;
    private final List<Node> nodes = new ArrayList<Node>();
    private boolean complete;
    private int structureVersion;
    
    public ElementsBy(DocumentImpl document) {
        this.document = document;
    }
    
    protected abstract Iterator<? extends Node> createIterator();

    private void init() {
        int currentStructureVersion = document.getStructureVersion();
        if (iterator == null && !complete || structureVersion != currentStructureVersion) {
            iterator = createIterator();
            nodes.clear();
            complete = false;
            structureVersion = currentStructureVersion;
        }
    }
    
    public int getLength() {
        init();
        if (!complete) {
            while (iterator.hasNext()) {
                nodes.add(iterator.next());
            }
            iterator = null;
            complete = true;
        }
        return nodes.size();
    }

    public Node item(int index) {
        init();
        if (index < 0) {
            return null;
        } else if (index < nodes.size()) {
            return nodes.get(index);
        } else if (!complete) {
            Node node = null;
            while (index >= nodes.size()) {
                if (iterator.hasNext()) {
                    node = iterator.next();
                    nodes.add(node);
                } else {
                    iterator = null;
                    complete = true;
                    return null;
                }
            }
            return node;
        } else {
            return null;
        }
    }
}
