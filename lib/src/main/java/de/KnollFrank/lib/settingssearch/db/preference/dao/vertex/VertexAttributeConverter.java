package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.AttributeConverter.attribute2JSON;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.AttributeConverter.json2Attribute;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexConverter.json2Vertex;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.vertex.VertexConverter.vertex2JSON;

import org.jgrapht.nio.Attribute;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

class VertexAttributeConverter {

    public static Attribute vertex2Attribute(final PreferenceScreenWithHostClassPOJO vertex) {
        return json2Attribute(vertex2JSON(vertex));
    }

    public static PreferenceScreenWithHostClassPOJO attribute2Vertex(final Attribute attribute) {
        return json2Vertex(attribute2JSON(attribute));
    }
}
