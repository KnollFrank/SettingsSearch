package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceWithScreen.SCREEN_PREFIX;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceWithScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@Dao
public abstract class SearchablePreferenceScreenEntityDAO implements SearchablePreferenceScreenEntity.DbDataProvider {

    private final SearchablePreferenceEntityDAO searchablePreferenceDAO;
    // FK-TODO: remove cache? -> Caching ist für die Performance gut, solange die Invalidierung stimmt.
    private Optional<Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();
    // FK-TODO: remove cache? -> Behalten wir vorerst bei.
    private Optional<Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>> hostByPreference = Optional.empty();

    public SearchablePreferenceScreenEntityDAO(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.searchablePreferenceDAO = preferencesRoomDatabase.searchablePreferenceEntityDAO();
    }

    @Transaction
    public DatabaseState persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
                                 final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        final DatabaseState preferenceDatabaseState =
                searchablePreferenceDAO.persist(
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

    public Set<SearchablePreferenceScreenEntity> findSearchablePreferenceScreensByGraphId(final Locale graphId) {
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
        final DatabaseState preferenceDatabaseState = searchablePreferenceDAO.removeAll();
        final DatabaseState screenDatabaseState = wrapper.removeAll();
        final DatabaseState databaseState = preferenceDatabaseState.combine(screenDatabaseState);
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    @Transaction
    public DatabaseState remove(final Collection<SearchablePreferenceScreenEntity> screens) {
        final DatabaseState preferenceDatabaseState = searchablePreferenceDAO.remove(getAllPreferences(screens, this));
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
    protected abstract List<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreensByGraphId(final Locale graphId);

    @Insert
    protected abstract List<Long> persistAndReturnInsertedRowIds(Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens);

    @Query("SELECT " +
            "screen.id AS " + SCREEN_PREFIX + "id, " +
            "screen.host AS " + SCREEN_PREFIX + "host, " +
            "screen.title AS " + SCREEN_PREFIX + "title, " +
            "screen.summary AS " + SCREEN_PREFIX + "summary, " +
            "screen.graphId AS " + SCREEN_PREFIX + "graphId, " +
            "preference.* " +
            "FROM SearchablePreferenceEntity AS preference " +
            "JOIN SearchablePreferenceScreenEntity AS screen ON preference.searchablePreferenceScreenId = screen.id")
    // FK-TODO: getPreferenceWithScreen() braucht einen Cache, da sie zwei mal unabhängig aufgerufen wird und so eine doppelte Berechnung ausführt.
    protected abstract List<PreferenceWithScreen> getPreferenceWithScreen();

    @Query("DELETE FROM SearchablePreferenceScreenEntity")
    protected abstract int removeAllAndReturnNumberOfDeletedRows();

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen = Optional.of(computeAllPreferencesBySearchablePreferenceScreen());
        }
        return allPreferencesBySearchablePreferenceScreen.orElseThrow();
    }

    // FK-TODO: refactor
    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> computeAllPreferencesBySearchablePreferenceScreen() {
        final List<PreferenceWithScreen> preferenceWithScreens = getPreferenceWithScreen();
        final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> result = new HashMap<>();
        for (final PreferenceWithScreen preferenceWithScreen : preferenceWithScreens) {
            result
                    .computeIfAbsent(
                            preferenceWithScreen.screen(),
                            k -> new HashSet<>())
                    .add(preferenceWithScreen.preference());
        }
        return result;
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference() {
        if (hostByPreference.isEmpty()) {
            hostByPreference = Optional.of(computeHostByPreference());
        }
        return hostByPreference.orElseThrow();
    }

    // FK-TODO: refactor
    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> computeHostByPreference() {
        final List<PreferenceWithScreen> preferenceWithScreens = getPreferenceWithScreen();
        final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference = new HashMap<>();
        for (final PreferenceWithScreen preferenceWithScreen : preferenceWithScreens) {
            hostByPreference.put(preferenceWithScreen.preference(), preferenceWithScreen.screen());
        }
        return hostByPreference;
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
    }

    private class Wrapper {

        public DatabaseState persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens) {
            final List<Long> insertedRowIds = persistAndReturnInsertedRowIds(searchablePreferenceScreens);
            return DatabaseStateFactory.fromInsertedRowIds(insertedRowIds);
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
