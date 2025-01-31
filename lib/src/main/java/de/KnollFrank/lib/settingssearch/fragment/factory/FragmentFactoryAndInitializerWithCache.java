package de.KnollFrank.lib.settingssearch.fragment.factory;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

public class FragmentFactoryAndInitializerWithCache {

    private final FragmentFactoryAndInitializer delegate;
    private final Map<Arguments, Fragment> fragmentByArguments = new HashMap<>();

    public FragmentFactoryAndInitializerWithCache(final FragmentFactoryAndInitializer delegate) {
        this.delegate = delegate;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName,
                                                     final Optional<PreferenceWithHost> src,
                                                     final Context context,
                                                     final Fragments fragments) {
        final Arguments arguments = ArgumentsFactory.createArguments(fragmentClassName, src);
        if (!fragmentByArguments.containsKey(arguments)) {
            final Fragment fragment = delegate.instantiateAndInitializeFragment(fragmentClassName, src, context, fragments);
            fragmentByArguments.put(arguments, fragment);
        }
        return fragmentByArguments.get(arguments);
    }
}
