package de.KnollFrank.lib.settingssearch.fragment.factory;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class FragmentFactoryAndInitializerRegistry {

    private final FragmentFactoryAndInitializer delegate;
    private final Map<Arguments, Fragment> registry = new HashMap<>();

    public FragmentFactoryAndInitializerRegistry(final FragmentFactoryAndInitializer delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    public <T extends Fragment> T instantiateAndInitializeFragment(final Class<T> fragmentClass,
                                                                   final Optional<PreferenceWithHost> src,
                                                                   final Context context,
                                                                   final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return (T) registry.computeIfAbsent(
                ArgumentsFactory.createArguments(fragmentClass, src),
                arguments ->
                        delegate.instantiateAndInitializeFragment(
                                fragmentClass,
                                src,
                                context,
                                instantiateAndInitializeFragment));
    }
}
