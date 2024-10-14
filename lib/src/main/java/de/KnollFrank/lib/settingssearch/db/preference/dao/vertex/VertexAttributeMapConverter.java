package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.AttributeMapConverter.attribute2AttributeMap;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.AttributeMapConverter.attributeMap2Attribute;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeConverter.attribute2Vertex;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexAttributeConverter.vertex2Attribute;

import org.jgrapht.nio.Attribute;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class VertexAttributeMapConverter {

    public static Map<String, Attribute> vertex2AttributeMap(final PreferenceScreenWithHostClassPOJO vertex) {
        return attribute2AttributeMap(vertex2Attribute(vertex));
    }

    public static PreferenceScreenWithHostClassPOJO attributeMap2Vertex(final Map<String, Attribute> attributeMap) {
        return attribute2Vertex(attributeMap2Attribute(attributeMap));
    }
}
