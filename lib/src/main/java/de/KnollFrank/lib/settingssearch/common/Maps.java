package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Maps {

    public static <K, V> Map<K, V> merge(final Collection<Map<K, V>> maps) {
        return Maps
                .getEntryStream(maps)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue));
    }

    // adapted from https://stackoverflow.com/a/31954986
    public static <K, V> BiMap<K, V> mergeBiMaps(final Collection<BiMap<K, V>> biMaps) {
        return Maps
                .getEntryStream(biMaps)
                .collect(
                        HashBiMap::create,
                        (bm, t) -> bm.put(t.getKey(), t.getValue()),
                        BiMap::putAll);
    }

    public static <K, V> Optional<V> get(final Map<K, V> map, final K key) {
        return Optional.ofNullable(map.get(key));
    }

    public static <K, V> Map<K, V> filterPresentValues(final Map<K, Optional<V>> map) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().isPresent())
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> entry.getValue().get()));
    }

    private static <K, V> Stream<Entry<K, V>> getEntryStream(final Collection<? extends Map<K, V>> maps) {
        return maps
                .stream()
                .map(Map::entrySet)
                .flatMap(Set::stream);
    }
}
