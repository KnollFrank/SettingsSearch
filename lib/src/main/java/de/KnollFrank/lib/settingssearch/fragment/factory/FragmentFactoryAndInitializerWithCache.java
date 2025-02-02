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

    public <T extends Fragment> T instantiateAndInitializeFragment(final Class<T> fragmentClass,
                                                                   final Optional<PreferenceWithHost> src,
                                                                   final Context context,
                                                                   final Fragments fragments) {
        final Arguments arguments = ArgumentsFactory.createArguments(fragmentClass, src);
        if (!fragmentByArguments.containsKey(arguments)) {
            final T fragment = delegate.instantiateAndInitializeFragment(fragmentClass, src, context, fragments);
            fragmentByArguments.put(arguments, fragment);
        }
        return (T) fragmentByArguments.get(arguments);
    }
}
