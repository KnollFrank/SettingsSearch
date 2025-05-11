package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleEquality.equalBundles;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ConverterTest.test_a_doForward_doBackward_equals_a;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

@RunWith(RobolectricTestRunner.class)
public class OptionalBundleConverterTest {

    private final OptionalBundleConverter converter = new OptionalBundleConverter();

    @Test
    public void shouldConvertFromPresentBundle2StringAndBack() {
        // Given
        final Optional<Bundle> bundle = Optional.of(createBundle("key", "value"));

        // When
        final Optional<Bundle> bundleActual = converter.doBackward(converter.doForward(bundle));

        // Then
        assertThat(equalBundles(bundle.orElseThrow(), bundleActual.orElseThrow()), is(true));
    }

    @Test
    public void shouldConvertFromEmptyBundle2StringAndBack() {
        test_a_doForward_doBackward_equals_a(Optional.empty(), converter);
    }
}
