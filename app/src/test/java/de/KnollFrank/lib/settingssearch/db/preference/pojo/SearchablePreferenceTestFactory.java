package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;

public class SearchablePreferenceTestFactory {

    private static final IdGenerator idGenerator = IdGeneratorFactory.createIdGeneratorStartingAt(1);

    public static SearchablePreference createSearchablePreference(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId) {
        return new SearchablePreference(
                id,
                "some key",
                title,
                summary,
                iconResourceIdOrIconPixelData,
                androidx.preference.R.layout.preference,
                0,
                Optional.of("some fragment"),
                Optional.empty(),
                true,
                searchableInfo,
                parentId,
                predecessorId,
                "-1");
    }

    public static SearchablePreference createSearchablePreference(
            final String title,
            final Optional<SearchablePreference> predecessor) {
        return new SearchablePreference(
                idGenerator.nextId(),
                title,
                Optional.of(title),
                Optional.empty(),
                Optional.empty(),
                0,
                0,
                Optional.empty(),
                Optional.empty(),
                true,
                Optional.empty(),
                Optional.empty(),
                predecessor.map(SearchablePreference::getId),
                "-1");
    }

    public static SearchablePreference createSearchablePreference(
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData) {
        return createSearchablePreference(
                1,
                title,
                summary,
                searchableInfo,
                iconResourceIdOrIconPixelData,
                Optional.empty());
    }

    public static SearchablePreference createSearchablePreference(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Optional<SearchablePreference> predecessor) {
        return new SearchablePreference(
                id,
                "some key",
                title,
                summary,
                iconResourceIdOrIconPixelData,
                androidx.preference.R.layout.preference,
                0,
                Optional.of("some fragment"),
                Optional.empty(),
                true,
                searchableInfo,
                Optional.empty(),
                predecessor.map(SearchablePreference::getId),
                "-1");
    }

    public static SearchablePreference createSearchablePreference(final String key) {
        return new SearchablePreference(
                1,
                key,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                16,
                0,
                Optional.empty(),
                Optional.empty(),
                true,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                "-1");
    }

    public static SearchablePreference copyPreferenceAndSetPredecessor(final SearchablePreference preference,
                                                                       final Optional<SearchablePreference> predecessor) {
        return createSearchablePreference(
                preference.getTitle().orElseThrow(),
                predecessor);
    }
}
