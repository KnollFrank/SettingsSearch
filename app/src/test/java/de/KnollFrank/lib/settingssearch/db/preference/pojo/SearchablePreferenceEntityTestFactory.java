package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;

public class SearchablePreferenceEntityTestFactory {

    private static final IdGenerator idGenerator = IdGeneratorFactory.createIdGeneratorStartingAt(1);

    public static SearchablePreferenceEntity createSearchablePreference(
            final String id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Optional<String> parentId,
            final Optional<String> predecessorId) {
        return new SearchablePreferenceEntity(
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

    public static SearchablePreferenceEntity createSearchablePreference(
            final String title,
            final Optional<SearchablePreferenceEntity> predecessor) {
        return new SearchablePreferenceEntity(
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
                predecessor.map(SearchablePreferenceEntity::id),
                "-1");
    }

    public static SearchablePreferenceEntity createSearchablePreference(
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData) {
        return createSearchablePreference(
                "1",
                title,
                summary,
                searchableInfo,
                iconResourceIdOrIconPixelData,
                Optional.empty());
    }

    public static SearchablePreferenceEntity createSearchablePreference(
            final String id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Optional<SearchablePreferenceEntity> predecessor) {
        return new SearchablePreferenceEntity(
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
                predecessor.map(SearchablePreferenceEntity::id),
                "-1");
    }

    public static SearchablePreferenceEntity createSearchablePreference(final String key) {
        return new SearchablePreferenceEntity(
                "1",
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
}
