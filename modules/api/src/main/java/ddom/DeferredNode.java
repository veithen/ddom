package ddom;

import org.w3c.dom.Node;

// TODO: need to specify which nodes implement this interface (e.g. attributes??)
public interface DeferredNode extends Node {
    boolean isComplete();
    
    void build();
}
