package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;

// FK-TODO: use @Embedded
// FK-TODO: add unit test
public class PreferenceFragmentClassOfActivityConverter implements Converter<PreferenceFragmentClassOfActivity, String> {

    private static final String SEPARATOR = "@@@";
    private final ClassConverter<PreferenceFragmentCompat> preferenceFragmentCompatClassConverter = new ClassConverter<>();
    private final ClassConverter<Activity> activityClassConverter = new ClassConverter<>();
    private final PersistableBundleConverter bundleConverter = new PersistableBundleConverter();

    @TypeConverter
    @Override
    public String convertForward(final PreferenceFragmentClassOfActivity preferenceFragmentClassOfActivity) {
        return preferenceFragmentCompatClassConverter.convertForward(preferenceFragmentClassOfActivity.preferenceFragmentClass()) +
                SEPARATOR +
                activityClassConverter.convertForward(preferenceFragmentClassOfActivity.activityOfPreferenceFragment().activity()) +
                SEPARATOR +
                bundleConverter.convertForward(preferenceFragmentClassOfActivity.activityOfPreferenceFragment().arguments());
    }

    @TypeConverter
    @Override
    public PreferenceFragmentClassOfActivity convertBackward(final String value) {
        final String[] parts = value.split(SEPARATOR, -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid value for PreferenceFragmentClassOfActivityConverter: " + value);
        }
        return new PreferenceFragmentClassOfActivity(
                preferenceFragmentCompatClassConverter.convertBackward(parts[0]),
                new ActivityDescription(
                        activityClassConverter.convertBackward(parts[1]),
                        bundleConverter.convertBackward(parts[2])));
    }
}