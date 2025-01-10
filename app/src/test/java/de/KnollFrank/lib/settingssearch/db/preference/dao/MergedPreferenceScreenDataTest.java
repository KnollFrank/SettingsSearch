package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.createSearchablePreferencePOJO;
import static de.KnollFrank.lib.settingssearch.test.TestHelper.equalBundles;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathsAndHostsSetter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class MergedPreferenceScreenDataTest {

    @Test
    public void shouldPersistAndLoadMergedPreferenceScreenData() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final SearchablePreference searchablePreference1 =
                        createSearchablePreferencePOJO(
                                1,
                                Optional.of("some title 1"),
                                Optional.of("some summary 1"),
                                Optional.of("searchable info also has a title 1"),
                                POJOTestFactory.createBundle("someKey1", "someValue1"),
                                Optional.of(Either.ofLeft(4711)));
                final SearchablePreference searchablePreference2 =
                        createSearchablePreferencePOJO(
                                2,
                                Optional.of("some title 2"),
                                Optional.of("some summary 2"),
                                Optional.of("searchable info also has a title 2"),
                                POJOTestFactory.createBundle("someKey2", "someValue2"),
                                Optional.of(Either.ofRight(DrawableAndStringConverter.drawable2String(activity.getResources().getDrawable(R.drawable.smiley, null)))));
                final PreferencePathsAndHostsSetter preferencePathsAndHostsSetter =
                        new PreferencePathsAndHostsSetter(
                                ImmutableMap
                                        .<SearchablePreference, PreferencePath>builder()
                                        .put(searchablePreference1, new PreferencePath(List.of(searchablePreference1)))
                                        .put(searchablePreference2, new PreferencePath(List.of(searchablePreference1, searchablePreference2)))
                                        .build(),
                                ImmutableMap
                                        .<SearchablePreference, Class<? extends PreferenceFragmentCompat>>builder()
                                        .put(searchablePreference1, PreferenceFragmentCompat.class)
                                        .put(searchablePreference2, PreferenceFragmentCompat.class)
                                        .build());
                final Set<SearchablePreference> data =
                        Set.of(
                                searchablePreference1,
                                searchablePreference2);
                preferencePathsAndHostsSetter.setPreferencePathsAndHosts(data);
                final var preferences = new ByteArrayOutputStream();
                final var preferencePathByPreference = new ByteArrayOutputStream();
                final var hostByPreference = new ByteArrayOutputStream();

                // When
                MergedPreferenceScreenDataDAO.persist(
                        data,
                        preferences,
                        preferencePathByPreference,
                        hostByPreference);
                final Set<SearchablePreference> dataActual =
                        MergedPreferenceScreenDataDAO.load(
                                outputStream2InputStream(preferences),
                                outputStream2InputStream(preferencePathByPreference),
                                outputStream2InputStream(hostByPreference));

                // Then
                assertEquals(
                        new ArrayList<>(dataActual),
                        new ArrayList<>(data));
                assertThat(preferencePathByPreference(dataActual), is(preferencePathByPreference(data)));
                assertThat(hostByPreference(data), is(hostByPreference(data)));
            });
        }
    }

    private static ByteArrayInputStream outputStream2InputStream(final ByteArrayOutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void assertEquals(final SearchablePreference actual, final SearchablePreference expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getKey(), is(expected.getKey()));
        assertThat(actual.getIconResourceIdOrIconPixelData(), is(expected.getIconResourceIdOrIconPixelData()));
        assertThat(actual.getLayoutResId(), is(expected.getLayoutResId()));
        assertThat(actual.getSummary(), is(expected.getSummary()));
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getWidgetLayoutResId(), is(expected.getWidgetLayoutResId()));
        assertThat(actual.getFragment(), is(expected.getFragment()));
        assertThat(actual.isVisible(), is(expected.isVisible()));
        assertThat(actual.getSearchableInfo(), is(expected.getSearchableInfo()));
        assertThat(equalBundles(actual.getExtras(), expected.getExtras()), is(true));
        assertEquals(actual.getChildren(), expected.getChildren());
    }

    private static void assertEquals(final List<SearchablePreference> actuals, final List<SearchablePreference> expecteds) {
        assertThat(actuals.size(), is(expecteds.size()));
        for (int i = 0; i < actuals.size(); i++) {
            assertEquals(actuals.get(i), expecteds.get(i));
        }
    }

    private static Map<SearchablePreference, PreferencePath> preferencePathByPreference(final Set<SearchablePreference> preferences) {
        return attributeByPreference(
                preferences,
                SearchablePreference::getPreferencePath);
    }

    private static Map<SearchablePreference, Class<? extends PreferenceFragmentCompat>> hostByPreference(final Set<SearchablePreference> preferences) {
        return attributeByPreference(
                preferences,
                SearchablePreference::getHost);
    }

    private static <T> Map<SearchablePreference, T> attributeByPreference(
            final Set<SearchablePreference> preferences,
            final Function<SearchablePreference, T> getAttribute) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                getAttribute));
    }
}