package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
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
                                entry -> entry.getValue().orElseThrow()));
    }

    private static <K, V> Stream<Entry<K, V>> getEntryStream(final Collection<? extends Map<K, V>> maps) {
        return maps
                .stream()
                .map(Map::entrySet)
                .flatMap(Set::stream);
    }

    // FK-TODO: implement unsing mapKeysAndValues()
    public static <Key, Value, ValueMapped> Map<Key, ValueMapped> mapValues(
            final Map<Key, Value> map,
            final Function<Value, ValueMapped> valueMapper) {
        return map
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> valueMapper.apply(entry.getValue())));
    }

    public static <K, V> Map<K, V> mapEachKeyToValue(final Set<K> keys, final V value) {
        return keys
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                key -> value));
    }

    public static <K, V, KMapped, VMapped> Map<KMapped, VMapped> mapKeysAndValues(
            final Map<K, V> map,
            final Function<K, KMapped> keyMapper,
            final Function<V, VMapped> valueMapper) {
        return map
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> keyMapper.apply(entry.getKey()),
                                entry -> valueMapper.apply(entry.getValue())));
    }

    public static <K, V> Map<K, V> filter(final Map<K, V> map, final BiPredicate<K, V> predicate) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getKey(), entry.getValue()))
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                Entry::getValue));
    }
}
