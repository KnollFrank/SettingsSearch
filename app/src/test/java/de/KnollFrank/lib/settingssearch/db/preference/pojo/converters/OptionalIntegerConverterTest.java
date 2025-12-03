package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_convertForward_convertBackward_equals_a;

import org.junit.Test;

import java.util.Optional;

public class OptionalIntegerConverterTest {

    private final OptionalIntegerConverter converter = new OptionalIntegerConverter();

    @Test
    public void shouldConvertFromPresentInteger2StringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.of(4711), converter);
    }

    @Test
    public void shouldConvertFromEmptyInteger2StringAndBack() {
        test_a_convertForward_convertBackward_equals_a(Optional.empty(), converter);
    }
}
