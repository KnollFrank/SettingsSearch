package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class PreferenceScreenWithHostClass2POJOConverter {

    // FK-TODO: rename
    public record PreferenceScreenWithHostClassWithMap(
            SearchablePreferenceScreen searchablePreferenceScreen,
            BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public static PreferenceScreenWithHostClassWithMap convert2POJO(final PreferenceScreenWithHost preferenceScreenWithHost,
                                                                    final int id,
                                                                    final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap =
                SearchablePreferenceScreen2POJOConverter.convert2POJO(
                        id,
                        preferenceScreenWithHost.preferenceScreen(),
                        preferenceScreenWithHost.host(),
                        preference2SearchablePreferenceConverter);
        return new PreferenceScreenWithHostClassWithMap(
                searchablePreferenceScreenWithMap.searchablePreferenceScreen(),
                searchablePreferenceScreenWithMap.pojoEntityMap());
    }
}
