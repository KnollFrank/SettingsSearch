package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.matcher;

import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.os.PersistableBundle;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import de.KnollFrank.lib.settingssearch.ActivityDescription;

public class ActivityDescriptionMatchers {

    private ActivityDescriptionMatchers() {
    }

    public static Matcher<ActivityDescription> isEqualTo(final ActivityDescription expected) {
        return allOf(
                withActivity(Matchers.<Class<? extends Activity>>is(expected.activity())),
                withArguments(BundleMatchers.isEqualTo(expected.arguments()))
        );
    }

    private static Matcher<ActivityDescription> withActivity(final Matcher<Class<? extends Activity>> activityMatcher) {
        return new FeatureMatcher<>(activityMatcher, "the activity class", "activity") {

            @Override
            protected Class<? extends Activity> featureValueOf(final ActivityDescription actual) {
                return actual.activity();
            }
        };
    }

    private static Matcher<ActivityDescription> withArguments(final Matcher<PersistableBundle> argumentsMatcher) {
        return new FeatureMatcher<>(argumentsMatcher, "the arguments bundle", "arguments") {

            @Override
            protected PersistableBundle featureValueOf(final ActivityDescription actual) {
                return actual.arguments();
            }
        };
    }
}
