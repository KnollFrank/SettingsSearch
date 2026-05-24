package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.MoreCollectors;

import java.util.Optional;
import java.util.stream.Stream;

public class Fragments {

    public static Either<PreferenceFragmentCompat, String> findEitherVisiblePreferenceFragmentOnCurrentActivityOrError() {
        return CurrentActivityProvider
                .getCurrentActivity()
                .map(
                        activity ->
                                // FK-TODO: use asFragmentActivityOrError()
                                activity instanceof final FragmentActivity fragmentActivity ?
                                        findEitherVisiblePreferenceFragmentOrError(fragmentActivity) :
                                        Either.<PreferenceFragmentCompat, String>ofRight("Current Activity (" + activity.getClass().getName() + ") is not a FragmentActivity. Fragments cannot be retrieved."))
                .orElseGet(() -> Either.ofRight("No current Activity found. Is the app in foreground?"));
    }

    private static Either<PreferenceFragmentCompat, String> findEitherVisiblePreferenceFragmentOrError(final FragmentActivity fragmentActivity) {
        return Fragments
                .findVisiblePreferenceFragment(fragmentActivity.getSupportFragmentManager())
                .map(Either::<PreferenceFragmentCompat, String>ofLeft)
                .orElseGet(() -> Either.ofRight("No visible PreferenceFragmentCompat found on Activity: " + fragmentActivity.getClass().getName() + "."));
    }

    public static Optional<PreferenceFragmentCompat> findVisiblePreferenceFragment(final FragmentManager fragmentManager) {
        return fragmentManager
                .getFragments()
                .stream()
                .filter(Fragment::isVisible)
                .flatMap(Fragments::asPreferenceFragment)
                .collect(MoreCollectors.toOptional());
    }

    private static Either<FragmentActivity, String> asFragmentActivityOrError(final Activity activity) {
        return activity instanceof final FragmentActivity fragmentActivity ?
                Either.ofLeft(fragmentActivity) :
                Either.ofRight("Current Activity (" + activity.getClass().getName() + ") is not a FragmentActivity.");
    }

    private static Stream<PreferenceFragmentCompat> asPreferenceFragment(final Fragment fragment) {
        return fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Stream.of(preferenceFragment) :
                Stream.empty();
    }
}
