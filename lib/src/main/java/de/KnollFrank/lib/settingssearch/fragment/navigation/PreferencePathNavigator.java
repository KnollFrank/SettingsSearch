package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

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

    private Optional<PreferenceFragmentCompat> _navigatePreferencePath(final PreferencePath preferencePath) throws UiObjectNotFoundException {
        navigateToInitialPreferenceScreen.run();
        clickPreferences(Lists.withoutLastElement(preferencePath.preferences()).orElseThrow());
        return Fragments.findVisiblePreferenceFragmentOnCurrentActivity();
    }

    private static void clickPreferences(final List<SearchablePreferenceOfHostWithinTree> preferences) throws UiObjectNotFoundException {
        for (final SearchablePreferenceOfHostWithinTree preference : preferences) {
            clickPreferenceWithTitle(preference.searchablePreference().getTitle().orElseThrow());
        }
    }

    private static void clickPreferenceWithTitle(final String title) throws UiObjectNotFoundException {
        final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final UiSelector titleSelector = new UiSelector().text(title);

        // Try finding it directly first (it might already be on screen)
        final UiObject preference = device.findObject(titleSelector);
        if (preference.exists()) {
            preference.click();
            return;
        }

        // If not found, try to scroll to it if there is a scrollable container
        final UiSelector scrollableSelector = new UiSelector().scrollable(true);
        if (device.findObject(scrollableSelector).exists()) {
            final UiScrollable scrollable = new UiScrollable(scrollableSelector);
            try {
                if (scrollable.scrollIntoView(titleSelector)) {
                    preference.click();
                    return;
                }
            } catch (final UiObjectNotFoundException e) {
                // Scroll into view failed, maybe it's not in the list or the list is weird
            }
        }

        // Fallback: wait a bit and try one last time
        if (preference.waitForExists(2000)) {
            preference.click();
        } else {
            throw new UiObjectNotFoundException("Could not find preference with title: " + title);
        }
    }
}