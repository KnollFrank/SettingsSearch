package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createSomeBundleOfBundles;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleEquality.equalBundles;

import android.os.Bundle;

import com.google.common.testing.EqualsTester;

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

    @Test
    public void testEqualsOfConvertedBundles() {
        final Bundle bundle1 = new Bundle();
        bundle1.putString("someKeyA", "someValueA");
        bundle1.putString("someKeyB", "someValueB");

        final Bundle bundle2 = new Bundle();
        bundle2.putString("someKeyB", "someValueB");
        bundle2.putString("someKeyA", "someValueA");

        final Converter<Bundle, String> converter = new BundleConverter();

        new EqualsTester()
                .addEqualityGroup(
                        converter.doForward(bundle1),
                        converter.doForward(bundle2))
                .addEqualityGroup(
                        converter.doForward(createBundle("someKey", "someValue")))
                .addEqualityGroup(
                        converter.doForward(createSomeBundleOfBundles()))
                .testEquals();
    }
}
