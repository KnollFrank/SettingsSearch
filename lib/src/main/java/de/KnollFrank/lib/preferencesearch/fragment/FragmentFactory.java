package de.KnollFrank.lib.preferencesearch.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface FragmentFactory {

    Fragment instantiate(String fragmentClassName, Optional<Preference> src, Context context);
}
