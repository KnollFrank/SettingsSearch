package de.KnollFrank.lib.settingssearch.common;

import androidx.test.espresso.idling.CountingIdlingResource;

public final class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL_BACKGROUND_TASK";

    private static final CountingIdlingResource countingIdlingResource = new CountingIdlingResource(RESOURCE);

    private EspressoIdlingResource() {
    }

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        if (!countingIdlingResource.isIdleNow()) {
            countingIdlingResource.decrement();
        }
    }

    public static CountingIdlingResource getCountingIdlingResource() {
        return countingIdlingResource;
    }
}
