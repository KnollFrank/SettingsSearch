package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import org.jgrapht.nio.Attribute;

import java.util.Map;

class AttributeMapConverter {

    private static final String PREFERENCE_SCREEN_WITH_HOST_CLASS = "preferenceScreenWithHostClass";

    public static Map<String, Attribute> attribute2AttributeMap(final Attribute attribute) {
        return Map.of(PREFERENCE_SCREEN_WITH_HOST_CLASS, attribute);
    }

    public static Attribute attributeMap2Attribute(final Map<String, Attribute> attributeMap) {
        return attributeMap.get(PREFERENCE_SCREEN_WITH_HOST_CLASS);
    }
}
