package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.test.BundleEquality.equalBundles;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BundleConverterTest {

    @Test
    public void shouldConvertFromBundle2StringAndBack() {
        // Given
        final Bundle bundle = createBundle("someKey", "someValue");
        final Converter<Bundle, String> converter = new BundleConverter();

        // When
        final Bundle bundleActual = converter.doBackward(converter.doForward(bundle));

        // Then
        assertThat(equalBundles(bundle, bundleActual), is(true));
    }
}
