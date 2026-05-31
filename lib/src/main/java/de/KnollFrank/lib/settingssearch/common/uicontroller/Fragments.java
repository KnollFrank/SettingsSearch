package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// FK-TODO: refactor
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
        return findVisibleFragment(fragmentActivity.getSupportFragmentManager())
                .map(Either::<Fragment, String>ofLeft)
                .orElseGet(() -> Either.ofRight("No visible Fragment found on Activity: " + fragmentActivity.getClass().getName() + "."));
    }

    private static Either<PreferenceFragmentCompat, String> findEitherVisiblePreferenceFragmentOrError(final FragmentActivity fragmentActivity) {
        return findVisiblePreferenceFragment(fragmentActivity.getSupportFragmentManager())
                .map(Either::<PreferenceFragmentCompat, String>ofLeft)
                .orElseGet(() -> Either.ofRight("No visible PreferenceFragment found on Activity: " + fragmentActivity.getClass().getName() + "."));
    }

    private static Optional<Fragment> findVisibleFragment(final FragmentManager fragmentManager) {
        final List<Fragment> visibleFragments = findVisibleFragmentsRecursively(fragmentManager);
        if (visibleFragments.isEmpty()) {
            return Optional.empty();
        }

        // 1. Prioritize fragment that actually contains the settings RecyclerView
        final Optional<Fragment> fragmentWithSettingsRv = visibleFragments.stream()
                .filter(f -> containsSettingsRecyclerView(f.getView()))
                .findFirst();
        if (fragmentWithSettingsRv.isPresent()) {
            return fragmentWithSettingsRv;
        }

        // 2. Fallback to any PreferenceFragmentCompat
        final Optional<Fragment> anyPreferenceFragment = visibleFragments.stream()
                .filter(f -> f instanceof PreferenceFragmentCompat)
                .findFirst();
        if (anyPreferenceFragment.isPresent()) {
            return anyPreferenceFragment;
        }

        // 3. Last resort: the first visible fragment
        return Optional.of(visibleFragments.get(0));
    }

    private static Optional<PreferenceFragmentCompat> findVisiblePreferenceFragment(final FragmentManager fragmentManager) {
        return findVisibleFragment(fragmentManager)
                .flatMap(Fragments::asPreferenceFragment);
    }

    private static List<Fragment> findVisibleFragmentsRecursively(final FragmentManager fragmentManager) {
        final List<Fragment> result = new ArrayList<>();
        for (final Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.isVisible()) {
                result.add(fragment);
                result.addAll(findVisibleFragmentsRecursively(fragment.getChildFragmentManager()));
            }
        }
        return result;
    }

    private static boolean containsSettingsRecyclerView(final View view) {
        if (view == null) return false;
        if (view instanceof final RecyclerView recyclerView) {
            return recyclerView.getId() == androidx.preference.R.id.recycler_view;
        }
        if (view instanceof final ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (containsSettingsRecyclerView(viewGroup.getChildAt(i))) {
                    return true;
                }
            }
        }
        return false;
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
