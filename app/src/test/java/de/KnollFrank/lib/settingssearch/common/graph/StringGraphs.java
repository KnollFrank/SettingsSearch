package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

class StringGraphs {

    public static GraphBuilder<StringVertex, StringEdge, ?> newGraphBuilder() {
        return DefaultDirectedGraph.createBuilder(StringEdge.class);
    }

    public static StringEdge cloneEdge(final StringEdge edge) {
        return new StringEdge(edge.getLabel());
    }

    public static DefaultDirectedGraph<StringVertex, StringEdge> createEmptyGraph() {
        return new DefaultDirectedGraph<>(StringEdge.class);
    }
}
