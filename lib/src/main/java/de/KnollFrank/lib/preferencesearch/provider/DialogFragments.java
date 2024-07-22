package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import java.util.Optional;
import java.util.function.Function;

class DialogFragments {

    private final FragmentManager fragmentManager;
    private final Function<FragmentManager, Fragment> fragmentSupplier;

    public DialogFragments(final FragmentManager fragmentManager, final Function<FragmentManager, Fragment> fragmentSupplier) {
        this.fragmentManager = fragmentManager;
        this.fragmentSupplier = fragmentSupplier;
    }

    public Optional<String> getStringFromDialogFragment(
            final Preference preference,
            final Function<Fragment, Optional<String>> getString) {
        final Optional<Fragment> fragment = openFragment(preference);
        final Optional<String> searchableInfo = fragment.flatMap(getString);
        fragment.ifPresent(this::closeFragment);
        return searchableInfo;
    }

    private Optional<Fragment> openFragment(final Preference preference) {
        preference.performClick();
        fragmentManager.executePendingTransactions();
        return Optional.ofNullable(fragmentSupplier.apply(fragmentManager));
    }

    private void closeFragment(final Fragment fragment) {
        if (fragment instanceof final DialogFragment dialogFragment) {
            dialogFragment.dismiss(); // FK-TODO: in dismiss() wird leider kein commitNow() sondern ein commit() aufgerufen
        } else {
            fragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commitNow();
        }
    }
}
