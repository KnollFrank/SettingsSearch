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
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenGraphRenderer {

	public static void renderPreferenceScreenGraph(final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) throws ExportException {
		final Writer writer = new StringWriter();
		getDOTExporter().exportGraph(preferenceScreenGraph, writer);
		System.out.println(writer);
	}

	private static DOTExporter<PreferenceScreenWithHost, PreferenceEdge> getDOTExporter() {
		final DOTExporter<PreferenceScreenWithHost, PreferenceEdge> exporter =
				new DOTExporter<>(PreferenceScreenGraphRenderer::getVertexId);
		exporter.setVertexAttributeProvider(PreferenceScreenGraphRenderer::getVertexAttribute);
		exporter.setEdgeAttributeProvider(PreferenceScreenGraphRenderer::getEdgeAttribute);
		return exporter;
	}

	private static String getVertexId(final PreferenceScreenWithHost preferenceScreenWithHost) {
		return preferenceScreenWithHost
				.preferenceScreen
				.toString()
				.concat("_")
				.concat(Integer.toHexString(preferenceScreenWithHost.preferenceScreen.hashCode()))
				.replace(' ', '_');
	}

	private static Map<String, Attribute> getVertexAttribute(final PreferenceScreenWithHost preferenceScreenWithHost) {
		return ImmutableMap
				.<String, Attribute>builder()
				.put(
						"label",
						DefaultAttribute.createAttribute(getLabel(preferenceScreenWithHost.preferenceScreen)))
				.put("shape", DefaultAttribute.createAttribute("box"))
				.build();
	}

	private static String getLabel(final PreferenceScreen preferenceScreen) {
		return preferenceScreen.toString() + "\\n\\n" + getPreferences(preferenceScreen);
	}

	private static String getPreferences(final PreferenceScreen preferenceScreen) {
		return Preferences
				.getAllChildren(preferenceScreen)
				.stream()
				.map(Preference::toString)
				.collect(Collectors.joining("\\n"));
	}

	private static Map<String, Attribute> getEdgeAttribute(final PreferenceEdge preferenceEdge) {
		return ImmutableMap.of(
				"label",
				DefaultAttribute.createAttribute(preferenceEdge.preference.getTitle().toString()));
	}
}
