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
// in order to display dotGraphDifference:
// 1. save output to a file named dotGraphDifference.dot
// 2. dot -Tpdf dotGraphDifference.dot -o dotGraphDifference.pdf
// 3. open dotGraphDifference.pdf
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

    private String exportSubGraph(Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph, String idPrefix) {
        DOTExporter<SearchablePreferenceScreen, SearchablePreferenceEdge> exporter = new DOTExporter<>(v -> idPrefix + getVertexId(v));
        exporter.setVertexAttributeProvider(v -> Map.of(
                "shape", DefaultAttribute.createAttribute("none"),
                "label", new Attribute() {
                    @Override
                    public String getValue() {
                        return getVertexHtmlLabel(v, "white", false);
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
        String result = writer.toString();
        return result.substring(result.indexOf("{") + 1, result.lastIndexOf("}"));
    }

    private String exportDiffGraph() {
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

        final DOTExporter<SearchablePreferenceScreen, SearchablePreferenceEdge> exporter = new DOTExporter<>(v -> "diff_" + getVertexId(v));
        exporter.setVertexAttributeProvider(this::getVertexAttributes);
        exporter.setEdgeAttributeProvider(this::getEdgeAttributes);

        final Writer writer = new StringWriter();
        exporter.exportGraph(mergedGraph, writer);
        final String result = writer.toString();
        return result.substring(result.indexOf("{") + 1, result.lastIndexOf("}"));
    }

    private Map<String, Attribute> getVertexAttributes(final SearchablePreferenceScreen vertex) {
        return ImmutableMap.<String, Attribute>builder()
                .put("shape", DefaultAttribute.createAttribute("none"))
                .put("label", new Attribute() {
                    @Override
                    public String getValue() {
                        return getVertexHtmlLabel(vertex, getBgColor(vertex), true);
                    }

                    @Override
                    public AttributeType getType() {
                        return AttributeType.HTML;
                    }
                })
                .build();
    }

    private String getVertexHtmlLabel(final SearchablePreferenceScreen screen, final String bgColor, boolean showDifferences) {
        final StringBuilder sb = new StringBuilder();

        // Haupttabelle (Wurzelelement des HTML-Labels)
        sb.append("<table border='0' cellborder='1' cellspacing='0' cellpadding='4' bgcolor='").append(bgColor).append("'>");

        // --- SEKTION 1: SCREEN METADATEN ---
        // Zeile 1: ID
        sb.append("<tr>")
                .append("<td align='left' bgcolor='#F0F0F0'><b>ID</b></td>")
                .append("<td align='left' colspan='2' bgcolor='#F0F0F0'>").append(escapeHtml(screen.id())).append("</td>")
                .append("</tr>");

        // Zeile 2: TITLE
        sb.append("<tr>")
                .append("<td align='left' bgcolor='#F0F0F0'><b>Title</b></td>")
                .append("<td align='left' colspan='2' bgcolor='#F0F0F0'>").append(escapeHtml(screen.title().orElse("-"))).append("</td>")
                .append("</tr>");

        // Zeile 3: SUMMARY
        sb.append("<tr>")
                .append("<td align='left' bgcolor='#F0F0F0'><b>Summary</b></td>")
                .append("<td align='left' colspan='2' bgcolor='#F0F0F0'>").append(escapeHtml(screen.summary().orElse("-"))).append("</td>")
                .append("</tr>");

        // Zeile 4: HOST (Neu hinzugefügt)
        sb.append("<tr>")
                .append("<td align='left' bgcolor='#F0F0F0'><b>Host</b></td>")
                .append("<td align='left' colspan='2' bgcolor='#F0F0F0'>").append(escapeHtml(screen.host().preferenceFragmentClass().getName())).append("</td>")
                .append("</tr>");

        // Trenner / Sektions-Header
        sb.append("<tr><td align='center' colspan='3' bgcolor='#D0D0D0'><b>Preferences</b></td></tr>");

        // --- SEKTION 2: PRÄFERENZEN TABELLENKOPF ---
        sb.append("<tr>")
                .append("<td align='left' bgcolor='#E0E0E0'><b>Pref ID</b></td>")
                .append("<td align='left' bgcolor='#E0E0E0'><b>KEY</b></td>")
                .append("<td align='left' bgcolor='#E0E0E0'><b>Title</b></td>")
                .append("</tr>");

        // --- SEKTION 3: PRÄFERENZEN LISTE ---
        screen.allPreferencesOfPreferenceHierarchy().forEach(pref -> {
            if (showDifferences && preferenceContentDiffs.containsKey(pref)) {
                // DIFF-Fall: Belegt alle 3 Spalten
                sb.append("<tr><td colspan='3' border='0' align='left' cellpadding='0'>");
                sb.append("<table border='0' cellborder='1' cellspacing='0' cellpadding='2' width='100%' bgcolor='white'>");

                // EXPECTED-Zeile
                sb.append("<tr><td bgcolor='#EEFFEE'><font point-size='10' color='#006600'>EXPECTED:</font></td>")
                        .append("<td align='left' bgcolor='#EEFFEE'>").append(escapeHtml(preferenceContentDiffs.get(pref))).append("</td></tr>");

                // ACTUAL-Zeile
                sb.append("<tr><td bgcolor='#FFEEEE'><font point-size='10' color='#CC0000'>ACTUAL:</font></td>")
                        .append("<td align='left' bgcolor='#FFEEEE'>").append(escapeHtml(pref.toString())).append("</td></tr>");

                sb.append("</table></td></tr>");
            } else {
                // IDENTISCH-Fall: Drei Spalten bündig unter den Headern
                sb.append("<tr>")
                        .append("<td align='left' bgcolor='white'><font point-size='10'>").append(escapeHtml(pref.getId())).append("</font></td>")
                        .append("<td align='left' bgcolor='white'><font point-size='10'>").append(escapeHtml(pref.getKey())).append("</font></td>")
                        .append("<td align='left' bgcolor='white'>").append(escapeHtml(pref.getTitle().orElse("No Title"))).append("</td>")
                        .append("</tr>");
            }
        });
        sb.append("</table>");
        return sb.toString();
    }

    private static Attribute getLabelEdgeAttribute(final SearchablePreferenceEdge edge) {
        return DefaultAttribute.createAttribute("\"" + edge.preference.getId() + "\"");
    }

    private Map<String, Attribute> getEdgeAttributes(final SearchablePreferenceEdge edge) {
        String color = COLOR_DEFAULT;
        if (edgesOnlyInActual.contains(edge)) color = COLOR_ONLY_IN_ACTUAL;
        else if (edgesOnlyInExpected.contains(edge)) color = COLOR_ONLY_IN_EXPECTED;
        return Map.of("label", getLabelEdgeAttribute(edge), "color", DefaultAttribute.createAttribute(color));
    }

    private String getBgColor(final SearchablePreferenceScreen vertex) {
        if (verticesOnlyInActual.contains(vertex)) return COLOR_ONLY_IN_ACTUAL;
        if (verticesOnlyInExpected.contains(vertex)) return COLOR_ONLY_IN_EXPECTED;
        if (hasAnyPreferenceDiff(vertex)) return COLOR_CONTENT_MISMATCH;
        return COLOR_CONTENT_IDENTICAL;
    }

    private boolean hasAnyPreferenceDiff(final SearchablePreferenceScreen screen) {
        return screen.allPreferencesOfPreferenceHierarchy().stream().anyMatch(preferenceContentDiffs::containsKey);
    }

    private Map<SearchablePreference, String> findPreferenceContentDifferences() {
        final Map<SearchablePreference, String> diffs = new HashMap<>();
        final Map<String, SearchablePreferenceScreen> actualMap = actual.vertexSet().stream().collect(Collectors.toMap(SearchablePreferenceScreen::id, Function.identity(), (a, b) -> a));
        final Map<String, SearchablePreferenceScreen> expectedMap = expected.vertexSet().stream().collect(Collectors.toMap(SearchablePreferenceScreen::id, Function.identity(), (a, b) -> a));
        Sets.intersection(actualMap.keySet(), expectedMap.keySet()).forEach(id -> {
            SearchablePreferenceScreen sA = actualMap.get(id);
            SearchablePreferenceScreen sE = expectedMap.get(id);
            Map<String, SearchablePreference> ePrefs = sE.allPreferencesOfPreferenceHierarchy().stream().collect(Collectors.toMap(SearchablePreference::getId, Function.identity(), (a, b) -> a));
            sA.allPreferencesOfPreferenceHierarchy().forEach(pA -> {
                SearchablePreference pE = ePrefs.get(pA.getId());
                if (pE != null && !pA.toString().equals(pE.toString()))
                    diffs.put(pA, pE.toString());
            });
        });
        return diffs;
    }

    private String getLegendContent() {
        return "  subgraph cluster_legend {\n" +
                "    label=\"Legend\";\n" +
                "    legend_node [shape=plaintext, label=<\n" +
                "      <table border='0' cellborder='1' cellspacing='0'>\n" +
                "        <tr><td>Color</td><td>Meaning</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_ONLY_IN_ACTUAL + "'>      </td><td>Only in Actual (Removed / Extra)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_ONLY_IN_EXPECTED + "'>      </td><td>Only in Expected (Missing)</td></tr>\n" +
                "        <tr><td bgcolor='" + COLOR_CONTENT_MISMATCH + "'>      </td><td>Content Mismatch</td></tr>\n" +
                "      </table>>];\n  }\n";
    }

    private String getVertexId(final SearchablePreferenceScreen vertex) {
        return vertex.id().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }
}
