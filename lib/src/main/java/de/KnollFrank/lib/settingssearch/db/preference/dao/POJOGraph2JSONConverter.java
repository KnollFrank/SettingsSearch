package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;
import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.json.JSONExporter;

import java.io.IOException;
import java.io.Writer;

import de.KnollFrank.lib.settingssearch.db.preference.dao.converter.EdgeAttributeMapConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.converter.VertexAttributeMapConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

// FK-TODO: remove?
class POJOGraph2JSONConverter {

    public static void pojoGraph2JSON(final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
                                      final Writer writer) {
        getJSONExporter().exportGraph(pojoGraph, writer);
        closeSilently(writer);
    }

    private static JSONExporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getJSONExporter() {
        final JSONExporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> exporter = new JSONExporter<>();
        exporter.setVertexIdProvider(new IntegerIdProvider<>(1));
        exporter.setVertexAttributeProvider(VertexAttributeMapConverter::vertex2AttributeMap);
        exporter.setEdgeAttributeProvider(EdgeAttributeMapConverter::edge2AttributeMap);
        return exporter;
    }

    private static void closeSilently(final Writer writer) {
        try {
            writer.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
