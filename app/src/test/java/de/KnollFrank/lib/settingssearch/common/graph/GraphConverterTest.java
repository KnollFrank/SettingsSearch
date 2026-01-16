package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphEquality.assertActualEqualsExpected;

import com.google.common.graph.ImmutableValueGraph;

import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

public class GraphConverterTest {

    private final StringNode nA = new StringNode("A");
    private final StringNode nB = new StringNode("B");

    // [A] --("Edge-AB")--> [B]
    private final AsUnmodifiableGraph<StringNode, StringEdge> jGraphT_GRAPH =
            new AsUnmodifiableGraph<>(
                    DefaultDirectedGraph
                            .<StringNode, StringEdge>createBuilder(StringEdge.class)
                            .addVertex(nA)
                            .addVertex(nB)
                            .addEdge(nA, nB, new StringEdge("Edge-AB"))
                            .build());

    // [A] --("Edge-AB")--> [B]
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    private final ImmutableValueGraph<StringNode, String> GUAVA_GRAPH =
            StringGraphs
                    .newStringGraphBuilder()
                    .addNode(nA)
                    .addNode(nB)
                    .putEdgeValue(nA, nB, "Edge-AB")
                    .build();

    private final GraphConverter<StringNode, StringEdge, String> graphConverter =
            new GraphConverter<>(
                    new ToJGraphTConverter<>(StringEdge.class, StringEdge::new),
                    new ToGuavaGraphConverter<>(StringEdge::getLabel));

    @Test
    public void shouldConvertJGraphTToGuava() {
        assertThat(graphConverter.toGuava(jGraphT_GRAPH), is(GUAVA_GRAPH));
    }

    @Test
    public void shouldConvertGuavaToJGraphT() {
        assertActualEqualsExpected(graphConverter.toJGraphT(GUAVA_GRAPH), jGraphT_GRAPH);
    }
}
