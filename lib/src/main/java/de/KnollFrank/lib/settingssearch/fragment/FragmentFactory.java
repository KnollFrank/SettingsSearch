package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;

@FunctionalInterface
public interface FragmentFactory {

    // instantiate fragmentClass, where fragmentClass.getName().equals(src.orElseThrow().preference.getFragment()) if src.isPresent()
    <T extends Fragment> T instantiate(Class<T> fragmentClass, Optional<PreferenceOfHostOfActivity> src, Context context, InstantiateAndInitializeFragment instantiateAndInitializeFragment);
}
