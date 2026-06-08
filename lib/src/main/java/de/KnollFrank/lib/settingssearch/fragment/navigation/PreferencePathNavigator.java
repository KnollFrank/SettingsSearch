package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.common.uicontroller.CurrentActivityProvider;
import de.KnollFrank.lib.settingssearch.common.uicontroller.Fragments;
import de.KnollFrank.lib.settingssearch.common.uicontroller.UiController;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;

// FK-TODO: refactor
public class PreferencePathNavigator {

    private final Runnable navigateToInitialPreferenceScreen;

    public PreferencePathNavigator(final Runnable navigateToInitialPreferenceScreen) {
        this.navigateToInitialPreferenceScreen = navigateToInitialPreferenceScreen;
    }

    public ListenableFuture<Optional<? extends Fragment>> navigatePreferencePath(final PreferencePath preferencePath) {
        final SettableFuture<Optional<? extends Fragment>> future = SettableFuture.create();
        EspressoIdlingResource.increment();
        new Thread(() -> {
            try {
                future.set(_navigatePreferencePath(preferencePath));
            } catch (final Exception e) {
                future.setException(e);
            } finally {
                EspressoIdlingResource.decrement();
            }
        }).start();
        return future;
    }

    private Optional<PreferenceFragmentCompat> _navigatePreferencePath(final PreferencePath preferencePath) throws InterruptedException {
        navigateToInitialPreferenceScreen.run();
        UiController.waitUntilIdle();
        clickPreferences(Lists.withoutLastElement(preferencePath.preferences()).orElseThrow());
        scrollToPreferenceHavingTitle(
                preferencePath.preferences().get(preferencePath.preferences().size() - 1).searchablePreference().getTitle().orElseThrow(),
                getCurrentActivity());
        return Fragments
                .findEitherVisiblePreferenceFragmentOnCurrentActivityOrError()
                .join(
                        Optional::of,
                        errorMessage -> Optional.empty());
    }

    private void clickPreferences(final List<SearchablePreferenceOfHostWithinTree> preferences) throws InterruptedException {
        for (final SearchablePreferenceOfHostWithinTree preference : preferences) {
            clickPreference(preference);
            UiController.waitUntilIdle();
        }
    }

    private void clickPreference(final SearchablePreferenceOfHostWithinTree preference) throws InterruptedException {
        clickElementWithText(preference.searchablePreference().getTitle().orElseThrow());
    }

    private void clickElementWithText(final String text) throws InterruptedException {
        final Activity activity = getCurrentActivity();
        scrollToPreferenceHavingTitle(text, activity);
        final View view = waitForViewWithText(text, activity);
        OnUiThreadRunnerFactory
                .fromActivity(activity)
                .runBlockingOnUiThread(() -> performClickOnViewChain(view));
    }

    private static Activity getCurrentActivity() throws InterruptedException {
        return CurrentActivityProvider.awaitResumedActivity();
    }

    private record PreferenceOfPreferenceFragment(Preference preference,
                                                  PreferenceFragmentCompat preferenceFragment) {
    }

    private void scrollToPreferenceHavingTitle(final String text, final Activity activity) throws InterruptedException {
        final Optional<PreferenceOfPreferenceFragment> preferenceOfPreferenceFragment = findPreferenceByTitle(text);
        if (preferenceOfPreferenceFragment.isPresent()) {
            scrollToPreference(preferenceOfPreferenceFragment.orElseThrow(), activity);
        }
    }

    private Optional<PreferenceOfPreferenceFragment> findPreferenceByTitle(final String text) {
        return Fragments
                .findEitherVisiblePreferenceFragmentOnCurrentActivityOrError()
                .<Optional<PreferenceFragmentCompat>>join(
                        Optional::of,
                        errorMessage -> Optional.empty())
                .flatMap(
                        preferenceFragment ->
                                this
                                        .findPreferenceByTitle(preferenceFragment.getPreferenceScreen(), text)
                                        .map(preference -> new PreferenceOfPreferenceFragment(preference, preferenceFragment)));
    }

    private Optional<Preference> findPreferenceByTitle(final PreferenceGroup preferenceGroup, final String title) {
        return Preferences
                .getChildrenRecursively(preferenceGroup)
                .stream()
                .filter(preference -> isPreferenceWithTitle(preference, title))
                .findFirst();
    }

    private static Boolean isPreferenceWithTitle(final Preference preference, final String title) {
        return Optional
                .ofNullable(preference.getTitle())
                .map(title::contentEquals)
                .orElse(false);
    }

    private void scrollToPreference(final PreferenceOfPreferenceFragment preferenceOfPreferenceFragment, final Activity activity) throws InterruptedException {
        activity.runOnUiThread(() -> preferenceOfPreferenceFragment.preferenceFragment().scrollToPreference(preferenceOfPreferenceFragment.preference()));
        UiController.waitUntilIdle();
    }

    private View waitForViewWithText(final String text, final Activity activity) throws InterruptedException {
        final OnUiThreadRunner onUiThreadRunner = OnUiThreadRunnerFactory.fromActivity(activity);
        while (true) {
            final Optional<View> foundView =
                    onUiThreadRunner.runBlockingOnUiThread(
                            () ->
                                    findViewWithText(
                                            activity.getWindow().getDecorView(),
                                            text));
            if (foundView.isPresent()) {
                return foundView.get();
            }
            UiController.waitUntilIdle();
        }
    }


    // FK-TODO: refactor analogous to Preferences.getChildrenRecursively(preferenceGroup)
    private Optional<View> findViewWithText(final View root, final String text) {
        if (root instanceof final TextView textView && text.equals(textView.getText().toString())) {
            return Optional.of(textView);
        }
        if (root instanceof final ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                final Optional<View> found = findViewWithText(group.getChildAt(i), text);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    private void performClickOnViewChain(final View view) {
        findClickableParentView(view).ifPresent(View::performClick);
    }

    private static Optional<View> findClickableParentView(final View view) {
        Optional<View> target;
        for (target = Optional.of(view);
             target.isPresent() && !target.orElseThrow().isClickable();
             target = findParentView(target.orElseThrow())) {
        }
        return target;
    }

    private static Optional<View> findParentView(final View view) {
        return Optional
                .ofNullable(view.getParent())
                .filter(View.class::isInstance)
                .map(View.class::cast);
    }
}
