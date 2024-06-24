package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public Fragment instantiate(final String fragmentClassName, final Context context) {
        return Fragment.instantiate(context, fragmentClassName);
    }
}
