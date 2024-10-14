package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;

import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;

public class NodesTransformer {

    public static <V, W> Graph<W, PreferenceEdge> transformNodes(
            final Graph<V, PreferenceEdge> graph,
            final Function<V, W> transformNode) {
        return GraphTransformerAlgorithm.transform(
                graph,
                PreferenceEdge.class,
                transformNodes(transformNode));
    }

    private static <V, W> GraphTransformer<V, PreferenceEdge, W, PreferenceEdge> transformNodes(
            final Function<V, W> transformNode) {
        return new GraphTransformer<>() {

            @Override
            public W transformNode(final V v) {
                return transformNode.apply(v);
            }

            @Override
            public PreferenceEdge transformEdge(final PreferenceEdge edge, final W w) {
                return new PreferenceEdge(edge.preference);
            }
        };
    }
}
