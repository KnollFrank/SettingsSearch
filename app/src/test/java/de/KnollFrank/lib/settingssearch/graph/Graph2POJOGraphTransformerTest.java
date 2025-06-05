package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenGraphProvider1Test.makeGetPreferencePathWorkOnGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverterTest.getInstantiateAndInitializeFragment;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.DST_PREFERENCE_ID;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.PREFERENCE_CONNECTING_SRC_2_DST_ID;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createPojoGraph;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;
import static de.KnollFrank.lib.settingssearch.graph.PojoGraphs.getPreferences;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Graph2POJOGraphTransformerTest extends AppDatabaseTest {

    @Test
    public void shouldTransformGraph2POJOGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final InstantiateAndInitializeFragment instantiateAndInitializeFragment = getInstantiateAndInitializeFragment(preferenceFragment, activity);
                final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph =
                        PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(preferenceFragment, instantiateAndInitializeFragment, activity);
                final Graph2POJOGraphTransformer graph2POJOGraphTransformer =
                        new Graph2POJOGraphTransformer(
                                new PreferenceScreen2SearchablePreferenceScreenConverter(
                                        new Preference2SearchablePreferenceConverter(
                                                (preference, hostOfPreference) -> Optional.empty(),
                                                new SearchableInfoAndDialogInfoProvider(
                                                        preference -> Optional.empty(),
                                                        (preference, hostOfPreference) -> Optional.empty()),
                                                IdGeneratorFactory.createIdGeneratorStartingAt(1))),
                                new DefaultPreferenceFragmentIdProvider());

                // When
                final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> pojoGraph =
                        removeMapFromPojoNodes(
                                graph2POJOGraphTransformer.transformGraph2POJOGraph(
                                        entityGraph));

                // Then
                makeGetPreferencePathWorkOnGraph(pojoGraph, appDatabase);
                // check graph:
                assertThat(pojoGraph, is(createPojoGraph(preferenceFragment.getClass())));
                {
                    final var data = getPreferenceAndExpectedPredecessorOfPreference(pojoGraph);
                    final SearchablePreferenceEntity preference = data.preference();
                    final SearchablePreferenceEntity expectedPredecessorOfPreference = data.expectedPredecessorOfPreference();
                    // check predecessor:
                    assertThat("predecessor of " + preference, preference.getPredecessor(appDatabase.searchablePreferenceEntityDAO()), is(Optional.of(expectedPredecessorOfPreference)));
                }
            });
        }
    }

    private static BiConsumer<PreferenceScreen, Context> getAddPreferences2Screen() {
        return new BiConsumer<>() {

            @Override
            public void accept(final PreferenceScreen screen, final Context context) {
                {
                    final PreferenceCategory preference = createParent(context);
                    screen.addPreference(preference);
                    preference.addPreference(createChild(context));
                    preference.addPreference(createChild(context));
                }
                screen.addPreference(createConnectionToFragment(PreferenceFragmentWithSinglePreference.class, context));
            }

            private static PreferenceCategory createParent(final Context context) {
                final PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                preferenceCategory.setKey("parentKey");
                preferenceCategory.setLayoutResource(15);
                return preferenceCategory;
            }

            private static Preference createChild(final Context context) {
                final Preference child = new Preference(context);
                child.setKey("some child key");
                child.setLayoutResource(16);
                return child;
            }

            private static Preference createConnectionToFragment(final Class<? extends Fragment> fragment,
                                                                 final Context context) {
                final Preference preference = new Preference(context);
                preference.setFragment(fragment.getName());
                preference.setTitle("preference connected to " + fragment.getSimpleName());
                preference.setKey("some key");
                return preference;
            }
        };
    }

    public static class PreferenceFragmentWithSinglePreference extends PreferenceFragmentTemplate {

        public PreferenceFragmentWithSinglePreference() {
            super(context -> {
                final Preference preference = new Preference(context);
                preference.setKey("some dst key");
                preference.setTitle("some title for dst key");
                return List.of(preference);
            });
        }
    }

    private PreferenceAndExpectedPredecessorOfPreference getPreferenceAndExpectedPredecessorOfPreference(
            final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> pojoGraphExpected) {
        final Set<SearchablePreferenceEntity> searchablePreferences = getPreferences(pojoGraphExpected.vertexSet(), appDatabase.searchablePreferenceScreenEntityDAO());
        return new PreferenceAndExpectedPredecessorOfPreference(
                getDstPreference(searchablePreferences),
                getPreferenceConnectingSrc2Dst(searchablePreferences));
    }

    private record PreferenceAndExpectedPredecessorOfPreference(
            SearchablePreferenceEntity preference,
            SearchablePreferenceEntity expectedPredecessorOfPreference) {
    }

    private static SearchablePreferenceEntity getPreferenceConnectingSrc2Dst(final Set<SearchablePreferenceEntity> searchablePreferences) {
        return getPreferenceById(searchablePreferences, PREFERENCE_CONNECTING_SRC_2_DST_ID);
    }

    private static SearchablePreferenceEntity getDstPreference(final Set<SearchablePreferenceEntity> searchablePreferences) {
        return getPreferenceById(searchablePreferences, DST_PREFERENCE_ID);
    }

    private static SearchablePreferenceEntity getPreferenceById(final Set<SearchablePreferenceEntity> searchablePreferences,
                                                                final int id) {
        return SearchablePreferences
                .findPreferenceByPredicate(
                        searchablePreferences,
                        searchablePreference -> searchablePreference.getId() == id)
                .orElseThrow();
    }
}