package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

public class PreferenceScreenWithHost2POJOConverter {

    public static SearchablePreferenceScreenWithMap convert2POJO(final PreferenceScreenWithHost preferenceScreenWithHost,
                                                                 final int id,
                                                                 final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        return SearchablePreferenceScreen2POJOConverter.convert2POJO(
                id,
                preferenceScreenWithHost.preferenceScreen(),
                preferenceScreenWithHost.host(),
                preference2SearchablePreferenceConverter);
    }
}
