package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createScreen;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceTestFactory.createSearchablePreference;

import com.google.common.graph.ValueGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldGetPreferencePath() {
        // Given
        final SearchablePreference predecessor = createSearchablePreference("predecessor");
        final SearchablePreference parent = createSearchablePreference("parent");
        final SearchablePreferenceScreen predecessorScreen = createScreen(predecessor);
        final SearchablePreferenceScreen parentScreen = createScreen(parent);
        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"}) final Tree<SearchablePreferenceScreen, SearchablePreference, ValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree =
                new Tree<>(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraphBuilder()
                                .putEdgeValue(predecessorScreen, parentScreen, predecessor)
                                .build());
        final SearchablePreferenceOfHostWithinTree _predecessor = new SearchablePreferenceOfHostWithinTree(predecessor, predecessorScreen, tree);
        final SearchablePreferenceOfHostWithinTree _parent = new SearchablePreferenceOfHostWithinTree(parent, parentScreen, tree);

        // When
        final PreferencePath preferencePath = _parent.getPreferencePath();

        // Then
        assertThat(preferencePath, is(new PreferencePath(List.of(_predecessor, _parent))));
    }
}
