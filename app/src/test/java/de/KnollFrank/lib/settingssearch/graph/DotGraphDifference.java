package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
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

// FK-TODO: refactor
public class DotGraphDifference {

    private static final String COLOR_ONLY_IN_ACTUAL = "red";
    private static final String COLOR_ONLY_IN_EXPECTED = "green";
    private static final String COLOR_CONTENT_MISMATCH = "#FFCC00";
    private static final String COLOR_DEFAULT = "black";
    private static final String COLOR_CONTENT_IDENTICAL = "white";

    private final boolean areEqual;
    private final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual;
    private final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected;
    private final Set<SearchablePreferenceScreen> allVertices;
    private final Set<SearchablePreferenceScreen> verticesOnlyInActual;
    private final Set<SearchablePreferenceScreen> verticesOnlyInExpected;
    private final Set<SearchablePreferenceEdge> edgesOnlyInActual;
    private final Set<SearchablePreferenceEdge> edgesOnlyInExpected;
    private final Map<SearchablePreference, String> preferenceContentDiffs;

    private DotGraphDifference(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual,
                               final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected) {
        this.actual = actual;
        this.expected = expected;
        verticesOnlyInActual = Sets.difference(actual.vertexSet(), expected.vertexSet());
        verticesOnlyInExpected = Sets.difference(expected.vertexSet(), actual.vertexSet());
        edgesOnlyInActual = Sets.difference(actual.edgeSet(), expected.edgeSet());
        edgesOnlyInExpected = Sets.difference(expected.edgeSet(), actual.edgeSet());
        preferenceContentDiffs = findPreferenceContentDifferences();
        areEqual = verticesOnlyInActual.isEmpty()
                && verticesOnlyInExpected.isEmpty()
                && edgesOnlyInActual.isEmpty()
                && edgesOnlyInExpected.isEmpty()
                && preferenceContentDiffs.isEmpty();
        allVertices = Sets.union(actual.vertexSet(), expected.vertexSet());
    }

    public static DotGraphDifference between(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual,
                                             final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected) {
        return new DotGraphDifference(actual, expected);
    }

    public boolean areEqual() {
        return areEqual;
    }

    @Override
    public String toString() {
        if (areEqual) {
            return "Graphs are equal.";
        }

        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                GraphTypeBuilder
                        .<SearchablePreferenceScreen, SearchablePreferenceEdge>directed()
                        .allowingMultipleEdges(true)
                        .edgeClass(SearchablePreferenceEdge.class)
                        .buildGraph();

        allVertices.forEach(mergedGraph::addVertex);
        Sets
                .union(actual.edgeSet(), expected.edgeSet())
                .forEach(
                        edge -> {
                            final SearchablePreferenceScreen source = actual.containsEdge(edge) ? actual.getEdgeSource(edge) : expected.getEdgeSource(edge);
                            final SearchablePreferenceScreen target = actual.containsEdge(edge) ? actual.getEdgeTarget(edge) : expected.getEdgeTarget(edge);
                            if (source != null && target != null) {
                                mergedGraph.addEdge(source, target, edge);
                            }
                        });

        final DOTExporter<SearchablePreferenceScreen, SearchablePreferenceEdge> exporter = new DOTExporter<>(this::getVertexId);
        exporter.setVertexAttributeProvider(this::getVertexAttributes);
        exporter.setEdgeAttributeProvider(this::getEdgeAttributes);
        exporter.setGraphAttributeProvider(
                () ->
                        ImmutableMap
                                .<String, Attribute>builder()
                                .put(
                                        "rankdir",
                                        DefaultAttribute.createAttribute("TB"))
                                .put(
                                        "label",
                                        DefaultAttribute.createAttribute("\"Graph Difference\\n\\n\""))
                                .put(
                                        "labelloc",
                                        DefaultAttribute.createAttribute("t"))
                                .put(
                                        "fontsize",
                                        DefaultAttribute.createAttribute("20"))
                                .build());

        final Writer writer = new StringWriter();
        try {
            exporter.exportGraph(mergedGraph, writer);
            return addLegend(writer.toString());
        } catch (final Exception e) {
            return "Error exporting graph to DOT: " + e.getMessage();
        }
    }

    private Map<String, Attribute> getVertexAttributes(final SearchablePreferenceScreen vertex) {
        return ImmutableMap
                .<String, Attribute>builder()
                .put(
                        "shape",
                        DefaultAttribute.createAttribute("none"))
                .put(
                        "label",
                        new Attribute() {

                            private final String htmlLabel = getVertexHtmlLabel(vertex, getBgColor(vertex));

                            @Override
                            public String getValue() {
                                return htmlLabel;
                            }

                            @Override
                            public AttributeType getType() {
                                return AttributeType.HTML;
                            }
                        })
                .build();
    }

