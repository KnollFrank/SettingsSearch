package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.graph.StringGraphEquality.assertActualEqualsExpected;

import com.google.common.graph.ImmutableValueGraph;

import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

public class GraphConverterTest {

    private final StringVertex vA = new StringVertex("A");
    private final StringVertex vB = new StringVertex("B");

    // [A] --("Edge-AB")--> [B]
    private final AsUnmodifiableGraph<StringVertex, StringEdge> jGraphT_GRAPH =
            new AsUnmodifiableGraph<>(
                    DefaultDirectedGraph.<StringVertex, StringEdge>createBuilder(StringEdge.class)
                            .addVertex(vA)
                            .addVertex(vB)
                            .addEdge(vA, vB, new StringEdge("Edge-AB"))
                            .build());

    // [A] --("Edge-AB")--> [B]
    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    private final ImmutableValueGraph<StringVertex, String> GUAVA_GRAPH =
            StringGraphs
                    .newStringGraphBuilder()
                    .addNode(vA)
                    .addNode(vB)
                    .putEdgeValue(vA, vB, "Edge-AB")
                    .build();

    private final GraphConverter<StringVertex, StringEdge, String> graphConverter =
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
