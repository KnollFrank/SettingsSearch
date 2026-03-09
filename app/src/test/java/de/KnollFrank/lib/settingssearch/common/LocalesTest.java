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
    public void shouldGetDisplayLocale() {
        // Given
        final Locale supportedLocale = Locale.forLanguageTag("de-DE");
        final Locale desiredLocale = Locale.forLanguageTag("de-AT");

        // When
        final Locale displayLocale =
                Locales.getDisplayLocale(
                        List.of(supportedLocale),
                        List.of(desiredLocale));

        // Then
        assertThat(displayLocale, is(supportedLocale));
    }

    @Test
    public void getDisplayLocale_shouldReturnPrimaryLocaleWhenNoMatchIsFound() {
        // Given
        final Locale primaryLocale = Locale.GERMAN;

        // When
        final Locale displayLocale =
                Locales.getDisplayLocale(
                        List.of(primaryLocale, Locale.ENGLISH),
                        List.of(Locale.CHINESE));

        // Then
        assertThat(displayLocale, is(primaryLocale));
    }

    @Test
    public void getDisplayLocale_shouldReturnFirstMatchWhenSystemAndSupportedLocalesOverlap() {
        // Given
        final Locale overlappingLocale = Locale.GERMANY;

        // When
        final Locale displayLocale =
                Locales.getDisplayLocale(
                        List.of(overlappingLocale, Locale.UK),
                        List.of(Locale.FRANCE, overlappingLocale));

        // Then
        assertThat(displayLocale, is(overlappingLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDisplayLocale_supportedLocalesMustNotBeEmpty() {
        // Given
        final List<Locale> emptyAppLocales = List.of();

        // When
        Locales.getDisplayLocale(
                emptyAppLocales,
                List.of(Locale.JAPANESE));
    }
}