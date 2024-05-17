package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import android.os.Looper;

import com.bytehamster.preferencesearch.test.R;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class PreferenceParserTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @AfterClass
    public static void afterClass() {
        Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldParseXmlResource() {
        // Given
        final PreferenceParser preferenceParser = PreferenceParserFactory.fromContext(TestUtils.getContext());
        final int preferenceScreen = R.xml.prefs;

        // When
        final List<PreferenceItem> preferenceItems = preferenceParser.parsePreferenceScreen(preferenceScreen);

        // Then
        assertThat(preferenceItems, hasSize(15));
    }
}