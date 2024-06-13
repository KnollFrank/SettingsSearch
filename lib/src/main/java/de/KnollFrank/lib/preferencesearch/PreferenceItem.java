package de.KnollFrank.lib.preferencesearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

// FK-TODO: remove Parcelable interface?
public class PreferenceItem implements Parcelable, IPreferenceItem {

    public final Optional<String> title;
    private final Optional<String> summary;
    private final Optional<String> key;
    private final Optional<String> breadcrumbs;
    private final Optional<String> keywords;
    private final Optional<String> entries;
    // FK-TODO: remove keyBreadcrumbs
    private final List<String> keyBreadcrumbs = new ArrayList<>();

    public PreferenceItem(final Optional<String> title,
                          final Optional<String> summary,
                          final Optional<String> key,
                          final Optional<String> breadcrumbs,
                          final Optional<String> keywords,
                          final Optional<String> entries) {
        this.title = title;
        this.summary = summary;
        this.key = key;
        this.breadcrumbs = breadcrumbs;
        this.keywords = keywords;
        this.entries = entries;
    }

    @Override
    public boolean matches(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return false;
        }
        return Stream
                .of(title, summary, breadcrumbs, keywords, entries)
                .anyMatch(haystack -> matches(haystack, keyword));
    }

    private static boolean matches(final Optional<String> haystack, final String needle) {
        return haystack
                .filter(_haystack -> matches(_haystack, needle))
                .isPresent();
    }

    private static boolean matches(final String haystack, final String needle) {
        return haystack.toLowerCase().contains(needle.toLowerCase());
    }

    private PreferenceItem(final Parcel source) {
        this.title = Optional.ofNullable(source.readString());
        this.summary = Optional.ofNullable(source.readString());
        this.key = Optional.ofNullable(source.readString());
        this.breadcrumbs = Optional.ofNullable(source.readString());
        this.keywords = Optional.ofNullable(source.readString());
        this.entries = Optional.ofNullable(source.readString());
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        parcel.writeString(title.orElse(null));
        parcel.writeString(summary.orElse(null));
        parcel.writeString(key.orElse(null));
        parcel.writeString(breadcrumbs.orElse(null));
        parcel.writeString(keywords.orElse(null));
        parcel.writeString(entries.orElse(null));
    }

    public static final Creator<PreferenceItem> CREATOR = new Creator<>() {

        @Override
        public PreferenceItem createFromParcel(final Parcel source) {
            return new PreferenceItem(source);
        }

        @Override
        public PreferenceItem[] newArray(int size) {
            return new PreferenceItem[size];
        }
    };

    @Override
    public String toString() {
        return "PreferenceItem: " + title + " " + summary + " " + key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferenceItem that = (PreferenceItem) o;
        return Objects.equals(title, that.title) && Objects.equals(summary, that.summary) && Objects.equals(key, that.key) && Objects.equals(breadcrumbs, that.breadcrumbs) && Objects.equals(keywords, that.keywords) && Objects.equals(entries, that.entries) && Objects.equals(keyBreadcrumbs, that.keyBreadcrumbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, summary, key, breadcrumbs, keywords, entries, keyBreadcrumbs);
    }
}
