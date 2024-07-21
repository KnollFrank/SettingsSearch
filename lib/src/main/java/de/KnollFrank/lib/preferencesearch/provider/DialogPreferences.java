package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.DropDownPreference;

import java.util.Optional;
import java.util.function.Function;

class DialogPreferences {

    private final FragmentManager fragmentManager;
    private final String tagOfDialogFragment;

    public DialogPreferences(final FragmentManager fragmentManager, final String tagOfDialogFragment) {
        this.fragmentManager = fragmentManager;
        this.tagOfDialogFragment = tagOfDialogFragment;
    }

    public Optional<String> getStringFromDialogFragment(
            final DialogPreference dialogPreference,
            final Function<DialogFragment, Optional<String>> getString) {
        final Optional<DialogFragment> dialogFragment = getDialogFragment(dialogPreference);
        final Optional<String> searchableInfo = dialogFragment.flatMap(getString);
        dialogFragment.ifPresent(DialogFragment::dismiss);
        return searchableInfo;
    }

    private Optional<DialogFragment> getDialogFragment(final DialogPreference dialogPreference) {
        // prevent NullPointerException
        if (dialogPreference instanceof DropDownPreference) {
            return Optional.empty();
        }
        dialogPreference.performClick();
        fragmentManager.executePendingTransactions();
        return Optional.of((DialogFragment) fragmentManager.findFragmentByTag(tagOfDialogFragment));
    }
}
