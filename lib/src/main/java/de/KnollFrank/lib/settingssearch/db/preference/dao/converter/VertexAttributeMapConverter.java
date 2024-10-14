package de.KnollFrank.lib.settingssearch.db.preference.dao.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.AttributeConverter.attribute2JSON;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.AttributeConverter.json2Attribute;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.VertexConverter.entity2JSON;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.converter.VertexConverter.json2Entity;

import org.jgrapht.nio.Attribute;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class VertexAttributeMapConverter {

    private static final AttributeMapConverter attributeMapConverter = new AttributeMapConverter("preferenceScreenWithHostClass");

    public static Map<String, Attribute> vertex2AttributeMap(final PreferenceScreenWithHostClassPOJO vertex) {
        final String json = entity2JSON(vertex);
        final Attribute attribute = json2Attribute(json);
        final Map<String, Attribute> attributeMap = attributeMapConverter.attribute2AttributeMap(attribute);
        return attributeMap;
    }

    public static PreferenceScreenWithHostClassPOJO attributeMap2Vertex(final Map<String, Attribute> attributeMap) {
        final Attribute attribute = attributeMapConverter.attributeMap2Attribute(attributeMap);
        final String json = attribute2JSON(attribute);
        final PreferenceScreenWithHostClassPOJO vertex = json2Entity(json, PreferenceScreenWithHostClassPOJO.class);
        return vertex;
    }
}
