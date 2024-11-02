package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferencePathByPreference {

    // remove class and inline methods getPreferencePathByPreference() and convertPojoKeys2EntityKeys()
    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        return convertPojoKeys2EntityKeys(preferencePathByPreference, pojoEntityMap);
    }

    private static Map<Preference, PreferencePath> convertPojoKeys2EntityKeys(
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return preferencePathByPreference
                .entrySet()
                .stream()
                .filter(entry -> pojoEntityMap.containsKey(entry.getKey()))
                .collect(
                        Collectors.toMap(
                                entry -> pojoEntityMap.get(entry.getKey()),
                                Entry::getValue));
    }
}
