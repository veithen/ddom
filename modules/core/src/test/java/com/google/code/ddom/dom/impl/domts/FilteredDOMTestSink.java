package com.google.code.ddom.dom.impl.domts;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.w3c.domts.DOMTestSink;

public class FilteredDOMTestSink implements DOMTestSink {
    private final DOMTestSink parent;
    private final Set<Class> excludes;
    private final List<Pattern> excludePatterns;

    public FilteredDOMTestSink(DOMTestSink parent, Set<Class> excludes, List<Pattern> excludePatterns) {
        this.parent = parent;
        this.excludes = excludes;
        this.excludePatterns = excludePatterns;
    }

    public void addTest(Class test) {
        if (excludes.contains(test)) {
            return;
        }
        String name = test.getSimpleName();
        for (Pattern pattern : excludePatterns) {
            if (pattern.matcher(name).matches()) {
                return;
            }
        }
        parent.addTest(test);
    }
}
