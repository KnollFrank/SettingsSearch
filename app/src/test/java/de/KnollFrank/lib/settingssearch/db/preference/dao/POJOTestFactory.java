package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class POJOTestFactory {

    private static final IdGenerator idGenerator = IdGeneratorFactory.createIdGeneratorStartingAt1();

    public static SearchablePreference createSearchablePreferencePOJO(
            final String title,
            final Class<? extends PreferenceFragmentCompat> host,
            final Optional<SearchablePreference> predecessor) {
        return new SearchablePreference(
                idGenerator.nextId(),
                title,
                Optional.empty(),
                0,
                Optional.empty(),
                Optional.of(title),
                0,
                Optional.empty(),
                Optional.empty(),
                true,
                Optional.empty(),
                new Bundle(),
                host,
                List.of(),
                predecessor);
    }

    public static SearchablePreference createSearchablePreferencePOJO(
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData) {
        return createSearchablePreferencePOJO(
                1,
                title,
                summary,
                searchableInfo,
                createBundle("someKey", "someValue"),
                iconResourceIdOrIconPixelData,
                TestPreferenceFragment.class,
                Optional.empty());
    }

    public static SearchablePreference createSearchablePreferencePOJO(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Bundle extras,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Class<? extends PreferenceFragmentCompat> host,
            final Optional<SearchablePreference> predecessor) {
        return new SearchablePreference(
                id,
                "some key",
                iconResourceIdOrIconPixelData,
                androidx.preference.R.layout.preference,
                summary,
                title,
                0,
                Optional.of("some fragment"),
                Optional.empty(),
                true,
                searchableInfo,
                extras,
                host,
                List.of(),
                predecessor);
    }

    public static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static SearchablePreference copy(final SearchablePreference preference,
                                            final Optional<SearchablePreference> predecessor) {
        return createSearchablePreferencePOJO(
                preference.getTitle().orElseThrow(),
                preference.getHost(),
                predecessor);
    }
}
