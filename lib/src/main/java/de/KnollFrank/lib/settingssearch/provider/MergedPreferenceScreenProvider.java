package de.KnollFrank.lib.settingssearch.provider;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.convert2POJO;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.codepoetics.protonpack.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedSearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreenProvider {

    private final PreferenceScreensMerger preferenceScreensMerger;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final boolean cacheMergedPreferenceScreens;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;

    private static MergedPreferenceScreen mergedPreferenceScreen;

    public MergedPreferenceScreenProvider(final PreferenceScreensMerger preferenceScreensMerger,
                                          final SearchableInfoAttribute searchableInfoAttribute,
                                          final boolean cacheMergedPreferenceScreens,
                                          final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                          final Context context) {
        this.preferenceScreensMerger = preferenceScreensMerger;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider) {
        if (!cacheMergedPreferenceScreens) {
            return computeMergedPreferenceScreen(searchablePreferenceScreenGraphProvider);
        }
        if (mergedPreferenceScreen == null) {
            mergedPreferenceScreen = computeMergedPreferenceScreen(searchablePreferenceScreenGraphProvider);
        }
        return mergedPreferenceScreen;
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider) {
        return computeMergedPreferenceScreen(ConnectedSearchablePreferenceScreens.fromSearchablePreferenceScreenGraph(searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph()));
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(final ConnectedSearchablePreferenceScreens screens) {
        // MUST compute A (which just reads screens) before B (which modifies screens)
        // A:
        final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference =
                HostByPreferenceProvider.getHostByPreference(screens.connectedSearchablePreferenceScreens());
        final SearchablePreferenceScreenPOJO mergedSearchablePreferenceScreenPOJO = merge(screens.connectedSearchablePreferenceScreens());
        // aus diesem mergedSearchablePreferenceScreenPOJO den PreferenceScreensMerger.PreferenceScreenAndNonClickablePreferences der nächsten Programmzeile berechnen
        // und ein Mapping zwischen den einzelnen SearchablePreferencePOJOs von mergedSearchablePreferenceScreenPOJO und den SearchablePreferences von preferenceScreenAndNonClickablePreferences zur Verfügung stellen.
        // B:
        final PreferenceScreensMerger.PreferenceScreenAndNonClickablePreferences preferenceScreenAndNonClickablePreferences = destructivelyMergeScreens(screens.connectedSearchablePreferenceScreens());
        return new MergedPreferenceScreen(
                preferenceScreenAndNonClickablePreferences.preferenceScreen(),
                mergedSearchablePreferenceScreenPOJO,
                preferenceScreenAndNonClickablePreferences.nonClickablePreferences(),
                screens.preferencePathByPreference(),
                searchableInfoAttribute,
                new PreferencePathNavigator(hostByPreference, fragmentFactoryAndInitializer, context));
    }

    private PreferenceScreensMerger.PreferenceScreenAndNonClickablePreferences destructivelyMergeScreens(final Set<PreferenceScreenWithHostClass> screens) {
        return preferenceScreensMerger.destructivelyMergeScreens(getPreferenceScreens(new ArrayList<>(screens)));
    }

    private static SearchablePreferenceScreenPOJO merge(final Set<PreferenceScreenWithHostClass> screens) {
        final IdGenerator idGenerator = new IdGenerator();
        return new SearchablePreferenceScreenPOJO(
                "title of merged screen",
                "summary of merged screen",
                StreamUtils
                        .zipWithIndex(screens.stream())
                        .map(preferenceScreenWithHostClassIndexed ->
                                convert2POJO(
                                        preferenceScreenWithHostClassIndexed.getValue(),
                                        Math.toIntExact(preferenceScreenWithHostClassIndexed.getIndex()),
                                        idGenerator))
                        .flatMap(preferenceScreenWithHostClassPOJO ->
                                preferenceScreenWithHostClassPOJO
                                        .preferenceScreen()
                                        .children()
                                        .stream())
                        .collect(Collectors.toList()));
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHostClass> screens) {
        return screens
                .stream()
                .map(PreferenceScreenWithHostClass::preferenceScreen)
                .collect(Collectors.toList());
    }
}
