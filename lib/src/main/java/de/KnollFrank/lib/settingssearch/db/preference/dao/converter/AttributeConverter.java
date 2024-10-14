package de.KnollFrank.lib.settingssearch.db.preference.dao.converter;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

class AttributeConverter {

    public static Attribute json2Attribute(final String json) {
        return DefaultAttribute.createAttribute(json);
    }

    public static String attribute2JSON(final Attribute attribute) {
        return attribute.getValue();
    }
}
