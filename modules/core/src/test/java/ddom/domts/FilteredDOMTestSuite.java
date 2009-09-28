package ddom.domts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestSink;
import org.w3c.domts.DOMTestSuite;

public class FilteredDOMTestSuite extends DOMTestSuite {
    private final DOMTestSuite parent;
    private final Set<Class> excludes = new HashSet<Class>();
    private final List<Pattern> excludePatterns = new ArrayList<Pattern>();
    
    public FilteredDOMTestSuite(DOMTestDocumentBuilderFactory factory, DOMTestSuite parent) {
        super(factory);
        this.parent = parent;
    }

    public void addExclude(Class exclude) {
        excludes.add(exclude);
    }
    
    public void addExclude(String pattern) {
        excludePatterns.add(Pattern.compile(pattern));
    }

    @Override
    public String getTargetURI() {
        return parent.getTargetURI();
    }

    @Override
    public void build(DOMTestSink sink) {
        parent.build(new FilteredDOMTestSink(sink, excludes, excludePatterns));
    }
}
