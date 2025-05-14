package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleWithEquality;

@RunWith(RobolectricTestRunner.class)
public class OptionalBundleWithEqualityConverterTest {

    private final Converter<Optional<BundleWithEquality>, String> converter = new OptionalBundleWithEqualityConverter();

    @Test
    public void shouldConvertFromPresentBundle2StringAndBack() {
        test_a_doForward_doBackward_equals_a(Optional.of(new BundleWithEquality(createBundle("key", "value"))), converter);
    }

    @Test
    public void shouldConvertFromEmptyBundle2StringAndBack() {
        test_a_doForward_doBackward_equals_a(Optional.empty(), converter);
    }
}
