package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverterTest.equalBundles;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.outputStream2InputStream;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class MergedPreferenceScreenDAOTest {

    @Test
    public void shouldPersistAndLoadMergedPreferenceScreen() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final MergedPreferenceScreen mergedPreferenceScreen =
                        new MergedPreferenceScreen(
                                Set.of(
                                        POJOTestFactory.createSearchablePreferencePOJO(
                                                Optional.of("some title"),
                                                Optional.of("some summary"),
                                                Optional.of("searchable info also has a title"))),
                                null,
                                null);
                final var outputStream = new ByteArrayOutputStream();

                // When
                MergedPreferenceScreenDAO.persist(mergedPreferenceScreen, outputStream);
                final MergedPreferenceScreen mergedPreferenceScreenActual =
                        MergedPreferenceScreenDAO.load(
                                outputStream2InputStream(outputStream));

                // Then
                assertEquals(
                        new ArrayList<>(mergedPreferenceScreenActual.allPreferencesForSearch()),
                        new ArrayList<>(mergedPreferenceScreen.allPreferencesForSearch()));
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