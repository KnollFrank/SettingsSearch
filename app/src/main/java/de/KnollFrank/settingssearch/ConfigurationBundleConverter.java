package de.KnollFrank.settingssearch;

import android.os.PersistableBundle;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class ConfigurationBundleConverter implements Converter<Configuration, PersistableBundle> {

    private static final String ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE = "addPreferenceToPreferenceFragmentWithSinglePreference";
    private static final String SUMMARY_CHANGING_PREFERENCE = "summaryChangingPreference";

    @Override
    public PersistableBundle doForward(final Configuration configuration) {
        final PersistableBundle bundle = new PersistableBundle();
        bundle.putBoolean(
                ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE,
                configuration.addPreferenceToPreferenceFragmentWithSinglePreference());
        bundle.putBoolean(
                SUMMARY_CHANGING_PREFERENCE,
                configuration.summaryChangingPreference());
        return bundle;
    }

    @Override
    public Configuration doBackward(final PersistableBundle bundle) {
        return new Configuration(
                bundle.getBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE, false),
                bundle.getBoolean(SUMMARY_CHANGING_PREFERENCE, false));
    }
}
