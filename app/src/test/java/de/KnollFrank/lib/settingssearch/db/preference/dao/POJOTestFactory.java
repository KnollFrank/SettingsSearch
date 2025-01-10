package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter.drawable2String;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.settingssearch.R;

public class POJOTestFactory {

    private static final IdGenerator idGenerator = new IdGenerator();

    public static SearchablePreference createSearchablePreferencePOJO(
            final String title,
            final Class<? extends PreferenceFragmentCompat> host) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        idGenerator.nextId(),
                        Optional.of(title),
                        Optional.empty(),
                        0,
                        Optional.empty(),
                        Optional.of(title),
                        0,
                        Optional.empty(),
                        true,
                        Optional.empty(),
                        new Bundle(),
                        List.of());
        searchablePreference.setHost(host);
        return searchablePreference;
    }

    public static SearchablePreference createSomeSearchablePreferencePOJO(final Bundle extras,
                                                                          final Resources resources) {
        return new SearchablePreference(
                1,
                Optional.of("some key"),
                Optional.of(Either.ofRight(drawable2String(resources.getDrawable(R.drawable.smiley, null)))),
                4712,
                Optional.of("some summary"),
                Optional.of("some title"),
                4713,
                Optional.of("some fragment"),
                true,
                Optional.of("some searchableInfo"),
                extras,
                List.of(
                        new SearchablePreference(
                                2,
                                Optional.of("some key 2"),
                                Optional.empty(),
                                4715,
                                Optional.of("some summary 2"),
                                Optional.of("some title 2"),
                                4716,
                                Optional.of("some fragment 2"),
                                true,
                                Optional.of("some searchableInfo 2"),
                                new Bundle(),
                                List.of())));
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
                iconResourceIdOrIconPixelData);
    }

    public static SearchablePreference createSearchablePreferencePOJO(
            final int id,
            final Optional<String> title,
            final Optional<String> summary,
            final Optional<String> searchableInfo,
            final Bundle extras,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        id,
                        Optional.of("some key"),
                        iconResourceIdOrIconPixelData,
                        androidx.preference.R.layout.preference,
                        summary,
                        title,
                        0,
                        Optional.of("some fragment"),
                        true,
                        searchableInfo,
                        extras,
                        List.of());
        searchablePreference.setPreferencePath(new PreferencePath(List.of(searchablePreference)));
        searchablePreference.setHost(TestPreferenceFragment.class);
        return searchablePreference;
    }

    public static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static SearchablePreference copy(final SearchablePreference preference) {
        return createSearchablePreferencePOJO(
                preference.getTitle().orElseThrow(),
                preference.getHost());
    }
}
