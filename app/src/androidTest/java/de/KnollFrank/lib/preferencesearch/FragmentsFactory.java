package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class FragmentsFactory {

    public static Fragments createFragments(final FragmentActivity activity) {
        return de.KnollFrank.lib.preferencesearch.fragment.FragmentsFactory.createFragments(
                new FragmentFactory(),
                activity,
                activity.getSupportFragmentManager(),
                TestActivity.FRAGMENT_CONTAINER_VIEW);
    }
}
