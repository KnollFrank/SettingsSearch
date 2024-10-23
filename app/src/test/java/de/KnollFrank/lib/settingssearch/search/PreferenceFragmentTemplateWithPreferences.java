package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;

import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;

public abstract class PreferenceFragmentTemplateWithPreferences extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        createPreferences(context).forEach(screen::addPreference);
        setPreferenceScreen(screen);
    }

    protected abstract List<Preference> createPreferences(Context context);

    @Override
    public void onDisplayPreferenceDialog(final @NonNull Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
