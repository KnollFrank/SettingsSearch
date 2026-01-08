package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ImmutableValueGraph;

import org.jgrapht.graph.AsUnmodifiableGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GraphConverter<V, E, W> {

    private final ToJGraphTConverter<V, E, W> toJGraphTConverter;
    private final ToGuavaGraphConverter<V, E, W> toGuavaGraphConverter;

    public GraphConverter(final ToJGraphTConverter<V, E, W> toJGraphTConverter,
                          final ToGuavaGraphConverter<V, E, W> toGuavaGraphConverter) {
        this.toJGraphTConverter = toJGraphTConverter;
        this.toGuavaGraphConverter = toGuavaGraphConverter;
    }

    public AsUnmodifiableGraph<V, E> toJGraphT(final ImmutableValueGraph<V, W> guavaGraph) {
        return toJGraphTConverter.toJGraphT(guavaGraph);
    }

    public ImmutableValueGraph<V, W> toGuava(final AsUnmodifiableGraph<V, E> jgraphtGraph) {
        return toGuavaGraphConverter.toGuava(jgraphtGraph);
    }
}
