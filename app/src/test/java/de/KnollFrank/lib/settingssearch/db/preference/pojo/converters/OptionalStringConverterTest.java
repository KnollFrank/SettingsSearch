package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import org.junit.Test;

import java.util.Optional;

public class OptionalStringConverterTest {

    private final OptionalStringConverter converter = new OptionalStringConverter();

    @Test
    public void shouldConvertFromPresentString2StringAndBack() {
        test_a_doForward_doBackward_equals_a(converter, Optional.of("some string"));
    }

    @Test
    public void shouldConvertFromEmptyString2StringAndBack() {
        test_a_doForward_doBackward_equals_a(converter, Optional.empty());
    }
}
