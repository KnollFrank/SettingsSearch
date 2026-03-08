package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward_equals_a;

import org.junit.Test;

import java.util.Locale;

public class LocaleConverterTest {

    @Test
    public void shouldConvertLocale() {
        test_a_convertForward_convertBackward_equals_a(Locale.forLanguageTag("de-CH"), new LocaleConverter());
    }
}