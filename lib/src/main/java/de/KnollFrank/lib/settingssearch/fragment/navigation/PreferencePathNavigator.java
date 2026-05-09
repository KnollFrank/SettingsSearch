package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import java.util.concurrent.atomic.AtomicReference;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.CurrentActivityProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

// FK-TODO: refactor
public class PreferencePathNavigator {

    private static final int MAX_VIEW_WAIT_RETRIES = 50;
    private static final long VIEW_POLLING_INTERVAL_MS = 100;
    private static final long IDLE_TIMEOUT_SECONDS = 5;

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
        waitUntilIdle();
        clickPreferences(Lists.withoutLastElement(preferencePath.preferences()).orElseThrow());
        return Fragments.findVisiblePreferenceFragmentOnCurrentActivity();
    }

    private void clickPreferences(final List<SearchablePreferenceOfHostWithinTree> preferences) throws Exception {
        for (final SearchablePreferenceOfHostWithinTree preference : preferences) {
            clickElementWithText(preference.searchablePreference().getTitle().orElseThrow());
            waitUntilIdle();
        }
    }

    private void clickElementWithText(final String text) throws Exception {
        final Activity activity =
                CurrentActivityProvider
                        .getCurrentActivity()
                        .orElseThrow(() -> new IllegalStateException("No active activity found"));

        // 1. Suche in Preferences (für automatisches Scrollen)
        Fragments
                .findVisiblePreferenceFragmentOnCurrentActivity()
                .ifPresent(
                        fragment ->
                                this
                                        .findPreferenceByTitle(fragment.getPreferenceScreen(), text)
                                        .ifPresent(
                                                preference -> {
                                                    activity.runOnUiThread(() -> fragment.scrollToPreference(preference));
                                                    try {
                                                        waitUntilIdle();
                                                    } catch (final InterruptedException e) {
                                                        Thread.currentThread().interrupt();
                                                    }
                                                }));

        // 2. Suche im View-Baum mit programmatischem Warten auf Sichtbarkeit
        final View view = waitForViewWithText(activity, text);

        final SettableFuture<Boolean> clicked = SettableFuture.create();
        activity.runOnUiThread(() -> {
            performClickOnViewChain(view);
            clicked.set(true);
        });
        clicked.get();
    }

    private View waitForViewWithText(final Activity activity, final String text) throws Exception {
        for (int i = 0; i < MAX_VIEW_WAIT_RETRIES; i++) {
            final AtomicReference<Optional<View>> foundView = new AtomicReference<>(Optional.empty());
            final CountDownLatch latch = new CountDownLatch(1);

            activity.runOnUiThread(() -> {
                foundView.set(findViewWithText(activity.getWindow().getDecorView(), text));
                latch.countDown();
            });

            latch.await();
            if (foundView.get().isPresent()) {
                return foundView.get().get();
            }
            Thread.sleep(VIEW_POLLING_INTERVAL_MS);
        }
        throw new Exception("Timeout waiting for view with text: " + text);
    }

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
        Optional<View> target = Optional.of(view);
        while (target.isPresent() && !target.get().isClickable()) {
            target = Optional.ofNullable(target.get().getParent())
                    .filter(View.class::isInstance)
                    .map(View.class::cast);
        }
        target.ifPresent(View::performClick);
    }

    private Optional<Preference> findPreferenceByTitle(final PreferenceGroup preferenceGroup, final String title) {
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            final Preference preference = preferenceGroup.getPreference(i);
            if (Optional.ofNullable(preference.getTitle()).map(title::contentEquals).orElse(false)) {
                return Optional.of(preference);
            }
            if (preference instanceof final PreferenceGroup _preferenceGroup) {
                final Optional<Preference> found = findPreferenceByTitle(_preferenceGroup, title);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    private void waitUntilIdle() throws InterruptedException {
        final Activity activity = CurrentActivityProvider.getCurrentActivity().orElse(null);
        if (activity == null) {
            return;
        }

        // 1. Warten bis der Main-Looper idle ist (MessageQueue leer)
        final CountDownLatch looperLatch = new CountDownLatch(1);
        activity.runOnUiThread(() -> {
            Looper.myQueue().addIdleHandler(
                    new MessageQueue.IdleHandler() {

                        @Override
                        public boolean queueIdle() {
                            looperLatch.countDown();
                            return false;
                        }
                    });
        });
        looperLatch.await(IDLE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // 2. Warten bis das Layout stabil ist
        final CountDownLatch layoutLatch = new CountDownLatch(1);
        activity.runOnUiThread(() -> {
            final View decorView = activity.getWindow().getDecorView();
            if (!decorView.isLayoutRequested()) {
                layoutLatch.countDown();
                return;
            }
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    layoutLatch.countDown();
                }
            });
        });
        layoutLatch.await(IDLE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
}
