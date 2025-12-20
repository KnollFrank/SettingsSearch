package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.Sets;

import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class DotGraphDifference {

    // Colors for highlighting differences
    private static final String COLOR_ONLY_IN_ACTUAL = "red";
    private static final String COLOR_ONLY_IN_EXPECTED = "green";
    private static final String COLOR_CONTENT_MISMATCH = "orange";
    private static final String COLOR_DEFAULT = "black";

    private final boolean areEqual;
    private final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual;
    private final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected;
    private final Set<SearchablePreferenceScreen> allVertices;

    // Sets containing the differences for quick lookup
    private final Set<SearchablePreferenceScreen> verticesOnlyInActual;
    private final Set<SearchablePreferenceScreen> verticesOnlyInExpected;
    private final Set<SearchablePreferenceEdge> edgesOnlyInActual;
    private final Set<SearchablePreferenceEdge> edgesOnlyInExpected;
    private final Map<SearchablePreference, String> preferenceContentDiffs;

    private DotGraphDifference(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual,
                               final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected) {
        this.actual = actual;
        this.expected = expected;

        // Structural differences
        this.verticesOnlyInActual = Sets.difference(actual.vertexSet(), expected.vertexSet());
        this.verticesOnlyInExpected = Sets.difference(expected.vertexSet(), actual.vertexSet());
        this.edgesOnlyInActual = Sets.difference(actual.edgeSet(), expected.edgeSet());
        this.edgesOnlyInExpected = Sets.difference(expected.edgeSet(), actual.edgeSet());

        // Content differences
        this.preferenceContentDiffs = findPreferenceContentDifferences();

        this.areEqual = verticesOnlyInActual.isEmpty()
                && verticesOnlyInExpected.isEmpty()
                && edgesOnlyInActual.isEmpty()
                && edgesOnlyInExpected.isEmpty()
                && preferenceContentDiffs.isEmpty();

        // A combined set of all vertices from both graphs for the exporter
        this.allVertices = Sets.union(actual.vertexSet(), expected.vertexSet());
    }

    public static DotGraphDifference between(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual,
                                             final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected) {
        return new DotGraphDifference(actual, expected);
    }

    public boolean areEqual() {
        return areEqual;
    }

    /**
     * Generates a DOT representation of a merged graph, highlighting the differences.
     *
     * @return A string in DOT format.
     */
    @Override
    public String toString() {
        if (areEqual) {
            return "Graphs are equal.";
        }

        // Create a merged graph containing all nodes and edges from both graphs
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                GraphTypeBuilder
                        .<SearchablePreferenceScreen, SearchablePreferenceEdge>directed()
                        .allowingMultipleEdges(true)
                        .allowingSelfLoops(true)
                        .edgeClass(SearchablePreferenceEdge.class)
                        .buildGraph();

        allVertices.forEach(mergedGraph::addVertex);
        Sets
                .union(actual.edgeSet(), expected.edgeSet())
                .forEach(
                        edge -> {
                            SearchablePreferenceScreen source = actual.getEdgeSource(edge);
                            if (source == null) {
                                source = expected.getEdgeSource(edge);
                            }

                            SearchablePreferenceScreen target = actual.getEdgeTarget(edge);
                            if (target == null) {
                                target = expected.getEdgeTarget(edge);
                            }

                            // Nur wenn beide Knoten gefunden wurden, fügen wir die Kante hinzu.
                            if (source != null && target != null) {
                                mergedGraph.addEdge(source, target, edge);
                            }
                        });

        // Configure the DOT exporter with custom attribute providers
        final DOTExporter<SearchablePreferenceScreen, SearchablePreferenceEdge> exporter = new DOTExporter<>(this::getVertexId);
        exporter.setVertexAttributeProvider(this::getVertexAttributes);
        exporter.setEdgeAttributeProvider(this::getEdgeAttributes);
        exporter.setGraphAttributeProvider(() -> Map.of("label", DefaultAttribute.createAttribute("\"Graph Difference\"")));

        final Writer writer = new StringWriter();
        try {
            exporter.exportGraph(mergedGraph, writer);
        } catch (final Exception e) {
            return "Error exporting graph to DOT: " + e.getMessage();
        }
        return writer.toString();
    }

    private String getVertexId(final SearchablePreferenceScreen vertex) {
        // Use a consistent ID based on the screen's own ID
        return "screen_" + vertex.id().replace(' ', '_').replaceAll("[^a-zA-Z0-9_]", "");
    }

    private Map<String, Attribute> getVertexAttributes(final SearchablePreferenceScreen vertex) {
        final Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("shape", DefaultAttribute.createAttribute("box"));

        String color = COLOR_DEFAULT;
        if (verticesOnlyInActual.contains(vertex)) {
            color = COLOR_ONLY_IN_ACTUAL;
        } else if (verticesOnlyInExpected.contains(vertex)) {
            color = COLOR_ONLY_IN_EXPECTED;
        }

        attributes.put("color", DefaultAttribute.createAttribute(color));
        attributes.put("label", DefaultAttribute.createAttribute(getVertexLabel(vertex)));
        return attributes;
    }

    private String getVertexLabel(final SearchablePreferenceScreen screen) {
        final StringBuilder labelBuilder = new StringBuilder();
        labelBuilder.append(screen.title()).append("\\n------------------\\n");
        // Append preference details, highlighting differences
        screen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .map(
                        pref -> {
                            String prefString = pref.toString();
                            if (preferenceContentDiffs.containsKey(pref)) {
                                prefString = "[DIFF] " + prefString + " | Expected: " + preferenceContentDiffs.get(pref);
                            }
                            return prefString;
                        })
                .forEach(prefString -> labelBuilder.append(prefString).append("\\l"));
        return labelBuilder.toString();
    }

    private Map<String, Attribute> getEdgeAttributes(final SearchablePreferenceEdge edge) {
        String color = COLOR_DEFAULT;
        if (edgesOnlyInActual.contains(edge)) {
            color = COLOR_ONLY_IN_ACTUAL;
        } else if (edgesOnlyInExpected.contains(edge)) {
            color = COLOR_ONLY_IN_EXPECTED;
        }
        return Map.of(
                "label", DefaultAttribute.createAttribute(edge.preference.getTitle().toString()),
                "color", DefaultAttribute.createAttribute(color));
    }

    private Map<SearchablePreference, String> findPreferenceContentDifferences() {
        final Map<SearchablePreference, String> diffs = new HashMap<>();

        // Erstelle Lookup-Maps für beide Seiten, um sicher zwischen den Instanzen zu unterscheiden
        final Map<String, SearchablePreferenceScreen> actualScreensById = getScreensById(actual.vertexSet());
        final Map<String, SearchablePreferenceScreen> expectedScreensById = getScreensById(expected.vertexSet());

        // Bilde die Schnittmenge der IDs (da diese die logische Identität definieren)
        final Set<String> commonIds = Sets.intersection(actualScreensById.keySet(), expectedScreensById.keySet());

        for (final String id : commonIds) {
            // Hole explizit beide Instanzen
            final SearchablePreferenceScreen sActual = actualScreensById.get(id);
            final SearchablePreferenceScreen sExpected = expectedScreensById.get(id);

            // Vergleiche die Präferenzen innerhalb dieser Screens
            final Map<String, SearchablePreference> expectedPrefsById = getPrefsById(sExpected.allPreferencesOfPreferenceHierarchy());

            for (final SearchablePreference prefActual : sActual.allPreferencesOfPreferenceHierarchy()) {
                final SearchablePreference prefExpected = expectedPrefsById.get(prefActual.getId());

                // Wenn die Preference in beiden vorhanden ist, vergleiche den Zustand via toString()
                if (prefExpected != null && !prefActual.toString().equals(prefExpected.toString())) {
                    // Wir nutzen prefActual als Schlüssel, da dieser Knoten im mergedGraph
                    // (der auf dem 'actual'-Set basiert) angezeigt wird.
                    diffs.put(prefActual, prefExpected.toString());
                }
            }
        }
        return diffs;
    }

    private static Map<String, SearchablePreference> getPrefsById(final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                Function.identity()));
    }

    private static Map<String, SearchablePreferenceScreen> getScreensById(final Set<SearchablePreferenceScreen> searchablePreferenceScreens) {
        return searchablePreferenceScreens
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceScreen::id,
                                Function.identity()));
    }
}
