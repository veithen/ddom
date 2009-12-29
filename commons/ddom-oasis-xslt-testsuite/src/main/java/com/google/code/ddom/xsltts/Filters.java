/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.ddom.xsltts;

import com.google.code.ddom.collections.ExclusionFilter;
import com.google.code.ddom.collections.Filter;

public class Filters {
    private Filters() {}
    
    public static final Filter<? super XSLTConformanceTest> XALAN_2_7_1_FILTER = new ExclusionFilter<String>(new String[] {
            "BVTs_bvt077", // Strange issue with xsl:include
            
            // NullPointerExceptions deep inside Xalan:
            "Namespace-alias__91781",
            "Namespace-alias_Namespace-Alias_NSAliasForDefaultWithExcludeResPref",
            "Namespace-alias_Namespace-Alias_Test1",
            "Namespace-alias_Namespace-Alias_Test2",
            
            // Xalan seems to have a problem with "#default" in xsl:namespace-alias and extension-element-prefixes
            "Namespace-alias__91782",
            "Stylesheet__91802",
            "Stylesheet__91803",
            "Stylesheet__91804",
            "Stylesheet__91806",
            "Stylesheet__91807",
            "Stylesheet__91808",
            "Stylesheet__91810",
            "Stylesheet__91811",
            "Stylesheet__91816",
    });
}
