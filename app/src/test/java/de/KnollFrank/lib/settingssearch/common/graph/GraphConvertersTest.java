package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphEquality.assertActualEqualsExpected;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.junit.Test;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GraphConvertersTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");
    private final StringVertex vC = new StringVertex("C");

    @Test
    public void shouldConvertJGraphTToGuava() {
        // Given
        /*
         * Input JGraphT Graph:
         * [A] --("Edge-AB")--> [B]
         */
        final Graph<StringVertex, StringEdge> jgraphtGraph =
                StringGraphs
                        .newGraphBuilder()
                        .addVertex(vA)
                        .addVertex(vB)
                        .addEdge(vA, vB, new StringEdge("Edge-AB"))
                        .build();

        // When
        final ImmutableValueGraph<StringVertex, String> guavaGraph =
                GraphConverters.toGuava(
                        jgraphtGraph,
                        StringEdge::getLabel);

        // Then
        assertThat(
                guavaGraph,
                is(
                        ValueGraphBuilder
                                .directed()
                                .<StringVertex, String>immutable()
                                .addNode(vA)
                                .addNode(vB)
                                .putEdgeValue(vA, vB, "Edge-AB")
                                .build()));
    }

    @Test
    public void shouldConvertGuavaToJGraphT() {
        // Given
        /*
         * Input Guava ValueGraph:
         * [A] --("Edge-AC")--> [C]
         */
        final ImmutableValueGraph<StringVertex, String> guavaGraph =
                ValueGraphBuilder
                        .directed()
                        .<StringVertex, String>immutable()
                        .addNode(vA)
                        .addNode(vC)
                        .putEdgeValue(vA, vC, "Edge-AC")
                        .build();

        // When
        final AsUnmodifiableGraph<StringVertex, StringEdge> jGraphT =
                GraphConverters.asJGraphT(
                        guavaGraph,
                        StringEdge.class,
                        StringEdge::new);

        // Then
        assertActualEqualsExpected(
                jGraphT,
                StringGraphs
                        .newGraphBuilder()
                        .addVertex(vA)
                        .addVertex(vC)
                        .addEdge(vA, vC, new StringEdge("Edge-AC"))
                        .build());
    }
}
