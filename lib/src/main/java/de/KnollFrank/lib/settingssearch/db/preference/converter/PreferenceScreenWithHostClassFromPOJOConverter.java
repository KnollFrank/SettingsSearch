package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;

import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferenceScreenWithHostClassFromPOJOConverter {

    public record PreferenceScreenWithHostClassWithMap(
            PreferenceScreenWithHostClass preferenceScreenWithHostClass,
            BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

    public static PreferenceScreenWithHostClassWithMap convertFromPOJO(final PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClassPOJO,
                                                                       final PreferenceManager preferenceManager) {
        final PreferenceScreenWithMap preferenceScreenWithMap =
                SearchablePreferenceScreenFromPOJOConverter.convertFromPOJO(
                        preferenceScreenWithHostClassPOJO.preferenceScreen(),
                        preferenceManager);
        return new PreferenceScreenWithHostClassWithMap(
                new PreferenceScreenWithHostClass(
                        preferenceScreenWithMap.preferenceScreen(),
                        preferenceScreenWithHostClassPOJO.host()),
                preferenceScreenWithMap.pojoEntityMap());
    }
}
