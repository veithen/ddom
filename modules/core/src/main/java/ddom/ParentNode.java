package ddom;

public interface ParentNode extends AbstractNode {
    void internalSetFirstChild(ChildNode child);
    void notifyChildrenModified(int delta);
    ChildNode getFirstChild();
    ChildNode getLastChild();
}
