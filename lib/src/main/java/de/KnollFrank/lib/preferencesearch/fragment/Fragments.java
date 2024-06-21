package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class Fragments {

    private final Context context;
    // FK-TODO: make private again
    public final FragmentInitializer fragmentInitializer;

    public Fragments(final Context context, final FragmentInitializer fragmentInitializer) {
        this.context = context;
        this.fragmentInitializer = fragmentInitializer;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName) {
        final Fragment _fragment = Fragment.instantiate(context, fragmentClassName);
        fragmentInitializer.initialize(_fragment);
        return _fragment;
    }
}
