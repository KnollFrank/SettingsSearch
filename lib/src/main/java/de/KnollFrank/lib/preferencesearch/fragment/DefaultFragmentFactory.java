package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public Fragment instantiate(final String fragmentClassName, Optional<Preference> src, final Context context) {
        return Fragment.instantiate(context, fragmentClassName);
    }
}
