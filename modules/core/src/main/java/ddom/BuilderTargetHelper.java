package ddom;

public class BuilderTargetHelper {
    public static void build(BuilderTarget node) {
        DocumentImpl document = node.getDocument();
        while (!node.isComplete()) {
            document.next();
        }
    }
}
