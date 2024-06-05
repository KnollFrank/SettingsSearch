package com.bytehamster.lib.preferencesearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PreferenceItem implements Parcelable {

    public final String title;
    public final String summary;
    public final String key;
    public final String breadcrumbs;
    public final String keywords;
    public final Class<? extends PreferenceFragmentCompat> resId;

    public String entries;
    // FK-TODO: breadcrumbs aktivieren, Tests dazu schreiben (Graph dazu wieder einführen)
    public final List<String> keyBreadcrumbs = new ArrayList<>();

    public PreferenceItem(final String title,
                          final String summary,
                          final String key,
                          final String breadcrumbs,
                          final String keywords,
                          final Class<? extends PreferenceFragmentCompat> resId) {
        this.title = title;
        this.summary = summary;
        this.key = key;
        this.breadcrumbs = breadcrumbs;
        this.keywords = keywords;
        this.resId = resId;
    }

    private PreferenceItem(final Parcel source) {
        this.title = source.readString();
        this.summary = source.readString();
        this.key = source.readString();
        this.breadcrumbs = source.readString();
        this.keywords = source.readString();
        this.resId = Parcels.readClass(source);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(summary);
        parcel.writeString(key);
        parcel.writeString(breadcrumbs);
        parcel.writeString(keywords);
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

    public boolean matches(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return false;
        }
        return getInfo().toLowerCase().contains(keyword.toLowerCase());
    }

    private String getInfo() {
        StringBuilder infoBuilder = new StringBuilder();
        // FK-TODO: emptyIfNull = s => Objects.toString(s, "");
        //          replace with infoBuilder.append("ø").append(emptyIfNull(title));
        if (!TextUtils.isEmpty(title)) {
            infoBuilder.append("ø").append(title);
        }
        if (!TextUtils.isEmpty(summary)) {
            infoBuilder.append("ø").append(summary);
        }
        if (!TextUtils.isEmpty(entries)) {
            infoBuilder.append("ø").append(entries);
        }
        if (!TextUtils.isEmpty(breadcrumbs)) {
            infoBuilder.append("ø").append(breadcrumbs);
        }
        if (!TextUtils.isEmpty(keywords)) {
            infoBuilder.append("ø").append(keywords);
        }
        return infoBuilder.toString();
    }

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
        return resId == that.resId && Objects.equals(title, that.title) && Objects.equals(summary, that.summary) && Objects.equals(key, that.key) && Objects.equals(breadcrumbs, that.breadcrumbs) && Objects.equals(keywords, that.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, summary, key, breadcrumbs, keywords, resId);
    }
}
