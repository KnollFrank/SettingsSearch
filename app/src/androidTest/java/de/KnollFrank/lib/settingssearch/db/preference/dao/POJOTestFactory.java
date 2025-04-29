package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: DRY with de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory
public class POJOTestFactory {

    public static SearchablePreferencePOJO createSearchablePreferencePOJO(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId) {
        return createSearchablePreferencePOJO(
                id,
                title,
                summary,
                searchableInfo,
                createBundle("someKey", "someValue"),
                iconResourceIdOrIconPixelData,
                PrefsFragmentFirst.class,
                parentId,
                predecessorId);
    }

    public static SearchablePreferencePOJO createSearchablePreferencePOJO(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Bundle extras,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final Class<? extends PreferenceFragmentCompat> host,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId) {
        return new SearchablePreferencePOJO(
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
                parentId,
                predecessorId);
    }

    public static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }
}
