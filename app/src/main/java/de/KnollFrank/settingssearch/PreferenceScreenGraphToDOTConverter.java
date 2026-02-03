package de.KnollFrank.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

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
import de.KnollFrank.lib.settingssearch.common.Preferences;

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
                .preferenceScreen()
                .toString()
                .concat("_")
                .concat(Integer.toHexString(preferenceScreenOfHostOfActivity.preferenceScreen().hashCode()))
                .replace(' ', '_');
    }

    private static Map<String, Attribute> getVertexAttribute(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return ImmutableMap
                .<String, Attribute>builder()
                .put(
                        "label",
                        DefaultAttribute.createAttribute(getLabel(preferenceScreenOfHostOfActivity.preferenceScreen())))
                .put("shape", DefaultAttribute.createAttribute("box"))
                .build();
    }

    private static String getLabel(final PreferenceScreen preferenceScreen) {
        return preferenceScreen.toString() + "\\l\\l" + getPreferences(preferenceScreen);
    }

    private static String getPreferences(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getChildrenRecursively(preferenceScreen)
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
