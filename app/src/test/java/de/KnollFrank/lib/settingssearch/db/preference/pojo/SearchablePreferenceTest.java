package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test.makeGetPreferencePathWorkOnPreferences;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreferencePOJO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceTest extends AppDatabaseTest {

    @Test
    public void shouldGetPreferencePath() {
        // Given
        final SearchablePreference predecessor =
                createSearchablePreferencePOJO(
                        "predecessor",
                        Optional.empty());

        final SearchablePreference parent =
                createSearchablePreferencePOJO(
                        "parent",
                        Optional.of(predecessor));
        makeGetPreferencePathWorkOnPreferences(List.of(parent, predecessor), appDatabase);

        // When
        final PreferencePath preferencePath = parent.getPreferencePath();

        // Then
        assertThat(preferencePath, is(new PreferencePath(List.of(predecessor, parent))));
    }
}
