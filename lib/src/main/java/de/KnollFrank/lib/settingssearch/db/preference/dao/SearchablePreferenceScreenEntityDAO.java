package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
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

    public void persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
                        final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        for (final var searchablePreferenceScreen : searchablePreferenceScreens) {
            persist(searchablePreferenceScreen, dbDataProvider);
        }
        invalidateCaches();
    }

    public void persist(final SearchablePreferenceScreenEntity searchablePreferenceScreen,
                        final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        searchablePreferenceDAO.persist(searchablePreferenceScreen.getAllPreferencesOfPreferenceHierarchy(dbDataProvider));
        _persist(searchablePreferenceScreen);
        invalidateCaches();
    }

    // FK-TODO: remove method?
    public void remove(final SearchablePreferenceScreenEntity screen) {
        searchablePreferenceDAO.remove(screen.getAllPreferencesOfPreferenceHierarchy(this));
        _removeByIds(Set.of(screen.id()));
        invalidateCaches();
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

    public void removeAll() {
        searchablePreferenceDAO.removeAll();
        _removeAll();
        invalidateCaches();
    }

    @Query("DELETE FROM SearchablePreferenceScreenEntity WHERE id IN (:ids)")
    protected abstract void _removeByIds(Set<String> ids);

    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE graphId = :graphId")
    protected abstract List<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreensByGraphId(final Locale graphId);

    @Insert
    protected abstract void _persist(SearchablePreferenceScreenEntity searchablePreferenceScreen);

    @Query("SELECT " +
            "screen.*, " +
            "preference.id AS pref_id, " +
            "preference.key AS pref_key, " +
            "preference.title AS pref_title, " +
            "preference.summary AS pref_summary, " +
            "preference.iconResourceIdOrIconPixelData AS pref_iconResourceIdOrIconPixelData, " +
            "preference.layoutResId AS pref_layoutResId, " +
            "preference.widgetLayoutResId AS pref_widgetLayoutResId, " +
            "preference.fragment AS pref_fragment, " +
            "preference.classNameOfReferencedActivity AS pref_classNameOfReferencedActivity, " +
            "preference.visible AS pref_visible, " +
            "preference.extras AS pref_extras, " +
            "preference.searchableInfo AS pref_searchableInfo, " +
            "preference.parentId AS pref_parentId, " +
            "preference.predecessorId AS pref_predecessorId, " +
            "preference.searchablePreferenceScreenId AS pref_searchablePreferenceScreenId " +
            "FROM SearchablePreferenceScreenEntity AS screen " +
            "JOIN SearchablePreferenceEntity AS preference ON screen.id = preference.searchablePreferenceScreenId")
    protected abstract List<PreferenceWithScreen> _getPreferenceWithScreen();

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen = Optional.of(computeAllPreferencesBySearchablePreferenceScreen());
        }
        return allPreferencesBySearchablePreferenceScreen.orElseThrow();
    }

    // FK-TODO: refactor
    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> computeAllPreferencesBySearchablePreferenceScreen() {
        final List<PreferenceWithScreen> preferenceWithScreens = _getPreferenceWithScreen();
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
        final List<PreferenceWithScreen> preferenceWithScreens = _getPreferenceWithScreen();
        final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference = new HashMap<>();
        for (final PreferenceWithScreen preferenceWithScreen : preferenceWithScreens) {
            hostByPreference.put(preferenceWithScreen.preference(), preferenceWithScreen.screen());
        }
        return hostByPreference;
    }

    @Query("DELETE FROM SearchablePreferenceScreenEntity")
    protected abstract void _removeAll();

    public void invalidateCaches() {
        allPreferencesBySearchablePreferenceScreen = Optional.empty();
        hostByPreference = Optional.empty();
    }
}
