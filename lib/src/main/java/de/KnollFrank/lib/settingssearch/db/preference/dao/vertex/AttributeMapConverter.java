package de.KnollFrank.lib.settingssearch.db.preference.dao.vertex;

import org.jgrapht.nio.Attribute;

import java.util.Map;

class AttributeMapConverter {

    private static final String PREFERENCE_SCREEN_WITH_HOST_CLASS = "preferenceScreenWithHostClass";

    public static Map<String, Attribute> attribute2Map(final Attribute attribute) {
        return Map.of(PREFERENCE_SCREEN_WITH_HOST_CLASS, attribute);
    }

    public static Attribute map2Attribute(final Map<String, Attribute> map) {
        return map.get(PREFERENCE_SCREEN_WITH_HOST_CLASS);
    }
}
