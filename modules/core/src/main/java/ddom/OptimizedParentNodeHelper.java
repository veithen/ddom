package ddom;

public class OptimizedParentNodeHelper {
    public static ChildNode getFirstChild(OptimizedParentNode node) {
        Object content = node.getContent();
        ChildNode firstChild;
        if (content instanceof String) {
            DocumentImpl document = node.getDocument();
            firstChild = document.getNodeFactory().createText(document, (String)content);
            // TODO: need to set parent
            node.internalSetFirstChild(firstChild);
        } else {
            firstChild = (ChildNode)content;
        }
        return firstChild;
    }
}
