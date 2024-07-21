package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;

import java.util.Optional;
import java.util.function.Function;

class DialogFragments {

    private final FragmentManager fragmentManager;
    private final String tagOfDialogFragment;

    public DialogFragments(final FragmentManager fragmentManager, final String tagOfDialogFragment) {
        this.fragmentManager = fragmentManager;
        this.tagOfDialogFragment = tagOfDialogFragment;
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
        // prevent NullPointerException
        if (preference instanceof DropDownPreference) {
            return Optional.empty();
        }
        preference.performClick();
        fragmentManager.executePendingTransactions();
        return Optional.of((DialogFragment) fragmentManager.findFragmentByTag(tagOfDialogFragment));
    }
}
