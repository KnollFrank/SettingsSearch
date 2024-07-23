package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentInitializer;

class DialogFragments {

    private final FragmentInitializer fragmentInitializer;
    // FK-TODO: remove fragmentManager
    private final FragmentManager fragmentManager;
    private final Function<FragmentManager, Fragment> fragmentSupplier;

    public DialogFragments(final FragmentInitializer fragmentInitializer,
                           final FragmentManager fragmentManager,
                           final Function<FragmentManager, Fragment> fragmentSupplier) {
        this.fragmentInitializer = fragmentInitializer;
        this.fragmentManager = fragmentManager;
        this.fragmentSupplier = fragmentSupplier;
    }

    public Optional<String> getStringFromDialogFragment(final Function<Fragment, Optional<String>> getString) {
        final Fragment preferenceDialog = openPreferenceDialog();
        final Optional<String> searchableInfo = getString.apply(preferenceDialog);
        closePreferenceDialog(preferenceDialog);
        return searchableInfo;
    }

    private Fragment openPreferenceDialog() {
        final Fragment preferenceDialog = fragmentSupplier.apply(fragmentManager);
        fragmentInitializer.initializePreferenceDialog(preferenceDialog);
        return preferenceDialog;
    }

    private void closePreferenceDialog(final Fragment preferenceDialog) {
        fragmentInitializer.removePreferenceDialog(preferenceDialog);
    }
}
