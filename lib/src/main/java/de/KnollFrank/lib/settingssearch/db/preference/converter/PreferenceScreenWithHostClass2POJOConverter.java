package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceScreenWithHostClass2POJOConverter {

    public record PreferenceScreenWithHostClassPOJOWithMap(
            PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClass,
            BiMap<SearchablePreference, Preference> pojoEntityMap) {
    }

    public static PreferenceScreenWithHostClassPOJOWithMap convert2POJO(final PreferenceScreenWithHost preferenceScreenWithHost,
                                                                        final int id,
                                                                        final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter) {
        final SearchablePreferenceScreenPOJOWithMap searchablePreferenceScreenPOJOWithMap =
                SearchablePreferenceScreen2POJOConverter.convert2POJO(
                        preferenceScreenWithHost.preferenceScreen(),
                        preferenceScreenWithHost.host(),
                        preference2SearchablePreferencePOJOConverter);
        return new PreferenceScreenWithHostClassPOJOWithMap(
                new PreferenceScreenWithHostClassPOJO(
                        id,
                        searchablePreferenceScreenPOJOWithMap.searchablePreferenceScreen(),
                        preferenceScreenWithHost.host().getClass()),
                searchablePreferenceScreenPOJOWithMap.pojoEntityMap());
    }
}
