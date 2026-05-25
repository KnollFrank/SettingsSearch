package de.KnollFrank.settingssearch;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;

import org.jgrapht.Graph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;

class PreferenceScreenGraphToDOTConverter {

    private PreferenceScreenGraphToDOTConverter() {
    }

    public static String graphToDOT(final Graph<PreferenceScreenOfHostOfActivity, PreferenceEdge> preferenceScreenGraph) throws ExportException {
        final Writer writer = new StringWriter();
        getDOTExporter().exportGraph(preferenceScreenGraph, writer);
        return writer.toString();
    }

    private static DOTExporter<PreferenceScreenOfHostOfActivity, PreferenceEdge> getDOTExporter() {
        final DOTExporter<PreferenceScreenOfHostOfActivity, PreferenceEdge> exporter =
                new DOTExporter<>(PreferenceScreenGraphToDOTConverter::getVertexId);
        exporter.setVertexAttributeProvider(PreferenceScreenGraphToDOTConverter::getVertexAttribute);
        exporter.setEdgeAttributeProvider(PreferenceScreenGraphToDOTConverter::getEdgeAttribute);
        return exporter;
    }

    private static String getVertexId(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return preferenceScreenOfHostOfActivity
                .title()
                .orElse("Untitled")
                .concat("_")
                .concat(Integer.toHexString(preferenceScreenOfHostOfActivity.hashCode()))
                .replace(' ', '_');
    }

    private static Map<String, Attribute> getVertexAttribute(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return ImmutableMap
                .<String, Attribute>builder()
                .put(
                        "label",
                        DefaultAttribute.createAttribute(getLabel(preferenceScreenOfHostOfActivity)))
                .put("shape", DefaultAttribute.createAttribute("box"))
                .build();
    }

    private static String getLabel(final PreferenceScreenOfHostOfActivity node) {
        return node.title().orElse("Untitled") + "\\l\\l" + getPreferences(node);
    }

    private static String getPreferences(final PreferenceScreenOfHostOfActivity node) {
        return node
                .preferences()
                .stream()
                .map(Preference::toString)
                .collect(Collectors.joining("\\l")) + "\\l";
    }

    private static Map<String, Attribute> getEdgeAttribute(final PreferenceEdge preferenceEdge) {
        return ImmutableMap.of(
                "label",
                DefaultAttribute.createAttribute(preferenceEdge.preference.getTitle().toString()));
    }
}
