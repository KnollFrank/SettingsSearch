package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassWithMap;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.graph.MapFromPojoNodesRemover.removeMapFromPojoNodes;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Graph2POJOGraphTransformerTest {

    @Test
    public void shouldTransformGraph2POJOGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final IFragments fragments = getFragments(preferenceFragment, activity);
                final Graph<PreferenceScreenWithHost, PreferenceEdge> entityGraph =
                        PojoGraphTestFactory.createSomeEntityPreferenceScreenGraph(preferenceFragment, fragments, activity);
                final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter =
                        new Preference2SearchablePreferenceConverter(
                                (preference, hostOfPreference) -> Optional.empty(),
                                new SearchableInfoAndDialogInfoProvider(
                                        preference -> Optional.empty(),
                                        (preference, hostOfPreference) -> Optional.empty()),
                                new IdGenerator());

                // When
                final Graph<PreferenceScreenWithHostClassWithMap, SearchablePreferenceEdge> pojoGraph =
                        Graph2POJOGraphTransformer.transformGraph2POJOGraph(
                                entityGraph,
                                preference2SearchablePreferenceConverter);

                // Then
                assertThat(removeMapFromPojoNodes(pojoGraph), is(createPojoGraph(preferenceFragment.getClass())));
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

                screen.addPreference(createConnectionToFragment(TestPreferenceFragment.class, context));
            }

            private static PreferenceCategory createParent(final Context context) {
                final PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                preferenceCategory.setKey("parentKey");
                preferenceCategory.setLayoutResource(15);
                return preferenceCategory;
            }

            private static Preference createChild(final Context context) {
                final Preference child = new Preference(context);
                child.setLayoutResource(16);
                return child;
            }

            private static Preference createConnectionToFragment(final Class<? extends Fragment> fragment,
                                                                 final Context context) {
                final Preference preference = new Preference(context);
                preference.setFragment(fragment.getName());
                preference.setTitle("preference connected to " + fragment.getSimpleName());
                return preference;
            }
        };
    }

    private static Graph<PreferenceScreenWithHostClass, SearchablePreferenceEdge> createPojoGraph(final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreference preferenceConnectingSrc2Dst =
                new SearchablePreference(
                        4,
                        Optional.empty(),
                        Optional.empty(),
                        2131427444,
                        Optional.empty(),
                        Optional.of("preference connected to TestPreferenceFragment"),
                        0,
                        Optional.of(TestPreferenceFragment.class.getName()),
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        new Bundle(),
                        List.of());
        return DefaultDirectedGraph
                .<PreferenceScreenWithHostClass, SearchablePreferenceEdge>createBuilder(SearchablePreferenceEdge.class)
                .addEdge(
                        createSrc(preferenceConnectingSrc2Dst, host),
                        createDst(),
                        new SearchablePreferenceEdge(preferenceConnectingSrc2Dst))
                .build();
    }

    private static PreferenceScreenWithHostClass createSrc(final SearchablePreference preferenceConnectingSrc2Dst,
                                                           final Class<? extends PreferenceFragmentCompat> host) {
        return new PreferenceScreenWithHostClass(
                1,
                new SearchablePreferenceScreen(
                        "screen title",
                        "screen summary",
                        List.of(
                                new SearchablePreference(
                                        1,
                                        Optional.of("parentKey"),
                                        Optional.empty(),
                                        15,
                                        Optional.empty(),
                                        Optional.empty(),
                                        0,
                                        Optional.empty(),
                                        Optional.empty(),
                                        true,
                                        Optional.empty(),
                                        new Bundle(),
                                        List.of(
                                                new SearchablePreference(
                                                        2,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        16,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        0,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        true,
                                                        Optional.empty(),
                                                        new Bundle(),
                                                        List.of()),
                                                new SearchablePreference(
                                                        3,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        16,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        0,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        true,
                                                        Optional.empty(),
                                                        new Bundle(),
                                                        List.of()))),
                                preferenceConnectingSrc2Dst)),
                host);
    }

    private static PreferenceScreenWithHostClass createDst() {
        return new PreferenceScreenWithHostClass(
                2,
                new SearchablePreferenceScreen(null, null, List.of()),
                TestPreferenceFragment.class);
    }
}