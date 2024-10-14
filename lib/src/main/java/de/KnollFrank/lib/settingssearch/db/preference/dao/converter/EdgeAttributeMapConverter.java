package de.KnollFrank.lib.settingssearch.db.preference.dao.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.AttributeConverter.attribute2JSON;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.AttributeConverter.json2Attribute;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.VertexConverter.entity2JSON;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.VertexConverter.json2Entity;

import org.jgrapht.nio.Attribute;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class EdgeAttributeMapConverter {

    private static final AttributeMapConverter attributeMapConverter = new AttributeMapConverter("searchablePreference");

    public static SearchablePreferencePOJOEdge attributeMap2Edge(final Map<String, Attribute> attributeMap) {
        final Attribute attribute = attributeMapConverter.attributeMap2Attribute(attributeMap);
        final String json = attribute2JSON(attribute);
        final SearchablePreferencePOJO preference = json2Entity(json, SearchablePreferencePOJO.class);
        final SearchablePreferencePOJOEdge edge = new SearchablePreferencePOJOEdge(preference);
        return edge;
    }

    public static Map<String, Attribute> edge2AttributeMap(final SearchablePreferencePOJOEdge edge) {
        final SearchablePreferencePOJO preference = edge.preference;
        final String json = entity2JSON(preference);
        final Attribute attribute = json2Attribute(json);
        final Map<String, Attribute> attributeMap = attributeMapConverter.attribute2AttributeMap(attribute);
        return attributeMap;
    }
}
