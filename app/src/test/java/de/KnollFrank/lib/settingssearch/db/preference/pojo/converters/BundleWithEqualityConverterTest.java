package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createBundle;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.BundleTestFactory.createSomeBundleOfBundles;

import android.os.Bundle;

import com.google.common.testing.EqualsTester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleWithEquality;

@RunWith(RobolectricTestRunner.class)
public class BundleWithEqualityConverterTest {

    @Test
    public void shouldConvertFromBundle2StringAndBack() {
        // Given
        final BundleWithEquality bundle = new BundleWithEquality(createBundle("someKey", "someValue"));
        final Converter<BundleWithEquality, String> converter = new BundleWithEqualityConverter();

        // When
        final BundleWithEquality bundleActual = converter.doBackward(converter.doForward(bundle));

        // Then
        assertThat(bundleActual, is(bundle));
    }

    @Test
    public void testEqualsOfConvertedBundles() {
        final Bundle bundle1 = new Bundle();
        bundle1.putString("someKeyA", "someValueA");
        bundle1.putString("someKeyB", "someValueB");

        final Bundle bundle2 = new Bundle();
        bundle2.putString("someKeyB", "someValueB");
        bundle2.putString("someKeyA", "someValueA");

        final Converter<BundleWithEquality, String> converter = new BundleWithEqualityConverter();

        new EqualsTester()
                .addEqualityGroup(
                        converter.doForward(new BundleWithEquality(bundle1)),
                        converter.doForward(new BundleWithEquality(bundle2)))
                .addEqualityGroup(
                        converter.doForward(new BundleWithEquality(createBundle("someKey", "someValue"))))
                .addEqualityGroup(
                        converter.doForward(new BundleWithEquality(createSomeBundleOfBundles())))
                .testEquals();
    }
}
