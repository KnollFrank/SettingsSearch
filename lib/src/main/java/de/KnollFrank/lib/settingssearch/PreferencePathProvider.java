package de.KnollFrank.lib.settingssearch;

import com.google.common.collect.ImmutableList;

import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenWithinTree;
import de.KnollFrank.lib.settingssearch.graph.GraphConverterFactory;

// FK-TODO: refactor
public class PreferencePathProvider {

    public static PreferencePath getPreferencePath(final SearchablePreferenceOfHostWithinTree target) {
        return new PreferencePath(getPathPreferences(target));
    }

    private static List<SearchablePreferenceOfHostWithinTree> getPathPreferences(final SearchablePreferenceOfHostWithinTree target) {
        return ImmutableList
                .<SearchablePreferenceOfHostWithinTree>builder()
                .addAll(
                        getPathPreferences(
                                new SearchablePreferenceScreenWithinTree(
                                        target.hostOfPreference(),
                                        target.treeContainingPreference())))
                .add(target)
                .build();
    }

    private static List<SearchablePreferenceOfHostWithinTree> getPathPreferences(final SearchablePreferenceScreenWithinTree target) {
        return getEdgePreferences(target.getGraphPath());
    }

    private static List<SearchablePreferenceOfHostWithinTree> getEdgePreferences(final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath) {
        final var graphContainingPreference =
                new Tree<>(
                        GraphConverterFactory
                                .createSearchablePreferenceScreenGraphConverter()
                                .toGuava(graphPath.getGraph()));
        return graphPath
                .getEdgeList()
                .stream()
                .map(searchablePreferenceEdge ->
                             new SearchablePreferenceOfHostWithinTree(
                                     searchablePreferenceEdge.preference,
                                     // FK-TODO: use converted guava Tree instead of jgraph graphPath.getGraph()
                                     graphPath.getGraph().getEdgeSource(searchablePreferenceEdge),
                                     graphContainingPreference))
                .collect(Collectors.toList());
    }
}
