package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward_equals_a;

import org.junit.Test;

import java.util.Optional;

public class OptionalStringConverterTest {

    private final OptionalStringConverter converter = new OptionalStringConverter();

    @Test
    public void shouldConvertPresentStringToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.of("some string"), converter);
    }

    @Test
    public void shouldConvertEmptyStringToStringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.empty(), converter);
    }
}
