package de.KnollFrank.lib.preferencesearch.search;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.function.Function;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private final Function<Context, Preference> preferenceFactory;

    public PreferenceFragment(final Function<Context, Preference> preferenceFactory) {
        this.preferenceFactory = preferenceFactory;
    }

    public static PreferenceFragment fromSinglePreference(final Function<Context, Preference> preferenceFactory) {
        return new PreferenceFragment(preferenceFactory);
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        screen.addPreference(preferenceFactory.apply(context));
        setPreferenceScreen(screen);
    }
}
