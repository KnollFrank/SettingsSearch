package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

class PreferenceScreenWithHostClass2POJOConverter {

    public static PreferenceScreenWithHostClassPOJO convert2POJO(final PreferenceScreenWithHostClass preferenceScreenWithHostClass) {
        return new PreferenceScreenWithHostClassPOJO(
                SearchablePreferenceScreen2POJOConverter.convert2POJO(preferenceScreenWithHostClass.preferenceScreen()),
                preferenceScreenWithHostClass.host());
    }

    public static Set<PreferenceScreenWithHostClassPOJO> convert2POJOs(final Set<PreferenceScreenWithHostClass> preferenceScreenWithHostClasses) {
        return preferenceScreenWithHostClasses
                .stream()
                .map(PreferenceScreenWithHostClass2POJOConverter::convert2POJO)
                .collect(Collectors.toSet());
    }
}
