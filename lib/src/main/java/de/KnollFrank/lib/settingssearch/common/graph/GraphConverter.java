package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GraphConverter<Node, Edge, EdgeValue> {

    private final ToJGraphTConverter<Node, Edge, EdgeValue> toJGraphTConverter;
    private final ToGuavaGraphConverter<Node, Edge, EdgeValue> toGuavaGraphConverter;

    public GraphConverter(final ToJGraphTConverter<Node, Edge, EdgeValue> toJGraphTConverter,
                          final ToGuavaGraphConverter<Node, Edge, EdgeValue> toGuavaGraphConverter) {
        this.toJGraphTConverter = toJGraphTConverter;
        this.toGuavaGraphConverter = toGuavaGraphConverter;
    }

    public AsUnmodifiableGraph<Node, Edge> toJGraphT(final ValueGraph<Node, EdgeValue> guavaGraph) {
        return toJGraphTConverter.toJGraphT(guavaGraph);
    }

    public ImmutableValueGraph<Node, EdgeValue> toGuava(final Graph<Node, Edge> jgraphtGraph) {
        return toGuavaGraphConverter.toGuava(jgraphtGraph);
    }
}
