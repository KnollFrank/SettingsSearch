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

        final StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        sb.append("  rankdir=TB;\n");
        sb.append("  label=\"Graph Comparison Details\\n\\n\";\n");
        sb.append("  labelloc=t;\n");
        sb.append("  fontsize=24;\n\n");

        // 1. Sektion: ACTUAL Graph
        sb.append("  subgraph cluster_actual {\n");
        sb.append("    label=\"1. ACTUAL GRAPH (Result of Merger)\";\n");
        sb.append("    style=dashed; color=gray; fontcolor=red;\n");
        sb.append(exportSubGraph(actual, "actual_"));
        sb.append("  }\n\n");

        // 2. Sektion: EXPECTED Graph
        sb.append("  subgraph cluster_expected {\n");
        sb.append("    label=\"2. EXPECTED GRAPH (Reference)\";\n");
        sb.append("    style=dashed; color=gray; fontcolor=green;\n");
        sb.append(exportSubGraph(expected, "expected_"));
        sb.append("  }\n\n");

        // 3. Sektion: MERGED (DIFF) Graph
        sb.append("  subgraph cluster_merged {\n");
        sb.append("    label=\"3. MERGED GRAPH (Differences Highlighted)\";\n");
        sb.append("    style=bold; color=black;\n");
        sb.append(exportDiffGraph());
        sb.append("  }\n\n");

        // Legende hinzufügen
        sb.append(getLegendContent());
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Exportiert einen einzelnen Graphen als DOT-Fragment mit einem Präfix für die IDs,
     * damit sich die Knoten der verschiedenen Sektionen nicht gegenseitig überschreiben.
     */
    private String exportSubGraph(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph, String idPrefix) {
        DOTExporter<SearchablePreferenceScreen, SearchablePreferenceEdge> exporter = new DOTExporter<>(v -> idPrefix + getVertexId(v));
        exporter.setVertexAttributeProvider(v -> Map.of(
                "shape", DefaultAttribute.createAttribute("none"),
                "label", new Attribute() {
                    @Override
                    public String getValue() {
                        return getVertexHtmlLabel(v, "white");
                    }

                    @Override
                    public AttributeType getType() {
                        return AttributeType.HTML;
                    }
                }));
        exporter.setEdgeAttributeProvider(e -> Map.of(
                "label", getLabelEdgeAttribute(e),
                "color", DefaultAttribute.createAttribute("black")));

        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);

        // Extrahiere nur den Inhalt zwischen den geschweiften Klammern des exportierten Graphen
        String result = writer.toString();
        return result.substring(result.indexOf("{") + 1, result.lastIndexOf("}"));
    }

    /**
     * Die Logik für den Diff-Graphen (ehemals der Inhalt deiner toString Methode).
     */
    private String exportDiffGraph() {
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergedGraph =
                GraphTypeBuilder
                        .<SearchablePreferenceScreen, SearchablePreferenceEdge>directed()
                        .allowingMultipleEdges(true)
                        .edgeClass(SearchablePreferenceEdge.class)
                        .buildGraph();

        allVertices.forEach(mergedGraph::addVertex);
        Sets.union(actual.edgeSet(), expected.edgeSet()).forEach(edge -> {
            final SearchablePreferenceScreen source = actual.containsEdge(edge) ? actual.getEdgeSource(edge) : expected.getEdgeSource(edge);
            final SearchablePreferenceScreen target = actual.containsEdge(edge) ? actual.getEdgeTarget(edge) : expected.getEdgeTarget(edge);
            if (source != null && target != null) {
                mergedGraph.addEdge(source, target, edge);
            }
        });

        DOTExporter<SearchablePreferenceScreen, SearchablePreferenceEdge> exporter = new DOTExporter<>(v -> "diff_" + getVertexId(v));
        exporter.setVertexAttributeProvider(this::getVertexAttributes);
        exporter.setEdgeAttributeProvider(this::getEdgeAttributes);

        Writer writer = new StringWriter();
        exporter.exportGraph(mergedGraph, writer);
        String result = writer.toString();
        return result.substring(result.indexOf("{") + 1, result.lastIndexOf("}"));
    }

    private String getLegendContent() {
        return "  // Legend\n" +
                "  subgraph cluster_legend {\n" +
                "    label=\"Legend\";\n" +
                "    legend_node [shape=plaintext, label=<\n" +
                "      <table border='0' cellborder='1' cellspacing='0'>\n" +
                "        <tr><td>Color</td><td>Meaning</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_ONLY_IN_ACTUAL + "'>      </td><td>Only in Actual (Removed / Extra)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_ONLY_IN_EXPECTED + "'>      </td><td>Only in Expected (Missing)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_CONTENT_MISMATCH + "'>      </td><td>Content Mismatch</td></tr>\n" +
                "        <tr><td bgcolor='white'>      </td><td>Identical</td></tr>\n" +
                "      </table>\n" +
                "    >];\n" +
                "  }\n";
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

        // Haupttabelle (Wurzelelement des HTML-Labels)
        // Wir erhöhen colspan auf 3, da wir nun drei Spalten haben (ID, KEY, TITLE)
        sb.append("<table border='0' cellborder='1' cellspacing='0' cellpadding='4' bgcolor='").append(bgColor).append("'>");

        // Titel-Zeile des Screens (Zusammengefasst über alle 3 Spalten)
        sb.append("<tr><td align='center' colspan='3' bgcolor='#F0F0F0'><b>")
                .append(escapeHtml(String.format("%s (%s)",
                                                 screen.title().orElse("Untitled"),
                                                 screen.host().getSimpleName())))
                .append("</b></td></tr>");

        // Spaltenüberschriften für die Präferenzen
        sb.append("<tr>")
                .append("<td align='left' bgcolor='#E0E0E0'><b>ID</b></td>")
                .append("<td align='left' bgcolor='#E0E0E0'><b>KEY</b></td>") // NEU
                .append("<td align='left' bgcolor='#E0E0E0'><b>Title</b></td>")
                .append("</tr>");

        screen.allPreferencesOfPreferenceHierarchy().forEach(pref -> {
            if (preferenceContentDiffs.containsKey(pref)) {
                // DIFF-Fall: Belegt alle 3 Spalten (colspan='3')
                sb.append("<tr><td colspan='3' border='0' align='left' cellpadding='0'>");
                sb.append("<table border='0' cellborder='1' cellspacing='0' cellpadding='2' width='100%' bgcolor='white'>");

                // EXPECTED-Zeile
                sb.append("<tr>")
                        .append("<td align='left' bgcolor='#EEFFEE'><font point-size='10' color='#006600'><b>EXPECTED:</b></font></td>")
                        .append("<td align='left' bgcolor='#EEFFEE'><font point-size='10'>")
                        .append(escapeHtml(preferenceContentDiffs.get(pref)))
                        .append("</font></td>")
                        .append("</tr>");

                // ACTUAL-Zeile
                sb.append("<tr>")
                        .append("<td align='left' bgcolor='#FFEEEE'><font point-size='10' color='#CC0000'><b>ACTUAL:</b></font></td>")
                        .append("<td align='left' bgcolor='#FFEEEE'><font point-size='10'>")
                        .append(escapeHtml(pref.toString()))
                        .append("</font></td>")
                        .append("</tr>");

                sb.append("</table>");
                sb.append("</td></tr>");
            } else {
                // IDENTISCH-Fall: Drei Spalten bündig unter den Headern
                sb.append("<tr>");

                // Spalte 1: ID
                sb.append("<td align='left' bgcolor='white'><font point-size='10'>")
                        .append(escapeHtml(pref.getId()))
                        .append("</font></td>");

                // Spalte 2: KEY (NEU)
                sb.append("<td align='left' bgcolor='white'><font point-size='10'>")
                        .append(escapeHtml(pref.getKey()))
                        .append("</font></td>");

                // Spalte 3: Title
                sb.append("<td align='left' bgcolor='white'>")
                        .append(escapeHtml(pref.getTitle().orElse("No Title")))
                        .append("</td>");

                sb.append("</tr>");
            }
        });

        sb.append("</table>");
        return sb.toString();
    }

    private Map<String, Attribute> getEdgeAttributes(final SearchablePreferenceEdge edge) {
        return Map.of(
                "label", getLabelEdgeAttribute(edge),
                "color", DefaultAttribute.createAttribute(getColor(edge)));
    }

    private static Attribute getLabelEdgeAttribute(final SearchablePreferenceEdge edge) {
        return DefaultAttribute.createAttribute("\"" + edge.preference.getId() + "\"");
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
