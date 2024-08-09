package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

@FunctionalInterface
public interface FragmentFactory {

    // instantiate fragmentClassName, where fragmentClassName.equals(src.get().preference.getFragment()) if src.isPresent()
    // FK-TODO: PreferenceWithHost.host könnte auch eine Instanz von PreferenceFragmentCompat sein anstatt nur die Klasse?
    Fragment instantiate(String fragmentClassName, Optional<PreferenceWithHost> src, Context context);
}
