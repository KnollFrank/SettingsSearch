package de.KnollFrank.lib.settingssearch.matcher;

import static org.hamcrest.Matchers.allOf;

import androidx.fragment.app.Fragment;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;

public class FragmentClassOfActivityMatchers {

    private FragmentClassOfActivityMatchers() {
    }

    public static Matcher<FragmentClassOfActivity<?>> isEqualTo(final FragmentClassOfActivity<?> expected) {
        return allOf(
                withFragment(Matchers.<Class<? extends Fragment>>is(expected.fragment())),
                withActivityDescription(ActivityDescriptionMatchers.isEqualTo(expected.activityOfFragment()))
        );
    }

    private static Matcher<FragmentClassOfActivity<?>> withFragment(final Matcher<Class<? extends Fragment>> fragmentMatcher) {
        return new FeatureMatcher<>(fragmentMatcher, "the fragment class", "fragment") {

            @Override
            protected Class<? extends Fragment> featureValueOf(final FragmentClassOfActivity<?> actual) {
                return actual.fragment();
            }
        };
    }

    private static Matcher<FragmentClassOfActivity<?>> withActivityDescription(final Matcher<ActivityDescription> activityDescriptionMatcher) {
        return new FeatureMatcher<>(activityDescriptionMatcher, "the activity description", "activityOfFragment") {

            @Override
            protected ActivityDescription featureValueOf(final FragmentClassOfActivity<?> actual) {
                return actual.activityOfFragment();
            }
        };
    }
}
