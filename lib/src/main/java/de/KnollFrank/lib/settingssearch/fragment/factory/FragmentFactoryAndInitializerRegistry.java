package de.KnollFrank.lib.settingssearch.fragment.factory;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class FragmentFactoryAndInitializerRegistry {

    private final FragmentFactoryAndInitializer delegate;
    private final Map<Arguments, FragmentOfActivity<? extends Fragment>> registry = new ConcurrentHashMap<>();

    public FragmentFactoryAndInitializerRegistry(final FragmentFactoryAndInitializer delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    public <T extends Fragment> FragmentOfActivity<T> instantiateAndInitializeFragment(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final Arguments arguments = ArgumentsFactory.createArguments(fragmentClass.fragment(), src);
        if (!registry.containsKey(arguments)) {
            final FragmentOfActivity<T> fragment = delegate.instantiateAndInitializeFragment(fragmentClass, src, context, instantiateAndInitializeFragment);
            registry.put(arguments, fragment);
        }
        return (FragmentOfActivity<T>) registry.get(arguments);
    }
}
