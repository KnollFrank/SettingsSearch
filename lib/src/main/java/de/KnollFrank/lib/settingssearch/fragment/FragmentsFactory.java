package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;

public class FragmentsFactory {

    public static Fragments createFragments(final FragmentFactory fragmentFactory,
                                            final Context context,
                                            final FragmentManager fragmentManager,
                                            final @IdRes int containerViewId) {
        return new Fragments(
                new FragmentFactoryAndInitializerWithCache(
                        new FragmentFactoryAndInitializer(
                                fragmentFactory,
                                new DefaultFragmentInitializer(fragmentManager, containerViewId))),
                context);
    }
}
