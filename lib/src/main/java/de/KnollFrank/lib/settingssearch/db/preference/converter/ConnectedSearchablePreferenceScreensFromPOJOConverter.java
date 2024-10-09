package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

// FK-TODO: remove class?
public class ConnectedSearchablePreferenceScreensFromPOJOConverter {

    public static ConnectedSearchablePreferenceScreens convertFromPOJO(final ConnectedSearchablePreferenceScreensPOJO connectedSearchablePreferenceScreensPOJO,
                                                                       final PreferenceManager preferenceManager) {
        return new ConnectedSearchablePreferenceScreens(
                PreferenceScreenWithHostClassFromPOJOConverter.convertFromPOJOs(connectedSearchablePreferenceScreensPOJO.connectedSearchablePreferenceScreens(), preferenceManager),
                convertFromPOJO(
                        // FK-FIXME: Fehler: jeder Key MUSS (!!!) == einem der SearchablePreference aus dem PreferenceScreen der vorherigen Zeile sein.
                        connectedSearchablePreferenceScreensPOJO.preferencePathByPreference(),
                        preferenceManager.getContext()));
    }

    private static Map<Preference, PreferencePath> convertFromPOJO(final Map<SearchablePreferencePOJO, PreferencePathPOJO> searchablePreferencePOJOPreferencePathPOJOMap,
                                                                   final Context context) {
        return searchablePreferencePOJOPreferencePathPOJOMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                searchablePreferencePOJOPreferencePathPOJOEntry -> SearchablePreferenceFromPOJOConverter.convertFromPOJO(searchablePreferencePOJOPreferencePathPOJOEntry.getKey(), context),
                                searchablePreferencePOJOPreferencePathPOJOEntry -> PreferencePathFromPOJOConverter.convertFromPOJO(searchablePreferencePOJOPreferencePathPOJOEntry.getValue(), context)));
    }
}
