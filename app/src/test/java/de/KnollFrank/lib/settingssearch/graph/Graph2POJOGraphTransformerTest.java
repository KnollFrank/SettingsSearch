package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAOTest.getAddPreferences2Screen;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Graph2POJOGraphTransformerTest {

    // FK-TODO: DRY with SearchablePreferenceScreenGraphDAOTest
    @Test
    public void shouldTransformGraph2POJOGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceScreenWithHostClass2POJOConverterTest.PreferenceFragmentTemplate(getAddPreferences2Screen());
                final Fragments fragments = getFragments(preferenceFragment, activity);
                final PreferenceFragmentCompat initializedPreferenceFragment = initializeFragment(preferenceFragment, fragments);
                final PreferenceScreensProvider preferenceScreensProvider =
                        new PreferenceScreensProvider(
                                new PreferenceScreenWithHostProvider(fragments),
                                (preference, hostOfPreference) -> Optional.empty(),
                                (preference, hostOfPreference) -> preference.isVisible(),
                                _preferenceScreenGraph -> {
                                },
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()),
                                initializedPreferenceFragment.getPreferenceManager());
                // FK-TODO: brauchen einen mit initializedPreferenceFragment verbundenes PreferenceFragment, damit der Graph zwei statt nur einen Knoten hat.
                final Graph<PreferenceScreenWithHostClass, PreferenceEdge> entityGraph = preferenceScreensProvider.getSearchablePreferenceScreenGraph(initializedPreferenceFragment);

                // When
                final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph = Graph2POJOGraphTransformer.transformGraph2POJOGraph(entityGraph);

                // Then
                assertThat(
                        Iterables.getOnlyElement(pojoGraph.vertexSet()),
                        is(
                                PreferenceScreenWithHostClass2POJOConverter.convert2POJO(
                                        Iterables.getOnlyElement(entityGraph.vertexSet()))));
            });
        }
    }
}