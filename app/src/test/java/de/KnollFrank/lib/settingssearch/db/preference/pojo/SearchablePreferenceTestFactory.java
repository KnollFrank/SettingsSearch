package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;

public class SearchablePreferenceTestFactory {

    private static final IdGenerator idGenerator = IdGeneratorFactory.createIdGeneratorStartingAt(1);

    private SearchablePreferenceTestFactory() {
    }

    public static SearchablePreference createSearchablePreference(final String key) {
        return new SearchablePreference(
                "id-" + key,
                key,
                Optional.of("Title " + key),
                Optional.empty(),
                Optional.empty(),
                16,
                0,
                Optional.empty(),
                Optional.empty(),
                true,
                new PersistableBundle(),
                Optional.empty(),
                Set.of());
    }

    public static SearchablePreference createSearchablePreference(
            final String id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Set<SearchablePreference> children) {
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
                new PersistableBundle(),
                searchableInfo,
                children);
    }

    public static SearchablePreference createSearchablePreferenceWithTitle(final String title) {
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
                new PersistableBundle(),
                Optional.empty(),
                Set.of());
    }

    public static SearchablePreference createSearchablePreference(
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData) {
        return createSearchablePreference(
                "1",
                title,
                summary,
                searchableInfo,
                iconResourceIdOrIconPixelData);
    }

    public static SearchablePreference createSearchablePreference(
            final String id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData) {
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
                new PersistableBundle(),
                searchableInfo,
                Set.of());
    }
}
