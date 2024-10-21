package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.drawable2String;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceFromPOJOConverterTest {

    @Test
    public void shouldConvertSearchablePreferencePOJO2SearchablePreference() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = createSomePreferenceFragment(activity);
                final PreferenceScreen preferenceScreen = preferenceFragment.getPreferenceScreen();
                final String key = "someKey";
                final String value = "someValue";
                final SearchablePreferencePOJO pojo =
                        POJOTestFactory.createSomeSearchablePreferencePOJO(
                                createBundle(key, value),
                                activity.getResources());

                // When
                SearchablePreferenceFromPOJOConverter.addConvertedPOJO2Parent(pojo, preferenceScreen);

                // Then
                assertEquals(
                        (SearchablePreference) preferenceScreen.getPreference(0),
                        pojo);
            });
        }
    }

    public static PreferenceFragmentCompat createSomePreferenceFragment(final TestActivity activity) {
        final PreferenceFragmentCompat preferenceFragment = new SearchablePreferenceScreenGraphDAOTest.TestPreferenceFragment();
        return initializeFragment(
                preferenceFragment,
                getFragments(
                        preferenceFragment,
                        activity));
    }

    private static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    private static void assertEquals(final SearchablePreference actual, final SearchablePreferencePOJO expected) {
        assertThat(actual.getKey(), is(expected.key()));
        assertThat(drawable2String(actual.getIcon()), is(expected.icon()));
        assertThat(actual.getLayoutResource(), is(expected.layoutResId()));
        assertThat(Optional.ofNullable(actual.getSummary()), is(expected.summary()));
        assertThat(Optional.ofNullable(actual.getTitle()), is(expected.title()));
        assertThat(actual.getWidgetLayoutResource(), is(expected.widgetLayoutResId()));
        assertThat(Optional.ofNullable(actual.getFragment()), is(expected.fragment()));
        assertThat(actual.isVisible(), is(expected.visible()));
        assertThat(actual.getSearchableInfo(), is(Optional.ofNullable(expected.searchableInfo())));
        assertThat(equalBundles(actual.getExtras(), expected.extras()), is(true));
        assertEquals(
                SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(actual)),
                expected.children());
    }

    private static void assertEquals(final List<SearchablePreference> actuals, final List<SearchablePreferencePOJO> expecteds) {
        assertThat(actuals.size(), is(expecteds.size()));
        for (int i = 0; i < actuals.size(); i++) {
            assertEquals(actuals.get(i), expecteds.get(i));
        }
    }

    // adapted from https://stackoverflow.com/a/13238729
    private static boolean equalBundles(final Bundle one, final Bundle two) {
        if (one.size() != two.size())
            return false;

        Set<String> setOne = new HashSet<>(one.keySet());
        setOne.addAll(two.keySet());
        Object valueOne;
        Object valueTwo;

        for (String key : setOne) {
            if (!one.containsKey(key) || !two.containsKey(key))
                return false;

            valueOne = one.get(key);
            valueTwo = two.get(key);
            if (valueOne instanceof Bundle && valueTwo instanceof Bundle &&
                    !equalBundles((Bundle) valueOne, (Bundle) valueTwo)) {
                return false;
            } else if (valueOne == null) {
                if (valueTwo != null)
                    return false;
            } else if (!valueOne.equals(valueTwo))
                return false;
        }

        return true;
    }
}