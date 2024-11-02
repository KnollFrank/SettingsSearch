package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.equalBundles;
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

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class MergedPreferenceScreenDataTest {

    @Test
    public void shouldPersistAndLoadMergedPreferenceScreenData() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final MergedPreferenceScreenData data =
                        new MergedPreferenceScreenData(
                                Set.of(
                                        POJOTestFactory.createSearchablePreferencePOJO(
                                                1,
                                                Optional.of("some title 1"),
                                                Optional.of("some summary 1"),
                                                Optional.of("searchable info also has a title 1")),
                                        POJOTestFactory.createSearchablePreferencePOJO(
                                                2,
                                                Optional.of("some title 2"),
                                                Optional.of("some summary 2"),
                                                Optional.of("searchable info also has a title 2"))),
                                ImmutableMap
                                        .<Integer, List<Integer>>builder()
                                        .put(1, List.of(1))
                                        .put(2, List.of(1, 2))
                                        .build(),
                                Map.of(1, PreferenceFragmentCompat.class));
                final var outputStream1 = new ByteArrayOutputStream();
                final var outputStream2 = new ByteArrayOutputStream();
                final var outputStream3 = new ByteArrayOutputStream();

                // When
                MergedPreferenceScreenDataDAO.persist(data, outputStream1, outputStream2, outputStream3);
                final MergedPreferenceScreenData dataActual =
                        MergedPreferenceScreenDataDAO.load(
                                outputStream2InputStream(outputStream1),
                                outputStream2InputStream(outputStream2),
                                outputStream2InputStream(outputStream3));

                // Then
                assertEquals(
                        new ArrayList<>(dataActual.allPreferencesForSearch()),
                        new ArrayList<>(data.allPreferencesForSearch()));
                assertThat(dataActual.preferencePathByPreferenceId(), is(data.preferencePathByPreferenceId()));
                assertThat(dataActual.hostByPreferenceId(), is(data.hostByPreferenceId()));
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