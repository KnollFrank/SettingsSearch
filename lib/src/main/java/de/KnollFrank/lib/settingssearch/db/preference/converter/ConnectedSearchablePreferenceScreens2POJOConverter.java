package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class ConnectedSearchablePreferenceScreens2POJOConverter {

    public static ConnectedSearchablePreferenceScreensPOJO convert2POJO(final ConnectedSearchablePreferenceScreens connectedSearchablePreferenceScreens) {
        return new ConnectedSearchablePreferenceScreensPOJO(
                PreferenceScreenWithHostClass2POJOConverter.convert2POJOs(connectedSearchablePreferenceScreens.connectedSearchablePreferenceScreens()),
                convert2POJO(connectedSearchablePreferenceScreens.preferencePathByPreference()));
    }

    private static Map<SearchablePreferencePOJO, PreferencePathPOJO> convert2POJO(final Map<Preference, PreferencePath> preferencePreferencePathMap) {
        return preferencePreferencePathMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                preferencePreferencePathEntry -> SearchablePreference2POJOConverter.convert2POJO((SearchablePreference) preferencePreferencePathEntry.getKey()),
                                preferencePreferencePathEntry -> PreferencePath2POJOConverter.convert2POJO(preferencePreferencePathEntry.getValue())));
    }
}
