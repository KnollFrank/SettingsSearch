package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;

class PreferencePathFromPOJOConverter {

    public static PreferencePath convertFromPOJO(final PreferencePathPOJO preferencePathPOJO,
                                                 final Context context) {
        return new PreferencePath(
                SearchablePreferenceFromPOJOConverter.convertFromPOJOs(
                        preferencePathPOJO.preferences(),
                        context));
    }
}
