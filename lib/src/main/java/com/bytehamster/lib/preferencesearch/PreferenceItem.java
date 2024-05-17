package com.bytehamster.lib.preferencesearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.XmlRes;

import org.apache.commons.text.similarity.FuzzyScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

class PreferenceItem extends ListItem implements Parcelable {
    static final int TYPE = 2;
    private static final FuzzyScore fuzzyScore = new FuzzyScore(Locale.getDefault());

    public final String title;
    public final String summary;
    public final String key;
    public final String breadcrumbs;
    public final String keywords;
    @XmlRes
    public final int resId;

    public String entries;
    public List<String> keyBreadcrumbs = new ArrayList<>();
    private float lastScore = 0;
    private String lastKeyword = null;

    PreferenceItem(final String title,
                   final String summary,
                   final String key,
                   final String breadcrumbs,
                   final String keywords,
                   @XmlRes final int resId) {
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
        this.resId = source.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(summary);
        parcel.writeString(key);
        parcel.writeString(breadcrumbs);
        parcel.writeString(keywords);
        parcel.writeInt(resId);
    }

    public static final Creator<PreferenceItem> CREATOR = new Creator<PreferenceItem>() {

        @Override
        public PreferenceItem createFromParcel(final Parcel source) {
            return new PreferenceItem(source);
        }

        @Override
        public PreferenceItem[] newArray(int size) {
            return new PreferenceItem[size];
        }
    };

    boolean hasData() {
        return title != null || summary != null;
    }

    boolean matchesFuzzy(String keyword) {
        return getScore(keyword) > 0.3;
    }

    boolean matches(String keyword) {
        Locale locale = Locale.getDefault();
        return getInfo().toLowerCase(locale).contains(keyword.toLowerCase(locale));
    }

    float getScore(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return 0;
        } else if (TextUtils.equals(lastKeyword, keyword)) {
            return lastScore;
        }
        String info = getInfo();

        float score = fuzzyScore.fuzzyScore(info, "ø" + keyword);
        float maxScore = (keyword.length() + 1) * 3 - 2; // First item can not get +2 bonus score

        lastScore = score / maxScore;
        lastKeyword = keyword;
        return lastScore;
    }

    private String getInfo() {
        StringBuilder infoBuilder = new StringBuilder();
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
    public int getType() {
        return TYPE;
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
