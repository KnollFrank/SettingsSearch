package de.KnollFrank.lib.settingssearch.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import androidx.annotation.XmlRes;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;

import de.KnollFrank.settingssearch.R;

@RunWith(RobolectricTestRunner.class)
public class LocalesReaderTest {

    @Test
    public void readLocales_shouldParseXmlCorrectly() {
        // Given
        final @XmlRes int resId = R.xml.locales_config;

        // When
        final List<Locale> result = LocalesReader.readLocales(
                ApplicationProvider.getApplicationContext().getResources(),
                resId);

        // Then
        assertThat(
                result,
                contains(
                        Locale.forLanguageTag("en"),
                        Locale.forLanguageTag("de"),
                        Locale.forLanguageTag("de-CH")));
    }
}
