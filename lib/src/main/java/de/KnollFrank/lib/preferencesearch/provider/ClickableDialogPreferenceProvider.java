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
        return new ClickableDialogPreferenceProvider(instantiateAndInitialize(classOfPreferenceFragment, instantiateAndInitializeFragment));
    }

    public ClickableDialogPreferenceProvider(final PreferenceFragmentCompat preferenceFragment) {
        this.preferenceFragment = preferenceFragment;
    }

    public Optional<DialogPreference> getClickableDialogPreference(final DialogPreference dialogPreference) {
        return rereadDialogPreference(Optional.ofNullable(dialogPreference.getKey()));
    }

    private Optional<DialogPreference> rereadDialogPreference(final Optional<String> key) {
        return key.map(preferenceFragment::findPreference);
    }

    private static PreferenceFragmentCompat instantiateAndInitialize(final Class<? extends PreferenceFragmentCompat> classOfPreferenceFragment,
                                                                     final Function<String, Fragment> instantiateAndInitializeFragment) {
        final PreferenceFragmentCompat preferenceFragment =
                (PreferenceFragmentCompat) instantiateAndInitializeFragment.apply(classOfPreferenceFragment.getName());
        preferenceFragment.onStart();
        return preferenceFragment;
    }
}
