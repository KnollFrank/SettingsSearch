package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import java.util.Optional;
import java.util.function.Function;

class DialogFragments {

    private final FragmentManager fragmentManager;
    private final Function<FragmentManager, DialogFragment> dialogFragmentSupplier;

    public DialogFragments(final FragmentManager fragmentManager, final Function<FragmentManager, DialogFragment> dialogFragmentSupplier) {
        this.fragmentManager = fragmentManager;
        this.dialogFragmentSupplier = dialogFragmentSupplier;
    }

    public Optional<String> getStringFromDialogFragment(
            final Preference preference,
            final Function<DialogFragment, Optional<String>> getString) {
        final Optional<DialogFragment> dialogFragment = getDialogFragment(preference);
        final Optional<String> searchableInfo = dialogFragment.flatMap(getString);
        dialogFragment.ifPresent(DialogFragment::dismiss);
        return searchableInfo;
    }

    private Optional<DialogFragment> getDialogFragment(final Preference preference) {
        preference.performClick();
        fragmentManager.executePendingTransactions();
        return Optional.ofNullable(dialogFragmentSupplier.apply(fragmentManager));
    }
}
