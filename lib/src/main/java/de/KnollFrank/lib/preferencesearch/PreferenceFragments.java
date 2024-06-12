package de.KnollFrank.lib.preferencesearch;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class PreferenceFragments {

    private final Context context;
    private final FragmentManager fragmentManager;
    private final @IdRes int containerResId;

    public PreferenceFragments(final Context context,
                               final FragmentManager fragmentManager,
                               final @IdRes int containerResId) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerResId = containerResId;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final String fragment) {
        return getPreferenceScreenOfFragment(Fragment.instantiate(this.context, fragment));
    }

    public void initialize(final Fragment fragment) {
        this
                .fragmentManager
                .beginTransaction()
                .replace(this.containerResId, fragment)
                .commitNow();
    }

    private Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final Fragment fragment) {
        return fragment instanceof PreferenceFragmentCompat ?
                Optional.of(getPreferenceScreenOfFragment((PreferenceFragmentCompat) fragment)) :
                Optional.empty();
    }

    private PreferenceScreenWithHost getPreferenceScreenOfFragment(final PreferenceFragmentCompat preferenceFragment) {
        initialize(preferenceFragment);
        return PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(preferenceFragment);
    }
}