    private String getBgColor(final SearchablePreferenceScreen vertex) {
        if (verticesOnlyInActual.contains(vertex)) {
            return COLOR_ONLY_IN_ACTUAL;
        } else if (verticesOnlyInExpected.contains(vertex)) {
            return COLOR_ONLY_IN_EXPECTED;
        } else if (hasAnyPreferenceDiff(vertex)) {
            return COLOR_CONTENT_MISMATCH;
        }
        return COLOR_CONTENT_IDENTICAL;
    }

    private boolean hasAnyPreferenceDiff(final SearchablePreferenceScreen screen) {
        return screen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .anyMatch(preferenceContentDiffs::containsKey);
    }

    private String getVertexHtmlLabel(final SearchablePreferenceScreen screen, final String bgColor) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<table border='0' cellborder='1' cellspacing='0' cellpadding='4' bgcolor='").append(bgColor).append("'>");
        sb.append("<tr><td align='center'><b>").append(escapeHtml(String.format("%s, %s", screen.title().orElseThrow(), screen.host().getSimpleName()))).append("</b></td></tr>");
        sb.append("<tr><td align='left' balign='left'>");
        screen
                .allPreferencesOfPreferenceHierarchy()
                .forEach(
                        pref -> {
                            if (preferenceContentDiffs.containsKey(pref)) {
                                sb.append("<table border='0' cellborder='1' cellspacing='0' cellpadding='2' bgcolor='white'>");
                                sb
                                        .append(String.format("<tr><td><font point-size='10' color='%s'>EXPECTED: </font></td><td align='left' bgcolor='#EEFFEE'>", COLOR_ONLY_IN_EXPECTED))
                                        .append(escapeHtml(preferenceContentDiffs.get(pref)))
                                        .append("</td></tr>");
                                sb
                                        .append(String.format("<tr><td><font point-size='10' color='%s'>ACTUAL: </font></td><td align='left' bgcolor='#FFEEEE'>", COLOR_ONLY_IN_ACTUAL))
                                        .append(escapeHtml(pref.toString()))
                                        .append("</td></tr>");
                                sb.append("</table>");
                            } else {
                                sb
                                        .append(escapeHtml(pref.getTitle().orElseThrow()))
                                        .append("<br/>");
                            }
                        });
        sb.append("</td></tr></table>");
        return sb.toString();
    }

    private Map<String, Attribute> getEdgeAttributes(final SearchablePreferenceEdge edge) {
        return Map.of(
                "label", DefaultAttribute.createAttribute("\"" + edge.preference.getTitle().orElseThrow() + "\""),
                "color", DefaultAttribute.createAttribute(getColor(edge)));
    }

    private String getColor(final SearchablePreferenceEdge edge) {
        if (edgesOnlyInActual.contains(edge)) {
            return COLOR_ONLY_IN_ACTUAL;
        } else if (edgesOnlyInExpected.contains(edge)) {
            return COLOR_ONLY_IN_EXPECTED;
        } else {
            return COLOR_DEFAULT;
        }
    }

    private Map<SearchablePreference, String> findPreferenceContentDifferences() {
        final Map<SearchablePreference, String> diffs = new HashMap<>();
        final Map<String, SearchablePreferenceScreen> actualScreensById = getScreensById(actual.vertexSet());
        final Map<String, SearchablePreferenceScreen> expectedScreensById = getScreensById(expected.vertexSet());

        final Set<String> commonIds = Sets.intersection(actualScreensById.keySet(), expectedScreensById.keySet());

        for (final String id : commonIds) {
            final SearchablePreferenceScreen sActual = actualScreensById.get(id);
            final SearchablePreferenceScreen sExpected = expectedScreensById.get(id);

            final Map<String, SearchablePreference> expectedPrefsById = getPrefsById(sExpected.allPreferencesOfPreferenceHierarchy());

            for (final SearchablePreference prefActual : sActual.allPreferencesOfPreferenceHierarchy()) {
                final SearchablePreference prefExpected = expectedPrefsById.get(prefActual.getId());
                if (prefExpected != null && !prefActual.toString().equals(prefExpected.toString())) {
                    diffs.put(prefActual, prefExpected.toString());
                }
            }
        }
        return diffs;
    }

    private String addLegend(final String dot) {
        final String legend = "\n  // Legend\n" +
                "  subgraph cluster_legend {\n" +
                "    label=\"Legend\";\n" +
                "    legend_node [shape=plaintext, label=<\n" +
                "      <table border='0' cellborder='1' cellspacing='0'>\n" +
                "        <tr><td>Color</td><td>Meaning</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_ONLY_IN_ACTUAL + "'>      </td><td>Only in Actual (Removed / Extra)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_ONLY_IN_EXPECTED + "'>      </td><td>Only in Expected (Missing)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_CONTENT_MISMATCH + "'>      </td><td>Content Mismatch (same ID, different state)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_CONTENT_IDENTICAL + "'>      </td><td>Identical</td></tr>\n" +
                "      </table>\n" +
                "    >];\n" +
                "  }\n" +
                "}";
        return dot.substring(0, dot.lastIndexOf('}')) + legend;
    }

    private String getVertexId(final SearchablePreferenceScreen vertex) {
        return "screen_" + vertex.id().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
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
