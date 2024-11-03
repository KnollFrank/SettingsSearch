package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

// FK-TODO: refactor
public class MergedPreferenceScreenDataDAO {

    public static void persist(final MergedPreferenceScreenData mergedPreferenceScreenData,
                               final OutputStream preferences,
                               final OutputStream preferencePathByPreference,
                               final OutputStream hostByPreference) {
        JsonDAO.persist(mergedPreferenceScreenData.preferences(), preferences);
        JsonDAO.persist(transform(mergedPreferenceScreenData.preferencePathByPreference()), preferencePathByPreference);
        JsonDAO.persist(transform7(mergedPreferenceScreenData.hostByPreference()), hostByPreference);
    }

    private static Map<Integer, Class<? extends PreferenceFragmentCompat>> transform7(final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
        return hostByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().id(),
                                Entry::getValue));
    }

    private static Map<Integer, List<Integer>> transform(final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        return preferencePathByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().id(),
                                entry -> transform(entry.getValue())));
    }

    private static List<Integer> transform(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(SearchablePreferencePOJO::id)
                .collect(Collectors.toList());
    }

    public static MergedPreferenceScreenData load(final InputStream preferences,
                                                  final InputStream preferencePathByPreference,
                                                  final InputStream hostByPreference) {
        final Set<SearchablePreferencePOJO> _preferences =
                JsonDAO.load(
                        preferences,
                        new TypeToken<>() {
                        });
        final Map<Integer, SearchablePreferencePOJO> preferenceById = getPreferenceById(_preferences);
        return new MergedPreferenceScreenData(
                _preferences,
                transform(
                        JsonDAO.load(
                                preferencePathByPreference,
                                new TypeToken<>() {
                                }),
                        preferenceById),
                transform8(
                        JsonDAO.load(
                                hostByPreference,
                                new TypeToken<>() {
                                }),
                        preferenceById));
    }

    private static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> transform8(
            final Map<Integer, Class<? extends PreferenceFragmentCompat>> hostByPreferenceId,
            final Map<Integer, SearchablePreferencePOJO> preferenceById) {
        return hostByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> preferenceById.get(entry.getKey()),
                                Entry::getValue));
    }

    private static Map<Integer, SearchablePreferencePOJO> getPreferenceById(final Set<SearchablePreferencePOJO> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferencePOJO::id,
                                Function.identity()));
    }

    private static Map<SearchablePreferencePOJO, PreferencePath> transform(
            final Map<Integer, List<Integer>> preferencePathIdByPreferenceId,
            final Map<Integer, SearchablePreferencePOJO> preferenceById) {
        return preferencePathIdByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> preferenceById.get(entry.getKey()),
                                entry ->
                                        new PreferencePath(
                                                entry
                                                        .getValue()
                                                        .stream()
                                                        .map(preferenceById::get)
                                                        .collect(Collectors.toList()))));
    }
}
