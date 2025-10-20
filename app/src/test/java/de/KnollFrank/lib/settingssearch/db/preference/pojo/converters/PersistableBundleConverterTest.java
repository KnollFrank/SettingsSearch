package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import static org.hamcrest.MatcherAssert.assertThat;

import android.os.PersistableBundle;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PersistableBundleConverterTest {

    @Test
    public void shouldConvertPersistableBundleAndBack() {
        // Given
        final PersistableBundleConverter converter = new PersistableBundleConverter();
        final PersistableBundle bundle = createSomePersistableBundle();

        // When
        final PersistableBundle bundleActual = converter.doBackward(converter.doForward(bundle));

        // Then
        assertThat(bundleActual, isEqualTo(bundle));
    }

    private static PersistableBundle createSomePersistableBundle() {
        final PersistableBundle bundle = new PersistableBundle();
        bundle.putStringArray("some string key", new String[]{"some string 1", "some string 2"});
        bundle.putInt("some int key", 815);
        return bundle;
    }

    private static Matcher<PersistableBundle> isEqualTo(final PersistableBundle expectedBundle) {
        return new TypeSafeMatcher<>() {

            @Override
            protected boolean matchesSafely(final PersistableBundle actualBundle) {
                return BundleUtils.areBundlesEqual(actualBundle, expectedBundle);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ein Bundle inhaltlich gleich wie ").appendValue(expectedBundle);
            }

            @Override
            protected void describeMismatchSafely(final PersistableBundle actualBundle, final Description mismatchDescription) {
                mismatchDescription.appendText("war aber ").appendValue(actualBundle);
            }
        };
    }
}
