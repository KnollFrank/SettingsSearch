package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

@FunctionalInterface
public interface FragmentFactory {

    Fragment instantiate(String fragmentClassName, Optional<PreferenceWithHost> src, Context context);
}
