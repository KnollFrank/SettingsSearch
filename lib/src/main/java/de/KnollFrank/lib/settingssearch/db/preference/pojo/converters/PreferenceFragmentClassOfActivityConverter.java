package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;
import de.KnollFrank.lib.settingssearch.common.Classes;

// FK-TODO: use @Embedded
// FK-TODO: add unit test
public class PreferenceFragmentClassOfActivityConverter implements Converter<PreferenceFragmentClassOfActivity, String> {

    private static final String SEPARATOR = "@@@";

    @TypeConverter
    @Override
    public String convertForward(final PreferenceFragmentClassOfActivity preferenceFragmentClassOfActivity) {
        return preferenceFragmentClassOfActivity.preferenceFragmentClass().getName() +
                SEPARATOR +
                preferenceFragmentClassOfActivity.activityOfPreferenceFragment().activity().getName();
    }

    @TypeConverter
    @Override
    public PreferenceFragmentClassOfActivity convertBackward(final String value) {
        final String[] parts = value.split(SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid value for PreferenceFragmentClassOfActivityConverter: " + value);
        }
        final Class<? extends PreferenceFragmentCompat> fragmentClass = Classes.getClass(parts[0]);
        final Class<? extends Activity> activityClass = Classes.getClass(parts[1]);
        return new PreferenceFragmentClassOfActivity(fragmentClass, new ActivityDescription(activityClass));
    }
}
