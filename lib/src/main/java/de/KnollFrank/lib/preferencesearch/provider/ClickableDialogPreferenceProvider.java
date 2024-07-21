package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.base.Function;

import java.util.Optional;

class ClickableDialogPreferenceProvider {

    private final PreferenceFragmentCompat preferenceFragment;

    public static ClickableDialogPreferenceProvider create(final Class<? extends PreferenceFragmentCompat> classOfPreferenceFragment,
                                                           final Function<String, Fragment> instantiateAndInitializeFragment) {
        return new ClickableDialogPreferenceProvider((PreferenceFragmentCompat) instantiateAndInitializeFragment.apply(classOfPreferenceFragment.getName()));
    }

    public ClickableDialogPreferenceProvider(final PreferenceFragmentCompat preferenceFragment) {
        this.preferenceFragment = preferenceFragment;
    }

    public Optional<DialogPreference> asClickableDialogPreference(final DialogPreference dialogPreference) {
        return rereadDialogPreference(Optional.ofNullable(dialogPreference.getKey()));
    }

    private Optional<DialogPreference> rereadDialogPreference(final Optional<String> key) {
        return key.map(preferenceFragment::findPreference);
    }
}
