package de.KnollFrank.lib.settingssearch.db.preference.converter;

import com.google.common.collect.BiMap;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferenceScreenWithHostClass2POJOConverter {

    public record PreferenceScreenWithHostClassPOJOWithMap(
            PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClass,
            BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
    }

    public static PreferenceScreenWithHostClassPOJOWithMap convert2POJO(final PreferenceScreenWithHostClass preferenceScreenWithHostClass,
                                                                        final int id,
                                                                        final IdGenerator idGenerator) {
        final SearchablePreferenceScreenPOJOWithMap searchablePreferenceScreenPOJOWithMap =
                SearchablePreferenceScreen2POJOConverter.convert2POJO(
                        preferenceScreenWithHostClass.preferenceScreen(),
                        idGenerator);
        return new PreferenceScreenWithHostClassPOJOWithMap(
                new PreferenceScreenWithHostClassPOJO(
                        id,
                        searchablePreferenceScreenPOJOWithMap.searchablePreferenceScreen(),
                        preferenceScreenWithHostClass.host()),
                searchablePreferenceScreenPOJOWithMap.pojoEntityMap());
    }
}
