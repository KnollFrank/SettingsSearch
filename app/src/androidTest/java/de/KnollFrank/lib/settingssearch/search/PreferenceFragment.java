package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.preference.Preference;

import java.util.List;
import java.util.function.Function;

public class PreferenceFragment extends PreferenceFragmentTemplateWithPreferences {

    private final Function<Context, List<Preference>> preferencesFactory;

    public PreferenceFragment(final Function<Context, List<Preference>> preferencesFactory) {
        this.preferencesFactory = preferencesFactory;
    }

    public static PreferenceFragment fromSinglePreference(final Function<Context, Preference> preferenceFactory) {
        return new PreferenceFragment(context -> List.of(preferenceFactory.apply(context)));
    }

    @Override
    protected List<Preference> createPreferences(final Context context) {
        return preferencesFactory.apply(context);
    }
}
