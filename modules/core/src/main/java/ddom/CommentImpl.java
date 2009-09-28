package ddom;

import org.w3c.dom.Comment;
import org.w3c.dom.Node;

public class CommentImpl extends CharacterDataImpl implements Comment {
    public CommentImpl(DocumentImpl document, String data) {
        super(document, data);
    }

    public final short getNodeType() {
        return COMMENT_NODE;
    }

    public final String getNodeName() {
        return "#comment";
    }

    public final Node cloneNode(boolean deep) {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createComment(document, getData());
    }

    public final CharSequence collectTextContent(CharSequence appendTo) {
        return appendTo;
    }
}
