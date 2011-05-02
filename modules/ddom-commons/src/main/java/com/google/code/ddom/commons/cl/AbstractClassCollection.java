/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.google.code.ddom.commons.cl;

public abstract class AbstractClassCollection implements ClassCollection {
    public boolean isInPackage(String pkg) {
        for (ClassRef classRef : getClassRefs()) {
            if (!isInPackage(classRef.getClassName(), pkg)) {
                return false;
            }
        }
        return true;
    }

    public String getRootPackageName() {
        String pkg = null;
        for (ClassRef classRef : getClassRefs()) {
            String className = classRef.getClassName();
            if (pkg == null) {
                pkg = className.substring(0, className.lastIndexOf('.'));
            } else {
                while (!isInPackage(className, pkg)) {
                    int idx = pkg.lastIndexOf('.');
                    if (idx == -1) {
                        return "";
                    }
                    pkg = pkg.substring(0, idx);
                }
            }
        }
        return pkg;
    }
    
    private static boolean isInPackage(String className, String pkg) {
        return pkg.length() < className.length() && className.startsWith(pkg)
                && className.charAt(pkg.length()) == '.';
    }
}
