package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.function.Function;

public class PreferenceFragmentFactory {

    private PreferenceFragmentFactory() {
    }

    public static PreferenceFragmentCompat fromSinglePreference(final Function<Context, Preference> preferenceFactory) {
        return new PreferenceFragmentTemplate(context -> List.of(preferenceFactory.apply(context)));
    }
}
