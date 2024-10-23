package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

class PreferenceScreensMerger {

    public static PreferenceScreenWithMap mergePreferenceScreens(
            final List<PreferenceScreenWithHostClassPOJO> preferenceScreens,
            final PreferenceManager preferenceManager) {
        return SearchablePreferenceScreenFromPOJOConverter.convertFromPOJO(
                concat(withoutHost(preferenceScreens)),
                preferenceManager);
    }

    private static List<SearchablePreferenceScreenPOJO> withoutHost(
            final List<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return preferenceScreens
                .stream()
                .map(PreferenceScreenWithHostClassPOJO::preferenceScreen)
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
