package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import org.junit.Test;

import java.util.Optional;

public class OptionalIntegerConverterTest {

    @Test
    public void shouldConvertFromPresentInteger2StringAndBack() {
        shouldConvertFromOptionalInteger2StringAndBack(Optional.of(4711));
    }

    @Test
    public void shouldConvertFromEmptyInteger2StringAndBack() {
        shouldConvertFromOptionalInteger2StringAndBack(Optional.empty());
    }

    private static void shouldConvertFromOptionalInteger2StringAndBack(final Optional<Integer> optionalInteger) {
        test_a_doForward_doBackward_equals_a(new OptionalIntegerConverter(), optionalInteger);
    }
}
