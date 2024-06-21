package de.KnollFrank.lib.preferencesearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class PreferenceScreenWithHostProvider {

    private final Context context;
    public final FragmentInitializer fragmentInitializer;

    public PreferenceScreenWithHostProvider(final Context context,
                                            final FragmentInitializer fragmentInitializer) {
        this.context = context;
        this.fragmentInitializer = fragmentInitializer;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final String fragment) {
        final Fragment _fragment = instantiateAndInitialize(fragment);
        return _fragment instanceof PreferenceFragmentCompat ?
                Optional.of(PreferenceScreenWithHost.fromPreferenceFragment((PreferenceFragmentCompat) _fragment)) :
                Optional.empty();
    }

    // FK-TODO: move method to new class common.Fragments
    private Fragment instantiateAndInitialize(final String fragment) {
        final Fragment _fragment = Fragment.instantiate(context, fragment);
        fragmentInitializer.initialize(_fragment);
        return _fragment;
    }
}
