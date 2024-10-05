package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;

class PreferencePath2POJOConverter {

    public static PreferencePathPOJO convert2POJO(final PreferencePath preferencePath) {
        return new PreferencePathPOJO(
                SearchablePreference2POJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(preferencePath.preferences())));
    }
}
