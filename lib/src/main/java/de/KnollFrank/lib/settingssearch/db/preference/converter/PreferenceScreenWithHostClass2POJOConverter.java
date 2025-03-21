package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceScreenWithHostClass2POJOConverter {

    public record PreferenceScreenWithHostClassWithMap(
            PreferenceScreenWithHostClass preferenceScreenWithHostClass,
            BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public static PreferenceScreenWithHostClassWithMap convert2POJO(final PreferenceScreenWithHost preferenceScreenWithHost,
                                                                    final int id,
                                                                    final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap =
                SearchablePreferenceScreen2POJOConverter.convert2POJO(
                        preferenceScreenWithHost.preferenceScreen(),
                        preferenceScreenWithHost.host(),
                        preference2SearchablePreferenceConverter);
        return new PreferenceScreenWithHostClassWithMap(
                new PreferenceScreenWithHostClass(
                        id,
                        searchablePreferenceScreenWithMap.searchablePreferenceScreen(),
                        preferenceScreenWithHost.host().getClass()),
                searchablePreferenceScreenWithMap.pojoEntityMap());
    }
}
