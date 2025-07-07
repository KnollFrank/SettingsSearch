package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.SearchablePreferenceScreenEntityAndDbDataProvider;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createSomeSearchablePreferenceScreen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntities;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends AppDatabaseTest {

    @Test
    public void shouldGetHostOfPreferencesOfScreen() {
        // Given
        final SearchablePreferenceScreenEntityDAO dao = appDatabase.searchablePreferenceScreenEntityDAO();
        final SearchablePreferenceScreenGraphTestFactory.Data data =
                new SearchablePreferenceScreenGraphTestFactory.Data(
                        5,
                        4,
                        "parentKey",
                        1,
                        2,
                        3,
                        "singleNodeGraph-screen1",
                        "graph-screen1",
                        "graph-screen2");
        final SearchablePreferenceScreenEntityAndDbDataProvider screen = createSomeSearchablePreferenceScreen(data);
        dao.persist(screen.entity(), screen.dbDataProvider());
        final SearchablePreferenceEntity preference =
                SearchablePreferenceEntities
                        .findPreferenceByKey(screen.entity().getAllPreferences(dao), data.PARENT_KEY())
                        .orElseThrow();

        // When
        final SearchablePreferenceScreenEntity hostOfPreference = preference.getHost(appDatabase.searchablePreferenceEntityDAO());

        // Then
        assertThat(hostOfPreference, is(screen.entity()));
    }
}
