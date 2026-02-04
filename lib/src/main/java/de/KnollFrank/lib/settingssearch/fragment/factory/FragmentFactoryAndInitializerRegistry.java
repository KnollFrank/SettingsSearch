package de.KnollFrank.lib.settingssearch.fragment.factory;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class FragmentFactoryAndInitializerRegistry {

    private final FragmentFactoryAndInitializer delegate;
    private final Map<Arguments, Fragment> registry = new ConcurrentHashMap<>();

    public FragmentFactoryAndInitializerRegistry(final FragmentFactoryAndInitializer delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    public <T extends Fragment> T instantiateAndInitializeFragment(final Class<T> fragmentClass,
                                                                   final Optional<PreferenceOfHostOfActivity> src,
                                                                   final Context context,
                                                                   final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final Arguments arguments = ArgumentsFactory.createArguments(fragmentClass, src);
        if (!registry.containsKey(arguments)) {
            final T fragment = delegate.instantiateAndInitializeFragment(fragmentClass, src, context, instantiateAndInitializeFragment);
            registry.put(arguments, fragment);
        }
        return (T) registry.get(arguments);
    }
}
