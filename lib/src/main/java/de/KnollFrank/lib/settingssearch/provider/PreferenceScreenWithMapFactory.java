package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class PreferenceScreenWithMapFactory {

    public static PreferenceScreenWithMap getPreferenceScreenWithMap(
            final List<PreferenceScreenWithHostClassPOJOWithMap> preferenceScreens,
            final PreferenceManager preferenceManager) {
        return SearchablePreferenceScreenFromPOJOConverter.convertFromPOJO(
                concat(withoutHostAndMap(preferenceScreens)),
                preferenceManager);
    }

    private static List<SearchablePreferenceScreenPOJO> withoutHostAndMap(
            final List<PreferenceScreenWithHostClassPOJOWithMap> preferenceScreens) {
        return preferenceScreens
                .stream()
                .map(preferenceScreenWithHostClassPOJOWithMap ->
                        preferenceScreenWithHostClassPOJOWithMap
                                .preferenceScreenWithHostClass()
                                .preferenceScreen())
                .collect(Collectors.toList());
    }

    private static SearchablePreferenceScreenPOJO concat(final List<SearchablePreferenceScreenPOJO> screens) {
        return new SearchablePreferenceScreenPOJO(
                "title of merged screen",
                "summary of merged screen",
                concatChildren(screens));
    }

    private static List<SearchablePreferencePOJO> concatChildren(final List<SearchablePreferenceScreenPOJO> screens) {
        return Lists.concat(
                screens
                        .stream()
                        .map(SearchablePreferenceScreenPOJO::children)
                        .collect(Collectors.toList()));
    }
}
