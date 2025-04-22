package de.KnollFrank.lib.settingssearch.fragment;

import androidx.fragment.app.Fragment;

public class PreferenceDialogs {

    private final FragmentAdderRemover fragmentAdderRemover;

    PreferenceDialogs(final FragmentAdderRemover fragmentAdderRemover) {
        this.fragmentAdderRemover = fragmentAdderRemover;
    }

    public void showPreferenceDialog(Fragment preferenceDialog) {
        fragmentAdderRemover.add(preferenceDialog);
    }

    public void hidePreferenceDialog(Fragment preferenceDialog) {
        fragmentAdderRemover.remove(preferenceDialog);
    }
}
