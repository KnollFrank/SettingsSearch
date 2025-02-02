package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

@FunctionalInterface
public interface FragmentFactory {

    // instantiate fragmentClass, where fragmentClass.getName().equals(src.get().preference.getFragment()) if src.isPresent()
    // FK-TODO: Rückgabetyp sollte immer PreferenceFragmentCompat sein?
    Fragment instantiate(Class<? extends Fragment> fragmentClass, Optional<PreferenceWithHost> src, Context context, Fragments fragments);
}
