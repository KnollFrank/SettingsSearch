package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class PreferenceScreenWithHostClass2POJOConverter {

    public static PreferenceScreenWithHostClassPOJO convert2POJO(final PreferenceScreenWithHostClass preferenceScreenWithHostClass,
                                                                 final int id,
                                                                 final IdGenerator idGenerator) {
        return new PreferenceScreenWithHostClassPOJO(
                id,
                SearchablePreferenceScreen2POJOConverter.convert2POJO(
                        preferenceScreenWithHostClass.preferenceScreen(),
                        idGenerator),
                preferenceScreenWithHostClass.host());
    }
}
