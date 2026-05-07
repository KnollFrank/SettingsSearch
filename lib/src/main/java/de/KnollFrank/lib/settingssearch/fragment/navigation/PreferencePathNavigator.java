package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.EspressoIdlingResource;
import de.KnollFrank.lib.settingssearch.common.Keyboard;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.Activities;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;

// FK-TODO: refactor
public class PreferencePathNavigator {

    private PreferencePathNavigator() {
    }

    public static ListenableFuture<Optional<? extends Fragment>> navigatePreferencePath(final PreferencePath preferencePath) {
        final SettableFuture<Optional<? extends Fragment>> future = SettableFuture.create();

        // Signalisiert Espresso-Tests, dass eine Hintergrundaktion läuft
        EspressoIdlingResource.increment();

        // UI Automator Aktionen müssen außerhalb des Main-Threads laufen
        new Thread(() -> {
            try {
                final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

                // FK-TODO: make configurable
                // 1. Suche verlassen / Tastatur schließen
                Activities
                        .getCurrentActivity()
                        .ifPresent(activity ->
                                           InstrumentationRegistry
                                                   .getInstrumentation()
                                                   .runOnMainSync(() -> Keyboard.hideKeyboard(activity)));
                device.pressBack();

                // 2. Den Pfad durchklicken
                for (final SearchablePreferenceOfHostWithinTree step : Lists.withoutLastElement(preferencePath.preferences()).orElseThrow()) {
                    final String title = step.searchablePreference().getTitle().orElseThrow();
                    clickPreferenceWithTitle(device, title);
                }

                // 3. Ergebnis zurückgeben
                future.set(Fragments.findVisiblePreferenceFragmentOnCurrentActivity());

            } catch (Exception e) {
                future.setException(e);
            } finally {
                // Signalisiert Espresso-Tests, dass wir fertig sind
                EspressoIdlingResource.decrement();
            }
        }).start();
        return future;
    }

    private static void clickPreferenceWithTitle(final UiDevice device, final String title) throws UiObjectNotFoundException {
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
            } catch (UiObjectNotFoundException e) {
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