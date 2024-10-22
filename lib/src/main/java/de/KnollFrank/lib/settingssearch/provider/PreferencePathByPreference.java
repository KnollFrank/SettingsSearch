package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferencePathByPojoPreferenceProvider;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;
import de.KnollFrank.lib.settingssearch.graph.HostClassAndMapFromNodesRemover;

class PreferencePathByPreference {

    public static Map<Preference, PreferencePath> getPreferencePathByPreference(
            final Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> pojoGraph,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return convertPojoKeys2EntityKeys(
                PreferencePathByPojoPreferenceProvider.getPreferencePathByPojoPreference(
                        HostClassAndMapFromNodesRemover.removeHostClassAndMapFromNodes(pojoGraph)),
                pojoEntityMap);
    }

    private static Map<Preference, PreferencePath> convertPojoKeys2EntityKeys(
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return preferencePathByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> pojoEntityMap.get(entry.getKey()),
                                Entry::getValue));
    }
}
