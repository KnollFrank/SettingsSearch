package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundle;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundleFactory;
import de.KnollFrank.lib.settingssearch.matcher.BundleMatchers;

@RunWith(RobolectricTestRunner.class)
public class LazyPersistableBundleConverterTest {

    @Test
    public void shouldConvertLazyPersistableBundleAndBack() {
        // Given
        final LazyPersistableBundleConverter converter = new LazyPersistableBundleConverter();
        final LazyPersistableBundle bundle = LazyPersistableBundleFactory.fromBundle(PersistableBundleTestFactory.createSomePersistableBundle());

        // When
        final LazyPersistableBundle bundleActual = converter.convertBackward(converter.convertForward(bundle));

        // Then
        assertThat(bundleActual.get(), BundleMatchers.isEqualTo(bundle.get()));
    }
}
