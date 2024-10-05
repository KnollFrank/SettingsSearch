package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

class PreferenceScreenWithHostClassPOJOConverter {

    public static PreferenceScreenWithHostClassPOJO convert2POJO(final PreferenceScreenWithHostClass preferenceScreenWithHostClass) {
        return new PreferenceScreenWithHostClassPOJO(
                SearchablePreferenceScreenPOJOConverter.convert2POJO(preferenceScreenWithHostClass.preferenceScreen()),
                preferenceScreenWithHostClass.host());
    }
}
