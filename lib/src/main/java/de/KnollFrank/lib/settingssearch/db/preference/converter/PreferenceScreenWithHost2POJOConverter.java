package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceScreenWithHost2POJOConverter {

    public static SearchablePreferenceScreenWithMap convert2POJO(final PreferenceScreenWithHost preferenceScreenWithHost,
                                                                 final String id,
                                                                 final Optional<String> parentId,
                                                                 final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                                 final Optional<SearchablePreference> predecessorOfPreferenceScreen) {
        return SearchablePreferenceScreen2POJOConverter.convert2POJO(
                id,
                parentId,
                preferenceScreenWithHost.preferenceScreen(),
                preferenceScreenWithHost.host(),
                preference2SearchablePreferenceConverter,
                predecessorOfPreferenceScreen);
    }
}
