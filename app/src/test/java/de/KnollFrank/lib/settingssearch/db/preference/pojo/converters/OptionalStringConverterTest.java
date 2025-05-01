package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import org.junit.Test;

import java.util.Optional;

// FK-TODO: DRY with OptionalIntegerConverterTest
public class OptionalStringConverterTest {

    @Test
    public void shouldConvertFromPresentString2StringAndBack() {
        shouldConvertFromOptionalString2StringAndBack(Optional.of("some string"));
    }

    @Test
    public void shouldConvertFromEmptyString2StringAndBack() {
        shouldConvertFromOptionalString2StringAndBack(Optional.empty());
    }

    private static void shouldConvertFromOptionalString2StringAndBack(final Optional<String> optionalString) {
        test_a_doForward_doBackward_equals_a(new OptionalStringConverter(), optionalString);
    }
}
