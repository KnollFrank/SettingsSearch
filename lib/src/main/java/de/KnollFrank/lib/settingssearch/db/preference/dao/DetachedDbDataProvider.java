package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class DetachedDbDataProvider implements DbDataProvider {

    protected final DbDataProviderData dbDataProviderData;

    public DetachedDbDataProvider(final DbDataProviderData dbDataProviderData) {
        this.dbDataProviderData = dbDataProviderData;
    }

    @Override
    public Set<SearchablePreferenceEntity> getAllPreferences(final SearchablePreferenceScreenEntity screen) {
        return Maps.get(dbDataProviderData.allPreferencesBySearchablePreferenceScreen(), screen).orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return Maps.get(dbDataProviderData.hostByPreference(), preference).orElseThrow();
    }

    @Override
    public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
        return Maps.get(dbDataProviderData.childrenByPreference(), preference).orElseThrow();
    }

    @Override
    public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
        return Maps.get(dbDataProviderData.predecessorByPreference(), preference).orElseThrow();
    }
}
