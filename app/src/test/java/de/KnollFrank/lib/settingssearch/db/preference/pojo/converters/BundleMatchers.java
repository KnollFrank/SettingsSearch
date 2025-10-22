package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.PersistableBundle;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import de.KnollFrank.lib.settingssearch.common.PersistableBundleEquality;

public class BundleMatchers {

    public static Matcher<PersistableBundle> isEqualTo(final PersistableBundle expectedBundle) {
        return new TypeSafeMatcher<>() {

            @Override
            protected boolean matchesSafely(final PersistableBundle actualBundle) {
                return PersistableBundleEquality.areBundlesEqual(actualBundle, expectedBundle);
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
