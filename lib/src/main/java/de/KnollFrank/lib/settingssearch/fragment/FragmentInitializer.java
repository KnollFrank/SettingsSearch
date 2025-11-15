package de.KnollFrank.lib.settingssearch.fragment;

import androidx.fragment.app.Fragment;

public class FragmentInitializer {

    private final FragmentAdderRemover fragmentAdderRemover;

    FragmentInitializer(final FragmentAdderRemover fragmentAdderRemover) {
        this.fragmentAdderRemover = fragmentAdderRemover;
    }

    public void initialize(final Fragment fragment) {
        fragmentAdderRemover.add(fragment);
        fragmentAdderRemover.remove(fragment);
    }
}
