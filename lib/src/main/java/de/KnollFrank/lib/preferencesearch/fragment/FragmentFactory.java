package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

@FunctionalInterface
public interface FragmentFactory {

    Fragment instantiate(Context context, String fragmentClassName);
}
