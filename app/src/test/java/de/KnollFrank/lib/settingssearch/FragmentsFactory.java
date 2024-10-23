package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.settingssearch.test.TestActivity;

public class FragmentsFactory {

    public static Fragments createFragments(final FragmentActivity activity) {
        return de.KnollFrank.lib.settingssearch.fragment.FragmentsFactory.createFragments(
                new DefaultFragmentFactory(),
                activity,
                activity.getSupportFragmentManager(),
                TestActivity.FRAGMENT_CONTAINER_VIEW);
    }
}
