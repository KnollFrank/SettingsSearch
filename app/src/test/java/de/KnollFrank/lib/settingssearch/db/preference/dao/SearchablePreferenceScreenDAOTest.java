package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntityTestFactory.SearchablePreferenceScreenEntityAndDbDataProvider;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntityTestFactory.createSomeSearchablePreferenceScreen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntities;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldGetHostOfPreferencesOfScreen() {
        // Given
        final SearchablePreferenceScreenEntityDAO dao = preferencesRoomDatabase.searchablePreferenceScreenEntityDAO();
        final SearchablePreferenceScreenGraphTestFactory.Data data =
                new SearchablePreferenceScreenGraphTestFactory.Data(
                        "5",
                        "4",
                        "parentKey",
                        "1",
                        "2",
                        "3",
                        "singleNodeGraph-screen1",
                        "tree-screen1",
                        "tree-screen2");
        final SearchablePreferenceScreenEntityAndDbDataProvider screen = createSomeSearchablePreferenceScreen(data);
        dao.persist(Set.of(screen.entity()), screen.dbDataProvider());
        final SearchablePreferenceEntity preference =
                SearchablePreferenceEntities
                        .findPreferenceByKey(screen.entity().getAllPreferencesOfPreferenceHierarchy(dao), data.PARENT_KEY())
                        .orElseThrow();

        // When
        final SearchablePreferenceScreenEntity hostOfPreference = preference.getHost(preferencesRoomDatabase.searchablePreferenceEntityDAO());

        // Then
        assertThat(hostOfPreference, is(screen.entity()));
    }
}
