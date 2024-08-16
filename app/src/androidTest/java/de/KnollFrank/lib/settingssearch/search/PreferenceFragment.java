package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.preference.Preference;

import java.util.function.Function;

public class PreferenceFragment extends PreferenceFragmentTemplateWithSinglePreference {

    private final Function<Context, Preference> preferenceFactory;

    public PreferenceFragment(final Function<Context, Preference> preferenceFactory) {
        this.preferenceFactory = preferenceFactory;
    }

    public static PreferenceFragment fromSinglePreference(final Function<Context, Preference> preferenceFactory) {
        return new PreferenceFragment(preferenceFactory);
    }

    @Override
    protected Preference createPreference(final Context context) {
        return preferenceFactory.apply(context);
    }
}
