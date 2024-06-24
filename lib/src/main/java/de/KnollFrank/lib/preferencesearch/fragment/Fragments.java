package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class Fragments {

    private final FragmentFactory fragmentFactory;
    private final FragmentInitializer fragmentInitializer;
    private final Context context;

    public Fragments(final FragmentFactory fragmentFactory,
                     final FragmentInitializer fragmentInitializer,
                     final Context context) {
        this.fragmentFactory = fragmentFactory;
        this.fragmentInitializer = fragmentInitializer;
        this.context = context;
    }

    public Fragment instantiateAndInitializeFragment(final String fragmentClassName) {
        final Fragment _fragment = fragmentFactory.instantiate(fragmentClassName, context);
        fragmentInitializer.initialize(_fragment);
        return _fragment;
    }
}
