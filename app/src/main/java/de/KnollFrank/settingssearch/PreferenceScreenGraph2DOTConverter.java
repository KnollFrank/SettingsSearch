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
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenGraph2DOTConverter {

	public static String graph2DOT(final Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) throws ExportException {
		final Writer writer = new StringWriter();
		getDOTExporter().exportGraph(preferenceScreenGraph, writer);
		return writer.toString();
	}

	private static DOTExporter<SearchablePreferenceScreenWithHost, PreferenceEdge> getDOTExporter() {
		final DOTExporter<SearchablePreferenceScreenWithHost, PreferenceEdge> exporter =
				new DOTExporter<>(PreferenceScreenGraph2DOTConverter::getVertexId);
		exporter.setVertexAttributeProvider(PreferenceScreenGraph2DOTConverter::getVertexAttribute);
		exporter.setEdgeAttributeProvider(PreferenceScreenGraph2DOTConverter::getEdgeAttribute);
		return exporter;
	}

	private static String getVertexId(final SearchablePreferenceScreenWithHost searchablePreferenceScreenWithHost) {
		return searchablePreferenceScreenWithHost
				.searchablePreferenceScreen()
				.searchablePreferenceScreen()
				.toString()
				.concat("_")
				.concat(Integer.toHexString(searchablePreferenceScreenWithHost.searchablePreferenceScreen().searchablePreferenceScreen().hashCode()))
				.replace(' ', '_');
	}

	private static Map<String, Attribute> getVertexAttribute(final SearchablePreferenceScreenWithHost searchablePreferenceScreenWithHost) {
		return ImmutableMap
				.<String, Attribute>builder()
				.put(
						"label",
						DefaultAttribute.createAttribute(getLabel(searchablePreferenceScreenWithHost.searchablePreferenceScreen().searchablePreferenceScreen())))
				.put("shape", DefaultAttribute.createAttribute("box"))
				.build();
	}

	private static String getLabel(final PreferenceScreen preferenceScreen) {
		return preferenceScreen.toString() + "\\l\\l" + getPreferences(preferenceScreen);
	}

	private static String getPreferences(final PreferenceScreen preferenceScreen) {
		return Preferences
				.getAllChildren(preferenceScreen)
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
