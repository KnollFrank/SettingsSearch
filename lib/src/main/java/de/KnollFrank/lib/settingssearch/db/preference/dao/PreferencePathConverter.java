package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class PreferencePathConverter {

    public static List<Integer> addIds(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(SearchablePreferencePOJO::id)
                .collect(Collectors.toList());
    }

    public static PreferencePath removeIds(final List<Integer> ids,
                                           final Map<Integer, SearchablePreferencePOJO> preferenceById) {
        return new PreferencePath(
                ids
                        .stream()
                        .map(preferenceById::get)
                        .collect(Collectors.toList()));
    }
}