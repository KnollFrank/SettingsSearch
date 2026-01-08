package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphEquality.assertActualEqualsExpected;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.graph.AsUnmodifiableGraph;
import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GraphConvertersTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");

    // [A] --("Edge-AB")--> [B]
    private final AsUnmodifiableGraph<StringVertex, StringEdge> jGraphT_GRAPH =
            new AsUnmodifiableGraph<>(
                    StringGraphs
                            .newGraphBuilder()
                            .addVertex(vA)
                            .addVertex(vB)
                            .addEdge(vA, vB, new StringEdge("Edge-AB"))
                            .build());

    // [A] --("Edge-AB")--> [B]
    private final ImmutableValueGraph<StringVertex, String> GUAVA_GRAPH =
            ValueGraphBuilder
                    .directed()
                    .<StringVertex, String>immutable()
                    .addNode(vA)
                    .addNode(vB)
                    .putEdgeValue(vA, vB, "Edge-AB")
                    .build();

    @Test
    public void shouldConvertJGraphTToGuava() {
        // When
        final ImmutableValueGraph<StringVertex, String> guavaGraph =
                GraphConverters.toGuava(
                        jGraphT_GRAPH,
                        StringEdge::getLabel);

        // Then
        assertThat(guavaGraph, is(GUAVA_GRAPH));
    }

    @Test
    public void shouldConvertGuavaToJGraphT() {
        // When
        final AsUnmodifiableGraph<StringVertex, StringEdge> jGraphT =
                GraphConverters.asJGraphT(
                        GUAVA_GRAPH,
                        StringEdge.class,
                        StringEdge::new);

        // Then
        assertActualEqualsExpected(jGraphT, jGraphT);
    }
}
