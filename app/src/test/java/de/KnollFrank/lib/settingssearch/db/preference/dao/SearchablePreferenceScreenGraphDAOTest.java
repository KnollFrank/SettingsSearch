package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getFragments;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter.PreferenceScreenWithHostClassWithMap;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.graph.Host2HostClassTransformer;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.Preferences2SearchablePreferencesTransformer;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest {

    @Test
    public void shouldPersistAndLoadSearchablePreferenceScreenGraph() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceFragmentCompat preferenceFragment = new PreferenceFragmentTemplate(getAddPreferences2Screen());
                final Fragments fragments = getFragments(preferenceFragment, activity);
                final PreferenceManager preferenceManager = initializeFragment(preferenceFragment, fragments).getPreferenceManager();
                final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph = createSomePojoPreferenceScreenGraph(preferenceFragment, fragments);
                final var outputStream = new ByteArrayOutputStream();

                // When
                SearchablePreferenceScreenGraphDAO.persist(preferenceScreenGraph, outputStream);
                final Graph<PreferenceScreenWithHostClassWithMap, PreferenceEdge> preferenceScreenGraphActual =
                        SearchablePreferenceScreenGraphDAO.load(
                                convert(outputStream),
                                preferenceManager);

                // Then
                assertThat(preferenceScreenGraphActual.vertexSet().size(), is(preferenceScreenGraph.vertexSet().size()));
                assertThat(preferenceScreenGraphActual.edgeSet().size(), is(preferenceScreenGraph.edgeSet().size()));
                final PreferenceEdge edge = Iterables.getOnlyElement(preferenceScreenGraphActual.edgeSet());
                assertThat(edge.preference.getTitle(), is("preference connected to TestPreferenceFragment"));
            });
        }
    }

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> createSomePojoPreferenceScreenGraph(
            final PreferenceFragmentCompat preferenceFragment,
            final Fragments fragments) {
        return Host2HostClassTransformer.transformHost2HostClass(
                transformPreferences2SearchablePreferences(
                        createSomeEntityPreferenceScreenGraph(
                                preferenceFragment,
                                fragments)));
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> createSomeEntityPreferenceScreenGraph(final PreferenceFragmentCompat preferenceFragment, final Fragments fragments) {
        return new PreferenceScreenGraphProvider(
                new PreferenceScreenWithHostProvider(fragments, PreferenceFragmentCompat::getPreferenceScreen),
                (preference, hostOfPreference) -> Optional.empty())
                .getPreferenceScreenGraph(preferenceFragment.getClass().getName());
    }

    private static Graph<PreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return new Preferences2SearchablePreferencesTransformer(
                new SearchableInfoAndDialogInfoProvider(
                        preference -> Optional.empty(),
                        (preference, hostOfPreference) -> Optional.empty()))
                .transformPreferences2SearchablePreferences(preferenceScreenGraph);
    }

    public static BiConsumer<PreferenceScreen, Context> getAddPreferences2Screen() {
        return new BiConsumer<>() {

            @Override
            public void accept(final PreferenceScreen screen, final Context context) {
                {
                    final SearchablePreference searchablePreference = createParent(context);
                    screen.addPreference(searchablePreference);
                    searchablePreference.addPreference(createChild(context, Optional.of("some searchable info of first child")));
                    searchablePreference.addPreference(createChild(context, Optional.of("some searchable info of second child")));
                }

                screen.addPreference(createConnectionToFragment(TestPreferenceFragment.class, context));
            }

            private static SearchablePreference createParent(final Context context) {
                final SearchablePreference searchablePreference =
                        new SearchablePreference(
                                context,
                                Optional.of("some searchable info"));
                searchablePreference.setKey("parentKey");
                searchablePreference.setLayoutResource(15);
                return searchablePreference;
            }

            private static SearchablePreference createChild(final Context context, final Optional<String> searchableInfo) {
                final SearchablePreference child =
                        new SearchablePreference(
                                context,
                                searchableInfo);
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

    private static InputStream convert(final OutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static class TestPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            setPreferenceScreen(screen);
        }
    }
}