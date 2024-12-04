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
import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class MergedPreferenceScreenDataTest {

    @Test
    public void shouldPersistAndLoadMergedPreferenceScreenData() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final SearchablePreferencePOJO searchablePreferencePOJO1 =
                        createSearchablePreferencePOJO(
                                1,
                                Optional.of("some title 1"),
                                Optional.of("some summary 1"),
                                Optional.of("searchable info also has a title 1"),
                                POJOTestFactory.createBundle("someKey1", "someValue1"),
                                Optional.of(Either.ofLeft(4711)));
                final SearchablePreferencePOJO searchablePreferencePOJO2 =
                        createSearchablePreferencePOJO(
                                2,
                                Optional.of("some title 2"),
                                Optional.of("some summary 2"),
                                Optional.of("searchable info also has a title 2"),
                                POJOTestFactory.createBundle("someKey2", "someValue2"),
                                Optional.of(Either.ofRight(DrawableAndStringConverter.drawable2String(activity.getResources().getDrawable(R.drawable.smiley, null)))));
                final MergedPreferenceScreenData data =
                        new MergedPreferenceScreenData(
                                Set.of(searchablePreferencePOJO1, searchablePreferencePOJO2),
                                ImmutableMap
                                        .<SearchablePreferencePOJO, PreferencePath>builder()
                                        .put(searchablePreferencePOJO1, new PreferencePath(List.of(searchablePreferencePOJO1)))
                                        .put(searchablePreferencePOJO2, new PreferencePath(List.of(searchablePreferencePOJO1, searchablePreferencePOJO2)))
                                        .build(),
                                ImmutableMap
                                        .<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>>builder()
                                        .put(searchablePreferencePOJO1, PreferenceFragmentCompat.class)
                                        .put(searchablePreferencePOJO2, PreferenceFragmentCompat.class)
                                        .build());
                final var preferences = new ByteArrayOutputStream();
                final var preferencePathByPreference = new ByteArrayOutputStream();
                final var hostByPreference = new ByteArrayOutputStream();

                // When
                MergedPreferenceScreenDataDAO.persist(
                        data,
                        preferences,
                        preferencePathByPreference,
                        hostByPreference);
                final MergedPreferenceScreenData dataActual =
                        MergedPreferenceScreenDataDAO.load(
                                outputStream2InputStream(preferences),
                                outputStream2InputStream(preferencePathByPreference),
                                outputStream2InputStream(hostByPreference));

                // Then
                assertEquals(
                        new ArrayList<>(dataActual.preferences()),
                        new ArrayList<>(data.preferences()));
                assertThat(preferencePathByPreference(dataActual.preferences()), is(preferencePathByPreference(data.preferences())));
                assertThat(hostByPreference(data.preferences()), is(hostByPreference(data.preferences())));
            });
        }
    }

    private static ByteArrayInputStream outputStream2InputStream(final ByteArrayOutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void assertEquals(final SearchablePreferencePOJO actual, final SearchablePreferencePOJO expected) {
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

    private static void assertEquals(final List<SearchablePreferencePOJO> actuals, final List<SearchablePreferencePOJO> expecteds) {
        assertThat(actuals.size(), is(expecteds.size()));
        for (int i = 0; i < actuals.size(); i++) {
            assertEquals(actuals.get(i), expecteds.get(i));
        }
    }

    private static Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference(final Set<SearchablePreferencePOJO> preferences) {
        return attributeByPreference(
                preferences,
                SearchablePreferencePOJO::getPreferencePath);
    }

    private static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference(final Set<SearchablePreferencePOJO> preferences) {
        return attributeByPreference(
                preferences,
                SearchablePreferencePOJO::getHost);
    }

    private static <T> Map<SearchablePreferencePOJO, T> attributeByPreference(
            final Set<SearchablePreferencePOJO> preferences,
            final Function<SearchablePreferencePOJO, T> getAttribute) {
        return PreferencePOJOs
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                getAttribute));
    }
}