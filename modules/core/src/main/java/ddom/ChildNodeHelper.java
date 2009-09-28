package ddom;

public class ChildNodeHelper {
    public static ChildNode getNextSibling(ChildNode node) {
        ParentNode parent = node.getParentNode();
        DocumentImpl document = node.getDocument();
        if (parent instanceof BuilderTarget) {
            ChildNode nextSibling;
            while ((nextSibling = node.internalGetNextSibling()) == null && !((BuilderTarget)parent).isComplete()) {
                document.next();
            }
            return nextSibling;
        } else {
            return node.internalGetNextSibling();
        }
    }
    
    public static ChildNode getPreviousSibling(ChildNode node) {
        ParentNode parent = node.getParentNode();
        if (parent == null) {
            return null;
        } else {
            ChildNode previousSibling = null;
            ChildNode sibling = parent.getFirstChild();
            while (sibling != null && sibling != node) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSibling();
            }
            return sibling == null ? null : previousSibling;
        }
    }
}
