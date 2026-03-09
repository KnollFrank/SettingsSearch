package de.KnollFrank.lib.settingssearch.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;

@RunWith(RobolectricTestRunner.class)
public class LocalesTest {

    @Test
    public void shouldGetBestSupportedLocaleForDesiredLocales() {
        // Given
        final Locale supportedLocale = Locale.forLanguageTag("de-DE");
        final Locale desiredLocale = Locale.forLanguageTag("de-AT");

        // When
        final Locale bestSupportedLocale =
                Locales.getBestSupportedLocaleForDesiredLocales(
                        List.of(supportedLocale),
                        List.of(desiredLocale));

        // Then
        assertThat(bestSupportedLocale, is(supportedLocale));
    }

    @Test
    public void getBestSupportedLocaleForDesiredLocales_shouldReturnPrimaryLocaleWhenNoMatchIsFound() {
        // Given
        final Locale primaryLocale = Locale.GERMAN;

        // When
        final Locale bestSupportedLocale =
                Locales.getBestSupportedLocaleForDesiredLocales(
                        List.of(primaryLocale, Locale.ENGLISH),
                        List.of(Locale.CHINESE));

        // Then
        assertThat(bestSupportedLocale, is(primaryLocale));
    }

    @Test
    public void getBestSupportedLocaleForDesiredLocales_shouldReturnFirstMatchWhenSystemAndSupportedLocalesOverlap() {
        // Given
        final Locale overlappingLocale = Locale.GERMANY;

        // When
        final Locale bestSupportedLocale =
                Locales.getBestSupportedLocaleForDesiredLocales(
                        List.of(overlappingLocale, Locale.UK),
                        List.of(Locale.FRANCE, overlappingLocale));

        // Then
        assertThat(bestSupportedLocale, is(overlappingLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBestSupportedLocaleForDesiredLocales_supportedLocalesMustNotBeEmpty() {
        // Given
        final List<Locale> emptyAppLocales = List.of();

        // When
        Locales.getBestSupportedLocaleForDesiredLocales(
                emptyAppLocales,
                List.of(Locale.JAPANESE));
    }
}