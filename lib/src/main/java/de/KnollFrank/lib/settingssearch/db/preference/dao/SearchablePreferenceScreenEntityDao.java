package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceWithScreen.SCREEN_PREFIX;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceWithScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceWithScreens;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@Dao
public abstract class SearchablePreferenceScreenEntityDao implements SearchablePreferenceScreenEntity.DbDataProvider {

    private final SearchablePreferenceEntityDao searchablePreferenceDao;
    private Optional<Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();
    private Optional<Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>> hostByPreference = Optional.empty();
    private Optional<List<PreferenceWithScreen>> preferenceWithScreens = Optional.empty();

    public SearchablePreferenceScreenEntityDao(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.searchablePreferenceDao = preferencesRoomDatabase.searchablePreferenceEntityDao();
    }

    @Transaction
    public DatabaseState persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
                                 final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        final DatabaseState preferenceDatabaseState =
                searchablePreferenceDao.persist(
                        getAllPreferences(
                                searchablePreferenceScreens,
                                dbDataProvider));
        final DatabaseState screenDatabaseState = wrapper.persist(searchablePreferenceScreens);
        final DatabaseState databaseState = preferenceDatabaseState.combine(screenDatabaseState);
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    public Set<SearchablePreferenceScreenEntity> findSearchablePreferenceScreensByGraphId(final LanguageCode graphId) {
        // FK-TODO: add cache? -> Ja, das wäre eine gute Idee für eine weitere Optimierung.
        return new HashSet<>(_findSearchablePreferenceScreensByGraphId(graphId));
    }

    @Override
    public Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy(final SearchablePreferenceScreenEntity screen) {
        return Maps
                .get(getAllPreferencesBySearchablePreferenceScreen(), screen)
                .orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return Maps
                .get(getHostByPreference(), preference)
                .orElseThrow();
    }

    @Transaction
    public DatabaseState removeAll() {
        final DatabaseState preferenceDatabaseState = searchablePreferenceDao.removeAll();
        final DatabaseState screenDatabaseState = wrapper.removeAll();
        final DatabaseState databaseState = preferenceDatabaseState.combine(screenDatabaseState);
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    @Transaction
    public DatabaseState remove(final Collection<SearchablePreferenceScreenEntity> screens) {
        final DatabaseState preferenceDatabaseState = searchablePreferenceDao.remove(getAllPreferences(screens, this));
        final DatabaseState screenDatabaseState = wrapper.remove(screens);
        final DatabaseState databaseState = preferenceDatabaseState.combine(screenDatabaseState);
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    @Query("DELETE FROM SearchablePreferenceScreenEntity WHERE id IN (:ids)")
    protected abstract int removeByIdsInBatchAndReturnNumberOfDeletedRows(Set<String> ids);

    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE graphId = :graphId")
    protected abstract List<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreensByGraphId(final LanguageCode graphId);

    @Insert
    protected abstract /* List<Long> degrades performance */ void persistInBatch(Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens);

    @Query("SELECT " +
            "screen.id AS " + SCREEN_PREFIX + "id, " +
            "screen.host_preferenceFragment AS " + SCREEN_PREFIX + "host_preferenceFragment, " +
            "screen.host_activity AS " + SCREEN_PREFIX + "host_activity, " +
            "screen.host_arguments AS " + SCREEN_PREFIX + "host_arguments, " +
            "screen.title AS " + SCREEN_PREFIX + "title, " +
            "screen.summary AS " + SCREEN_PREFIX + "summary, " +
            "screen.graphId AS " + SCREEN_PREFIX + "graphId, " +
            "preference.* " +
            "FROM SearchablePreferenceEntity AS preference " +
            "JOIN SearchablePreferenceScreenEntity AS screen ON preference.searchablePreferenceScreenId = screen.id")
    protected abstract List<PreferenceWithScreen> _computePreferenceWithScreens();

    @Query("DELETE FROM SearchablePreferenceScreenEntity")
    protected abstract int removeAllAndReturnNumberOfDeletedRows();

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen = Optional.of(computeAllPreferencesBySearchablePreferenceScreen());
        }
        return allPreferencesBySearchablePreferenceScreen.orElseThrow();
    }

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> computeAllPreferencesBySearchablePreferenceScreen() {
        return getAllPreferencesBySearchablePreferenceScreen(getPreferenceWithScreens());
    }

    private static Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen(final List<PreferenceWithScreen> preferenceWithScreens) {
        return preferenceWithScreens
                .stream()
                .collect(
                        Collectors.groupingBy(
                                PreferenceWithScreen::screen,
                                Collectors.mapping(
                                        PreferenceWithScreen::preference,
                                        Collectors.toSet())));
    }

    private List<PreferenceWithScreen> getPreferenceWithScreens() {
        if (preferenceWithScreens.isEmpty()) {
            preferenceWithScreens = Optional.of(computePreferenceWithScreens());
        }
        return preferenceWithScreens.orElseThrow();
    }

    private List<PreferenceWithScreen> computePreferenceWithScreens() {
        return PreferenceWithScreens.internObjects(_computePreferenceWithScreens());
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference() {
        if (hostByPreference.isEmpty()) {
            hostByPreference = Optional.of(computeHostByPreference());
        }
        return hostByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> computeHostByPreference() {
        return getHostByPreference(getPreferenceWithScreens());
    }

    private static Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference(final List<PreferenceWithScreen> preferenceWithScreens) {
        return preferenceWithScreens
                .stream()
                .collect(
                        Collectors.toMap(
                                PreferenceWithScreen::preference,
                                PreferenceWithScreen::screen));
    }

    private static Set<SearchablePreferenceEntity> getAllPreferences(
            final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
            final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        return Sets.union(
                searchablePreferenceScreens
                        .stream()
                        .map(screen -> screen.getAllPreferencesOfPreferenceHierarchy(dbDataProvider))
                        .collect(Collectors.toList()));
    }

    private void invalidateCaches() {
        allPreferencesBySearchablePreferenceScreen = Optional.empty();
        hostByPreference = Optional.empty();
        preferenceWithScreens = Optional.empty();
    }

    private class Wrapper {

        public DatabaseState persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens) {
            persistInBatch(searchablePreferenceScreens);
            return DatabaseState.fromDatabaseChanged(!searchablePreferenceScreens.isEmpty());
        }

        public DatabaseState removeAll() {
            final int numberOfDeletedRows = removeAllAndReturnNumberOfDeletedRows();
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }

        public DatabaseState remove(final Collection<SearchablePreferenceScreenEntity> screens) {
            final int numberOfDeletedRows = removeByIdsInBatchAndReturnNumberOfDeletedRows(getIds(screens));
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }

        private static Set<String> getIds(final Collection<SearchablePreferenceScreenEntity> screens) {
            return screens
                    .stream()
                    .map(SearchablePreferenceScreenEntity::id)
                    .collect(Collectors.toSet());
        }
    }

    private final Wrapper wrapper = new Wrapper();
}
