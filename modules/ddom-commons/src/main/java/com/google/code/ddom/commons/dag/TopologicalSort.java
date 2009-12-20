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
package com.google.code.ddom.commons.dag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements topological sorting.
 * 
 * @author Andreas Veithen
 */
public class TopologicalSort {
    private TopologicalSort() {}
    
    private static <T> void visit(Collection<T> vertices, EdgeRelation<T> edgeRelation, List<T> result, Set<T> visited, T vertex) {
        if (visited.add(vertex)) {
            for (T vertex2 : vertices) {
                if (vertex2 != vertex && edgeRelation.isEdge(vertex, vertex2)) {
                    visit(vertices, edgeRelation, result, visited, vertex2);
                }
            }
            result.add(vertex);
        }
    }
    
    /**
     * Apply topological ordering to a given collection.
     * 
     * @param <T> the vertex type
     * @param vertices the vertices of the directed acyclic graph
     * @param edgeRelation the relation defining the edges of the graph
     * @return the topologically sorted list of vertices, where the vertices with no incoming edges come first
     */
    public static <T> List<T> sort(Collection<T> vertices, EdgeRelation<T> edgeRelation) {
        List<T> result = new ArrayList<T>(vertices.size());
        Set<T> visited = new HashSet<T>();
        for (T vertex : vertices) {
            visit(vertices, edgeRelation, result, visited, vertex);
        }
        return result;
    }
}
