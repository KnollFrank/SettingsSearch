package com.bytehamster.lib.preferencesearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

class PreferenceItem implements Parcelable {

    public final Optional<String> title;
    public final Optional<String> summary;
    public final Optional<String> key;
    public final Optional<String> breadcrumbs;
    public final Optional<String> keywords;
    public final Optional<String> entries;
    public final Class<? extends PreferenceFragmentCompat> resId;
    // FK-TODO: breadcrumbs aktivieren, Tests dazu schreiben (Graph dazu wieder einführen)
    public final List<String> keyBreadcrumbs = new ArrayList<>();

    public PreferenceItem(final Optional<String> title,
                          final Optional<String> summary,
                          final Optional<String> key,
                          final Optional<String> breadcrumbs,
                          final Optional<String> keywords,
                          final Optional<String> entries,
                          final Class<? extends PreferenceFragmentCompat> resId) {
        this.title = title;
        this.summary = summary;
        this.key = key;
        this.breadcrumbs = breadcrumbs;
        this.keywords = keywords;
        this.entries = entries;
        this.resId = resId;
    }

    public boolean matches(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return false;
        }
        return Stream
                .of(title, summary, breadcrumbs, keywords, entries)
                .anyMatch(haystack -> matches(haystack, keyword));
    }

    private PreferenceItem(final Parcel source) {
        this.title = Optional.ofNullable(source.readString());
        this.summary = Optional.ofNullable(source.readString());
        this.key = Optional.ofNullable(source.readString());
        this.breadcrumbs = Optional.ofNullable(source.readString());
        this.keywords = Optional.ofNullable(source.readString());
        this.entries = Optional.ofNullable(source.readString());
        this.resId = Parcels.readClass(source);
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        parcel.writeString(title.orElse(null));
        parcel.writeString(summary.orElse(null));
        parcel.writeString(key.orElse(null));
        parcel.writeString(breadcrumbs.orElse(null));
        parcel.writeString(keywords.orElse(null));
        parcel.writeString(entries.orElse(null));
        Parcels.writeClass(parcel, resId);
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
        return Objects.equals(title, that.title) && Objects.equals(summary, that.summary) && Objects.equals(key, that.key) && Objects.equals(breadcrumbs, that.breadcrumbs) && Objects.equals(keywords, that.keywords) && Objects.equals(entries, that.entries) && Objects.equals(resId, that.resId) && Objects.equals(keyBreadcrumbs, that.keyBreadcrumbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, summary, key, breadcrumbs, keywords, entries, resId, keyBreadcrumbs);
    }

    private static boolean matches(final Optional<String> haystack, final String needle) {
        if (!haystack.isPresent()) {
            return false;
        }
        return matches(haystack.get(), needle);
    }

    private static boolean matches(final String haystack, final String needle) {
        return haystack.toLowerCase().contains(needle.toLowerCase());
    }
}
