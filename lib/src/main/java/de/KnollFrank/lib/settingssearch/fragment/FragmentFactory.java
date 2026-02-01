package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHost;

@FunctionalInterface
public interface FragmentFactory {

    // instantiate fragmentClass, where fragmentClass.getName().equals(src.orElseThrow().preference.getFragment()) if src.isPresent()
    <T extends Fragment> T instantiate(Class<T> fragmentClass, Optional<PreferenceOfHost> src, Context context, InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
