package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class DbDataProviderDatas {

    public static DbDataProviderData merge(final Collection<DbDataProviderData> dbDataProviderDatas) {
        return new DbDataProviderData(
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::allPreferencesBySearchablePreferenceScreen),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::hostByPreference),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::predecessorByPreference),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::childrenByPreference));
    }

    private static <K, V> Map<K, V> mapThenMerge(
            final Collection<DbDataProviderData> dbDataProviderDatas,
            final Function<DbDataProviderData, Map<K, V>> mapper) {
        return Maps.merge(
                dbDataProviderDatas
                        .stream()
                        .map(mapper)
                        .collect(Collectors.toSet()));
    }
}
