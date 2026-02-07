package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;

// FK-TODO: use @Embedded
public class PreferenceFragmentClassOfActivityConverter implements Converter<FragmentClassOfActivity<? extends PreferenceFragmentCompat>, String> {

    private static final String SEPARATOR = "@@@";
    private final ClassConverter<PreferenceFragmentCompat> preferenceFragmentCompatClassConverter = new ClassConverter<>();
    private final ClassConverter<Activity> activityClassConverter = new ClassConverter<>();
    private final PersistableBundleConverter bundleConverter = new PersistableBundleConverter();

    @TypeConverter
    @Override
    public String convertForward(final FragmentClassOfActivity<? extends PreferenceFragmentCompat> preferenceFragmentClassOfActivity) {
        return preferenceFragmentCompatClassConverter.convertForward(preferenceFragmentClassOfActivity.fragment()) +
                SEPARATOR +
                activityClassConverter.convertForward(preferenceFragmentClassOfActivity.activityOfFragment().activity()) +
                SEPARATOR +
                bundleConverter.convertForward(preferenceFragmentClassOfActivity.activityOfFragment().arguments());
    }

    @TypeConverter
    @Override
    public FragmentClassOfActivity<? extends PreferenceFragmentCompat> convertBackward(final String value) {
        final String[] parts = value.split(SEPARATOR, -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid value for PreferenceFragmentClassOfActivityConverter: " + value);
        }
        return new FragmentClassOfActivity<>(
                preferenceFragmentCompatClassConverter.convertBackward(parts[0]),
                new ActivityDescription(
                        activityClassConverter.convertBackward(parts[1]),
                        bundleConverter.convertBackward(parts[2])));
    }
}