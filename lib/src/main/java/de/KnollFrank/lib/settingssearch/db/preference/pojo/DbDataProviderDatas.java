package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class DbDataProviderDatas {

    private DbDataProviderDatas() {
    }

    public static DbDataProviderData merge(final Collection<DbDataProviderData> dbDataProviderDatas) {
        return new DbDataProviderData(
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::nodesByTree, de.KnollFrank.lib.settingssearch.common.Sets::union),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::allPreferencesBySearchablePreferenceScreen, de.KnollFrank.lib.settingssearch.common.Sets::union),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::hostByPreference, (v1, v2) -> v2),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::predecessorByPreference, (v1, v2) -> v2),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::childrenByPreference, de.KnollFrank.lib.settingssearch.common.Sets::union));
    }

    private static <K, V> Map<K, V> mapThenMerge(
            final Collection<DbDataProviderData> dbDataProviderDatas,
            final Function<DbDataProviderData, Map<K, V>> mapper,
            final java.util.function.BinaryOperator<V> mergeFunction) {
        return Maps.merge(
                dbDataProviderDatas
                        .stream()
                        .map(mapper)
                        .collect(Collectors.toSet()),
                mergeFunction);
    }
}
