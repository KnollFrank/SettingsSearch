package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferencePathConverterWithMarkup implements PreferencePathConverter {

    private final Supplier<List<Object>> markupsFactory;

    public PreferencePathConverterWithMarkup(final Supplier<List<Object>> markupsFactory) {
        this.markupsFactory = markupsFactory;
    }

    @Override
    public CharSequence toCharSequence(final PreferencePath preferencePath) {
        final List<SpannableString> titles = getTitles(preferencePath);
        markup(titles.get(0));
        return join(titles, new SpannableString(" > "));
    }

    private static List<SpannableString> getTitles(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(PreferencePathConverterWithMarkup::getTitle)
                .collect(Collectors.toList());
    }

    private static SpannableString getTitle(final SearchablePreferencePOJO searchablePreferencePOJO) {
        return searchablePreferencePOJO
                .getTitle()
                .map(SpannableString::new)
                .orElseGet(() -> new SpannableString("?"));
    }

    private void markup(final Spannable spannable) {
        for (final Object markup : markupsFactory.get()) {
            spannable.setSpan(
                    markup,
                    0,
                    spannable.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static SpannableString join(final List<SpannableString> titles, final SpannableString delimiter) {
        return appendAll(insert(titles, delimiter));
    }

    private static <T> List<T> insert(final List<T> ts, final T delimiter) {
        return ts
                .stream()
                .flatMap(t -> Stream.of(t, delimiter))
                .limit(ts.size() * 2L - 1)
                .collect(Collectors.toList());
    }

    private static SpannableString appendAll(final List<? extends CharSequence> charSequences) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        charSequences.forEach(builder::append);
        return new SpannableString(builder);
    }
}
