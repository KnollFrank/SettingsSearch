package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class SearchablePreferenceFromPOJOConverter {

    public static SearchablePreference convertFromPOJO(final SearchablePreferencePOJO searchablePreferencePOJO,
                                                       final Context context) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        context,
                        SearchableInfoAttributeConverter.convertFromPOJO(searchablePreferencePOJO.searchableInfo()),
                        Optional.of(searchablePreferencePOJO));
        searchablePreference.setKey(searchablePreferencePOJO.key());
        // FK-TODO: handle correctly: searchablePreference.setIcon(searchablePreferencePOJO.iconResId());
        searchablePreference.setLayoutResource(searchablePreferencePOJO.layoutResId());
        searchablePreference.setSummary(searchablePreferencePOJO.summary());
        searchablePreference.setTitle(searchablePreferencePOJO.title());
        searchablePreference.setWidgetLayoutResource(searchablePreferencePOJO.widgetLayoutResId());
        searchablePreference.setFragment(searchablePreferencePOJO.fragment());
        searchablePreference.setVisible(searchablePreferencePOJO.visible());
        searchablePreference.getExtras().putAll(searchablePreferencePOJO.extras());
        return searchablePreference;
    }

    public static List<SearchablePreference> convertFromPOJOs(final List<SearchablePreferencePOJO> searchablePreferencePOJOs,
                                                              final Context context) {
        return searchablePreferencePOJOs
                .stream()
                .map(searchablePreferencePOJO -> convertFromPOJO(searchablePreferencePOJO, context))
                .collect(Collectors.toList());
    }
}
