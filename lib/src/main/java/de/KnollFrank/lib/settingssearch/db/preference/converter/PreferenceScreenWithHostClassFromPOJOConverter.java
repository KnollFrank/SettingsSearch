package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.preference.PreferenceManager;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class PreferenceScreenWithHostClassFromPOJOConverter {

    public static PreferenceScreenWithHostClass convertFromPOJO(final PreferenceScreenWithHostClassPOJO preferenceScreenWithHostClassPOJO,
                                                                final PreferenceManager preferenceManager) {
        return new PreferenceScreenWithHostClass(
                SearchablePreferenceScreenFromPOJOConverter.convertFromPOJO(preferenceScreenWithHostClassPOJO.preferenceScreen(), preferenceManager),
                preferenceScreenWithHostClassPOJO.host());
    }
}
