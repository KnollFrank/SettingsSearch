package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.equalBundles;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.createSearchablePreferencePOJO;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.outputStream2InputStream;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
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
                                Optional.of("searchable info also has a title 1"));
                final SearchablePreferencePOJO searchablePreferencePOJO2 =
                        createSearchablePreferencePOJO(
                                2,
                                Optional.of("some title 2"),
                                Optional.of("some summary 2"),
                                Optional.of("searchable info also has a title 2"));
                final MergedPreferenceScreenData data =
                        new MergedPreferenceScreenData(
                                Set.of(searchablePreferencePOJO1, searchablePreferencePOJO2),
                                ImmutableMap
                                        .<SearchablePreferencePOJO, PreferencePath>builder()
                                        .put(searchablePreferencePOJO1, new PreferencePath(List.of(searchablePreferencePOJO1)))
                                        .put(searchablePreferencePOJO2, new PreferencePath(List.of(searchablePreferencePOJO1, searchablePreferencePOJO2)))
                                        .build(),
                                Map.of(searchablePreferencePOJO1, PreferenceFragmentCompat.class));
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
                assertThat(dataActual.preferencePathByPreference(), is(data.preferencePathByPreference()));
                assertThat(dataActual.hostByPreference(), is(data.hostByPreference()));
            });
        }
    }

    private static void assertEquals(final SearchablePreferencePOJO actual, final SearchablePreferencePOJO expected) {
        assertThat(actual.id(), is(expected.id()));
        assertThat(actual.key(), is(expected.key()));
        assertThat(actual.icon(), is(expected.icon()));
        assertThat(actual.layoutResId(), is(expected.layoutResId()));
        assertThat(actual.summary(), is(expected.summary()));
        assertThat(actual.title(), is(expected.title()));
        assertThat(actual.widgetLayoutResId(), is(expected.widgetLayoutResId()));
        assertThat(actual.fragment(), is(expected.fragment()));
        assertThat(actual.visible(), is(expected.visible()));
        assertThat(actual.searchableInfo(), is(expected.searchableInfo()));
        assertThat(equalBundles(actual.extras(), expected.extras()), is(true));
        assertEquals(actual.children(), expected.children());
    }

    private static void assertEquals(final List<SearchablePreferencePOJO> actuals, final List<SearchablePreferencePOJO> expecteds) {
        assertThat(actuals.size(), is(expecteds.size()));
        for (int i = 0; i < actuals.size(); i++) {
            assertEquals(actuals.get(i), expecteds.get(i));
        }
    }
}