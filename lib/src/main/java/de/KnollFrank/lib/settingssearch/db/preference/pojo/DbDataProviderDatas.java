package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Collection;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Sets;

public class DbDataProviderDatas {

    private DbDataProviderDatas() {
    }

    public static DbDataProviderData merge(final Collection<DbDataProviderData> dbDataProviderDatas) {
        return new DbDataProviderData(
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::nodesByTree, Sets::union),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::allPreferencesBySearchablePreferenceScreen, Sets::union),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::hostByPreference, (v1, v2) -> v2),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::predecessorByPreference, (v1, v2) -> v2),
                mapThenMerge(dbDataProviderDatas, DbDataProviderData::childrenByPreference, Sets::union));
    }

    private static <K, V> Map<K, V> mapThenMerge(
            final Collection<DbDataProviderData> dbDataProviderDatas,
            final Function<DbDataProviderData, Map<K, V>> mapper,
            final BinaryOperator<V> mergeFunction) {
        return Maps.merge(
                dbDataProviderDatas
                        .stream()
                        .map(mapper)
                        .collect(Collectors.toSet()),
                mergeFunction);
    }
}
