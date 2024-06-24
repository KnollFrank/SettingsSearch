package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class FragmentFactory implements IFragmentFactory {

    @Override
    public Fragment instantiate(final Context context, final String fragmentClassName) {
        return Fragment.instantiate(context, fragmentClassName);
    }
}
