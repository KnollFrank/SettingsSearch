package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public Fragment instantiate(final String fragmentClassName, Optional<PreferenceWithHost> src, final Context context) {
        return Fragment.instantiate(context, fragmentClassName);
    }
}
