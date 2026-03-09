package de.KnollFrank.lib.settingssearch.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.LocaleList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Locale;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class LocalesTest {

    @Test
    public void shouldReturnAppDefaultWhenNoMatchIsFound() {
        // Given
        final Locale appDefault = Locale.GERMAN;

        // When
        final Locale actualUsedLocale =
                Locales.getActualUsedLocale(
                        new LocaleList(),
                        List.of(appDefault, Locale.ENGLISH));

        // Then
        assertThat(actualUsedLocale, is(appDefault));
    }

    @Test
    public void shouldReturnFirstMatchWhenSystemAndAppLocalesOverlap() {
        // Given
        final Locale overlappingLocale = Locale.GERMANY;

        // When
        final Locale actualUsedLocale =
                Locales.getActualUsedLocale(
                        new LocaleList(Locale.FRANCE, overlappingLocale),
                        List.of(overlappingLocale, Locale.UK));

        // Then
        assertThat(actualUsedLocale, is(overlappingLocale));
    }

    @Test
    public void shouldReturnSystemDefaultWhenAppLocalesIsEmpty() {
        // Given
        final Locale systemDefault = Locale.JAPANESE;

        // When
        final Locale actualUsedLocale =
                Locales.getActualUsedLocale(
                        new LocaleList(systemDefault),
                        List.of());

        // Then
        assertThat(actualUsedLocale, is(systemDefault));
    }

    @Test
    public void getCurrentLocaleOrDefault_shouldReturnFirstElementOfList() {
        // Given
        final Locale first = Locale.ITALY;

        // When
        final Locale result =
                Locales.getCurrentLocaleOrDefault(
                        new LocaleList(
                                first,
                                Locale.GERMANY));

        // Then
        assertThat(result, is(first));
    }
}