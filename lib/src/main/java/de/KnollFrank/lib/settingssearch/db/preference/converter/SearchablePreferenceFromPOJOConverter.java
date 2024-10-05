package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreferenceFromPOJOConverter {

    public static SearchablePreference convertFromPOJO(final SearchablePreferencePOJO searchablePreferencePOJO,
                                                       final Context context) {
        return new SearchablePreference(
                context,
                SearchableInfoAttributeConverter.convertFromPOJO(searchablePreferencePOJO.searchableInfo()));
    }

    public static List<SearchablePreference> convertFromPOJOs(final List<SearchablePreferencePOJO> searchablePreferencePOJOs,
                                                              final Context context) {
        return searchablePreferencePOJOs
                .stream()
                .map(searchablePreferencePOJO -> convertFromPOJO(searchablePreferencePOJO, context))
                .collect(Collectors.toList());
    }
}
