package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;

public class PreferenceFragmentTemplate extends PreferenceFragmentCompat {

    private final BiConsumer<PreferenceScreen, Context> addPreferences2Screen;

    public PreferenceFragmentTemplate(final BiConsumer<PreferenceScreen, Context> addPreferences2Screen) {
        this.addPreferences2Screen = addPreferences2Screen;
    }

    public PreferenceFragmentTemplate(final Function<Context, List<Preference>> preferencesProvider) {
        this((preferenceScreen, context) ->
                preferencesProvider
                        .apply(context)
                        .forEach(preferenceScreen::addPreference));
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(requireContext());
        screen.setTitle("screen title");
        screen.setSummary("screen summary");
        addPreferences2Screen.accept(screen, requireContext());
        setPreferenceScreen(screen);
    }

    @Override
    public void onDisplayPreferenceDialog(final @NonNull Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
