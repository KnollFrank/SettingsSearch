package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;

public class PreferencePathPOJOConverter {

    public static PreferencePathPOJO convert2POJO(final PreferencePath preferencePath) {
        return new PreferencePathPOJO(
                SearchablePreferencePOJOConverter.convert2POJOs(
                        SearchablePreferencePOJOConverter.cast(
                                preferencePath.preferences())));
    }
}
