package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.json.JSONExporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

class POJOGraph2JSONConverter {

    public static void pojoGraph2JSON(final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
                                      final Writer writer) {
        getJSONExporter().exportGraph(pojoGraph, writer);
        closeSilently(writer);
    }

    private static JSONExporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getJSONExporter() {
        final JSONExporter<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> exporter = new JSONExporter<>();
        exporter.setVertexIdProvider(new IntegerIdProvider<>(1));
        exporter.setVertexAttributeProvider(VertexAttribute::vertex2Attributes);
        exporter.setEdgeAttributeProvider(POJOGraph2JSONConverter::getEdgeAttribute);
        return exporter;
    }

    private static Map<String, Attribute> getEdgeAttribute(final SearchablePreferencePOJOEdge searchablePreferencePOJOEdge) {
        return Map.of(
                // FK-TODO: DRY with JSON2POJOGraphConverter
                "searchablePreference",
                DefaultAttribute.createAttribute(convert2JSON(searchablePreferencePOJOEdge.preference)));
    }

    private static String convert2JSON(final SearchablePreferencePOJO preference) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonDAO.persist(preference, outputStream);
        return IOUtils.toString(outputStream);
    }

    private static void closeSilently(final Writer writer) {
        try {
            writer.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
