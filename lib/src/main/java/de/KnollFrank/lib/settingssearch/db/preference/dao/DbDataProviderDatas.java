package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Collection;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;

public class DbDataProviderDatas {

    public static DbDataProviderData merge(final Collection<DbDataProviderData> dbDataProviderDatas) {
        return new DbDataProviderData(
                Maps.merge(
                        dbDataProviderDatas
                                .stream()
                                // FK-TODO: DRY with Maps.merge
                                .map(DbDataProviderData::allPreferencesBySearchablePreferenceScreen)
                                .collect(Collectors.toSet())),
                Maps.merge(
                        dbDataProviderDatas
                                .stream()
                                .map(DbDataProviderData::hostByPreference)
                                .collect(Collectors.toSet())),
                Maps.merge(
                        dbDataProviderDatas
                                .stream()
                                .map(DbDataProviderData::predecessorByPreference)
                                .collect(Collectors.toSet())),
                Maps.merge(
                        dbDataProviderDatas
                                .stream()
                                .map(DbDataProviderData::childrenByPreference)
                                .collect(Collectors.toSet())));
    }
}
