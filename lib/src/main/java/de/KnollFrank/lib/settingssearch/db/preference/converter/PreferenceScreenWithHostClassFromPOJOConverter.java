package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceManager;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

class PreferenceScreenWithHostClassFromPOJOConverter {

    public static PreferenceScreenWithHostClass convertFromPOJO(final PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClassPOJO,
                                                                final PreferenceManager preferenceManager) {
        return new PreferenceScreenWithHostClass(
                SearchablePreferenceScreenFromPOJOConverter.convertFromPOJO(preferenceScreenWithHostClassPOJO.preferenceScreen(), preferenceManager),
                preferenceScreenWithHostClassPOJO.host());
    }

    public static Set<PreferenceScreenWithHostClass> convertFromPOJOs(final Set<PreferenceScreenWithHostClassPOJO> preferenceScreenWithHostClassPOJOs,
                                                                      final PreferenceManager preferenceManager) {
        return preferenceScreenWithHostClassPOJOs
                .stream()
                .map(preferenceScreenWithHostClassPOJO -> convertFromPOJO(preferenceScreenWithHostClassPOJO, preferenceManager))
                .collect(Collectors.toSet());
    }
}
