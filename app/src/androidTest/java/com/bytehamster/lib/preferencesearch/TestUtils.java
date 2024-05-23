package com.bytehamster.lib.preferencesearch;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

public class TestUtils {

    public static Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    public static Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }
}
