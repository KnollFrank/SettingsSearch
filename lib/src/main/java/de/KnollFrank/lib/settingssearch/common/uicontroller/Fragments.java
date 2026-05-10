package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.MoreCollectors;

import java.util.Optional;
import java.util.stream.Stream;

public class Fragments {

    public static Optional<PreferenceFragmentCompat> findVisiblePreferenceFragmentOnCurrentActivity() {
        return CurrentActivityProvider
                .getCurrentActivity()
                .flatMap(Fragments::asFragmentActivity)
                .flatMap(fragmentActivity -> findVisiblePreferenceFragment(fragmentActivity.getSupportFragmentManager()));
    }

    public static Optional<PreferenceFragmentCompat> findVisiblePreferenceFragment(final FragmentManager fragmentManager) {
        return fragmentManager
                .getFragments()
                .stream()
                .filter(Fragment::isVisible)
                .flatMap(Fragments::asPreferenceFragment)
                .collect(MoreCollectors.toOptional());
    }

    private static Optional<FragmentActivity> asFragmentActivity(final Activity activity) {
        return activity instanceof final FragmentActivity fragmentActivity ?
                Optional.of(fragmentActivity) :
                Optional.empty();
    }

    private static Stream<PreferenceFragmentCompat> asPreferenceFragment(final Fragment fragment) {
        return fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Stream.of(preferenceFragment) :
                Stream.empty();
    }
}
