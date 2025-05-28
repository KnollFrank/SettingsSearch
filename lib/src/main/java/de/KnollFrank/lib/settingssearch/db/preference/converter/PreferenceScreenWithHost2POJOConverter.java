package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceScreenWithHost2POJOConverter {

    public static SearchablePreferenceScreenWithMap convertPreferenceScreen(final PreferenceScreenWithHost preferenceScreenWithHost,
                                                                            final String id,
                                                                            final Optional<String> parentId,
                                                                            final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter,
                                                                            final Optional<SearchablePreference> predecessorOfPreferenceScreen) {
        return PreferenceScreen2SearchablePreferenceScreenConverter.convertPreferenceScreen(
                preferenceScreenWithHost.preferenceScreen(),
                preferenceScreenWithHost.host(),
                id,
                parentId,
                preference2SearchablePreferenceConverter,
                predecessorOfPreferenceScreen);
    }
}
