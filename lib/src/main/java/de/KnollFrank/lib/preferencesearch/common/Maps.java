package de.KnollFrank.lib.preferencesearch.common;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Maps {

    public static <K, V> Map<K, V> merge(final Collection<Map<K, V>> maps) {
        return maps
                .stream()
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue));
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
}
