package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.common.task.BlockingOnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.settingssearch.test.TestActivity;

class FragmentsFactory {

    public static Fragments createFragments(final FragmentActivity activity) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(
                        new FragmentFactoryAndInitializer(
                                new DefaultFragmentFactory(),
                                new DefaultFragmentInitializer(
                                        activity.getSupportFragmentManager(),
                                        TestActivity.FRAGMENT_CONTAINER_VIEW,
                                        BlockingOnUiThreadRunnerFactory.fromActivity(activity)))),
                activity);
    }
}
