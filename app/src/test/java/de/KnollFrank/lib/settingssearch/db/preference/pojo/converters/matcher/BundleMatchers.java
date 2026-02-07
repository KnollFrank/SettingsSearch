package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.matcher;

import android.os.PersistableBundle;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class BundleMatchers {

    private BundleMatchers() {
    }

    public static Matcher<PersistableBundle> isEqualTo(final PersistableBundle expectedBundle) {
        return new TypeSafeMatcher<>() {

            @Override
            protected boolean matchesSafely(final PersistableBundle actualBundle) {
                return PersistableBundleEquality.areBundlesEqual(actualBundle, expectedBundle);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a bundle with the same content as ").appendValue(expectedBundle);
            }

            @Override
            protected void describeMismatchSafely(final PersistableBundle actualBundle, final Description mismatchDescription) {
                mismatchDescription.appendText("war aber ").appendValue(actualBundle);
            }
        };
    }
}
