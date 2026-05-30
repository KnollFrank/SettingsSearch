package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.MoreCollectors;

import java.util.Optional;
import java.util.function.Function;

public class Fragments {

    public static Either<Fragment, String> findEitherVisibleFragmentOnCurrentActivityOrError() {
        return _findEitherVisibleFragmentOnCurrentActivityOrError(Fragments::findEitherVisibleFragmentOrError);
    }

    public static Either<PreferenceFragmentCompat, String> findEitherVisiblePreferenceFragmentOnCurrentActivityOrError() {
        return _findEitherVisibleFragmentOnCurrentActivityOrError(Fragments::findEitherVisiblePreferenceFragmentOrError);
    }

    private static <F extends Fragment> Either<F, String> _findEitherVisibleFragmentOnCurrentActivityOrError(final Function<FragmentActivity, Either<F, String>> findEitherVisibleFragmentOrError) {
        return CurrentActivityProvider
                .getCurrentActivity()
                .map(Fragments::asFragmentActivityOrError)
                .map(Either::left)
                .<Either<F, String>>map(fragmentActivity -> fragmentActivity.flatMap(findEitherVisibleFragmentOrError))
                .orElseGet(() -> Either.ofRight("No current Activity found. Is the app in foreground?"));
    }

    private static Either<Fragment, String> findEitherVisibleFragmentOrError(final FragmentActivity fragmentActivity) {
        return Fragments
                .findVisibleFragment(fragmentActivity.getSupportFragmentManager())
                .map(Either::<Fragment, String>ofLeft)
                .orElseGet(() -> Either.ofRight("No visible Fragment found on Activity: " + fragmentActivity.getClass().getName() + "."));
    }

    private static Either<PreferenceFragmentCompat, String> findEitherVisiblePreferenceFragmentOrError(final FragmentActivity fragmentActivity) {
        // FK-TODO: DRY with findEitherVisibleFragmentOrError()
        return Fragments
                .findVisiblePreferenceFragment(fragmentActivity.getSupportFragmentManager())
                .map(Either::<PreferenceFragmentCompat, String>ofLeft)
                .orElseGet(() -> Either.ofRight("No visible PreferenceFragmentCompat found on Activity: " + fragmentActivity.getClass().getName() + "."));
    }

    private static Optional<PreferenceFragmentCompat> findVisiblePreferenceFragment(final FragmentManager fragmentManager) {
        return Fragments
                .findVisibleFragment(fragmentManager)
                .flatMap(Fragments::asPreferenceFragment);
    }

    private static Optional<Fragment> findVisibleFragment(final FragmentManager fragmentManager) {
        return fragmentManager
                .getFragments()
                .stream()
                .filter(Fragment::isVisible)
                .collect(MoreCollectors.toOptional());
    }

    private static Either<FragmentActivity, String> asFragmentActivityOrError(final Activity activity) {
        return activity instanceof final FragmentActivity fragmentActivity ?
                Either.ofLeft(fragmentActivity) :
                Either.ofRight("Current Activity (" + activity.getClass().getName() + ") is not a FragmentActivity.");
    }

    private static Optional<PreferenceFragmentCompat> asPreferenceFragment(final Fragment fragment) {
        return fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(preferenceFragment) :
                Optional.empty();
    }
}
