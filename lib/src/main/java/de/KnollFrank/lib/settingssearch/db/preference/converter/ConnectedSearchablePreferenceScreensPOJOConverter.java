package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class ConnectedSearchablePreferenceScreensPOJOConverter {

    public static ConnectedSearchablePreferenceScreensPOJO convert2POJO(final ConnectedSearchablePreferenceScreens connectedSearchablePreferenceScreens) {
        return new ConnectedSearchablePreferenceScreensPOJO(
                convert2POJO(connectedSearchablePreferenceScreens.connectedSearchablePreferenceScreens()),
                convert2POJO(connectedSearchablePreferenceScreens.preferencePathByPreference()));
    }

    private static Set<PreferenceScreenWithHostClassPOJO> convert2POJO(final Set<PreferenceScreenWithHostClass> preferenceScreenWithHostClasses) {
        return preferenceScreenWithHostClasses
                .stream()
                .map(PreferenceScreenWithHostClassPOJOConverter::convert2POJO)
                .collect(Collectors.toSet());
    }

    private static Map<SearchablePreferencePOJO, PreferencePathPOJO> convert2POJO(final Map<Preference, PreferencePath> preferencePreferencePathMap) {
        return preferencePreferencePathMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                preferencePreferencePathEntry -> SearchablePreferencePOJOConverter.convert2POJO((SearchablePreference) preferencePreferencePathEntry.getKey()),
                                preferencePreferencePathEntry -> PreferencePathPOJOConverter.convert2POJO(preferencePreferencePathEntry.getValue())));
    }
}
