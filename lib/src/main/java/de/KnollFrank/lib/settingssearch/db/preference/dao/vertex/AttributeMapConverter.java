package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import org.jgrapht.nio.Attribute;

import java.util.Map;

public class AttributeMapConverter {

    private final String key;

    public AttributeMapConverter(final String key) {
        this.key = key;
    }

    public Map<String, Attribute> attribute2AttributeMap(final Attribute attribute) {
        return Map.of(key, attribute);
    }

    public Attribute attributeMap2Attribute(final Map<String, Attribute> attributeMap) {
        return attributeMap.get(key);
    }
}
