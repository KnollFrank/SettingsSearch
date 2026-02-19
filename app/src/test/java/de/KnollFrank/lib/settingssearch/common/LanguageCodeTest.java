package de.KnollFrank.lib.settingssearch.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.testing.EqualsTester;

import org.junit.Test;

import java.util.Locale;

public class LanguageCodeTest {

    @Test
    public void shouldCreateLanguageCodeForValidCode() {
        // Given
        final String code = "de";

        // When
        final LanguageCode languageCode = new LanguageCode(code);

        // Then
        assertThat(languageCode.code(), is(code));
    }

    @Test
    public void shouldCreateLanguageCodeFromLocale() {
        // Given
        final Locale locale = Locale.GERMAN;

        // When
        final LanguageCode languageCode = LanguageCode.from(locale);

        // Then
        assertThat(languageCode.code(), is(locale.getLanguage()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidCode() {
        new LanguageCode("invalid-code");
    }

    @Test
    public void testEquals() {
        new EqualsTester()
                // hebrew
                .addEqualityGroup(new LanguageCode("he"), new LanguageCode("iw"))
                // indonesian
                .addEqualityGroup(new LanguageCode("id"), new LanguageCode("in"))
                .addEqualityGroup(new LanguageCode("de"))
                .testEquals();
    }
}
