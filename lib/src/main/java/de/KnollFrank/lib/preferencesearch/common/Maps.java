package de.KnollFrank.lib.preferencesearch.common;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
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

    public static <K, V> Map<K, V> merge(final Collection<Map<K, V>> maps,
                                         final BinaryOperator<V> mergeFunction) {
        return Maps
                .getEntryStream(maps)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                mergeFunction));
    }

    public static <K, V> Optional<V> get(final Map<K, V> map, final K key) {
        return map.containsKey(key) ?
                Optional.of(map.get(key)) :
                Optional.empty();
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

    private static <K, V> Stream<Entry<K, V>> getEntryStream(final Collection<Map<K, V>> maps) {
        return maps
                .stream()
                .map(Map::entrySet)
                .flatMap(Set::stream);
    }
}
