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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.CurrentActivityProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

// FK-TODO: refactor
// FK-TODO: remove magic numbers
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

    private Optional<PreferenceFragmentCompat> _navigatePreferencePath(final PreferencePath preferencePath) throws Exception {
        navigateToInitialPreferenceScreen.run();
        waitForIdle();
        clickPreferences(Lists.withoutLastElement(preferencePath.preferences()).orElseThrow());
        return Fragments.findVisiblePreferenceFragmentOnCurrentActivity();
    }

    private void clickPreferences(final List<SearchablePreferenceOfHostWithinTree> preferences) throws Exception {
        for (final SearchablePreferenceOfHostWithinTree preference : preferences) {
            // FK-TODO: auch preference.searchablePreference().getKey() und verwenden übergeben
            clickElementWithText(preference.searchablePreference().getTitle().orElseThrow());
            waitForIdle();
        }
    }

    private void clickElementWithText(final String text) throws Exception {
        final Activity activity =
                CurrentActivityProvider
                        .getCurrentActivity()
                        .orElseThrow(() -> new IllegalStateException("No active activity found"));

        // 1. Suche in Preferences (für automatisches Scrollen)
        final Optional<PreferenceFragmentCompat> fragment = Fragments.findVisiblePreferenceFragmentOnCurrentActivity();
        if (fragment.isPresent()) {
            final Preference pref = findPreferenceByTitle(fragment.get().getPreferenceScreen(), text);
            if (pref != null) {
                activity.runOnUiThread(() -> fragment.get().scrollToPreference(pref));
                waitForIdle();
            }
        }

        // 2. Suche im View-Baum & Klick
        final SettableFuture<Boolean> clicked = SettableFuture.create();
        activity.runOnUiThread(() -> {
            final View view = findViewWithText(activity.getWindow().getDecorView(), text);
            if (view != null) {
                performClickOnViewChain(view);
                clicked.set(true);
            } else {
                clicked.setException(new Exception("Element not found: " + text));
            }
        });
        clicked.get(5, TimeUnit.SECONDS);
    }

    // FK-TODO: return Optional<View>
    private View findViewWithText(final View root, final String text) {
        if (root instanceof final TextView textView && text.equals(textView.getText().toString())) {
            return textView;
        }
        if (root instanceof final ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                final View found = findViewWithText(group.getChildAt(i), text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void performClickOnViewChain(final View view) {
        View target = view;
        while (target != null && !target.isClickable()) {
            if (target.getParent() instanceof final View parent) {
                target = parent;
            } else {
                break;
            }
        }
        if (target != null) {
            target.performClick();
        }
    }

    private Preference findPreferenceByTitle(final PreferenceGroup preferenceGroup, final String title) {
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            final Preference preference = preferenceGroup.getPreference(i);
            if (preference.getTitle() != null && title.contentEquals(preference.getTitle())) {
                return preference;
            }
            if (preference instanceof final PreferenceGroup _preferenceGroup) {
                final Preference found = findPreferenceByTitle(_preferenceGroup, title);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void waitForIdle() throws InterruptedException {
        final Activity activity = CurrentActivityProvider.getCurrentActivity().orElse(null);
        if (activity == null) {
            return;
        }

        final CountDownLatch latch = new CountDownLatch(1);
        activity.getWindow().getDecorView().post(latch::countDown);
        latch.await(2, TimeUnit.SECONDS);
        Thread.sleep(300); // Zeit für Fragmente/Animationen
    }
}