package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;

class PreferencePathPOJOConverter {

    public static PreferencePathPOJO convert2POJO(final PreferencePath preferencePath) {
        return new PreferencePathPOJO(
                SearchablePreferencePOJOConverter.convert2POJOs(
                        SearchablePreferenceCaster.cast(
                                preferencePath.preferences())));
    }
}
