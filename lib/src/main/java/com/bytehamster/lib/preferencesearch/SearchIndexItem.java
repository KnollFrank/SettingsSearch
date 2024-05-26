package com.bytehamster.lib.preferencesearch;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Parcels;

/**
 * Adds a given R.xml resource to the search index
 */
class SearchIndexItem implements Parcelable {

    private String breadcrumb = "";
    private final Class<? extends PreferenceFragmentCompat> resId;
    private final SearchConfiguration searchConfiguration;

    /**
     * Includes the given R.xml resource in the index
     *
     * @param resId The resource to index
     */
    SearchIndexItem(final Class<? extends PreferenceFragmentCompat> resId,
                    final SearchConfiguration searchConfiguration) {
        this.resId = resId;
        this.searchConfiguration = searchConfiguration;
    }

    /**
     * Adds a breadcrumb
     *
     * @param breadcrumb The breadcrumb to add
     * @return For chaining
     */
    public SearchIndexItem addBreadcrumb(@StringRes int breadcrumb) {
        assertNotParcel();
        return addBreadcrumb(searchConfiguration.activity.getString(breadcrumb));
    }

    /**
     * Adds a breadcrumb
     *
     * @param breadcrumb The breadcrumb to add
     * @return For chaining
     */
    public SearchIndexItem addBreadcrumb(String breadcrumb) {
        assertNotParcel();
        this.breadcrumb = Breadcrumb.concat(this.breadcrumb, breadcrumb);
        return this;
    }

    /**
     * Throws an exception if the item does not have a searchConfiguration (thus, is restored from a parcel)
     */
    private void assertNotParcel() {
        if (searchConfiguration == null) {
            throw new IllegalStateException("SearchIndexItems that are restored from parcel can not be modified.");
        }
    }

    Class<? extends PreferenceFragmentCompat> getResId() {
        return resId;
    }

    String getBreadcrumb() {
        return breadcrumb;
    }

    SearchConfiguration getSearchConfiguration() {
        return searchConfiguration;
    }

    public static final Creator<SearchIndexItem> CREATOR = new Creator<>() {
        @Override
        public SearchIndexItem createFromParcel(Parcel in) {
            return new SearchIndexItem(in);
        }

        @Override
        public SearchIndexItem[] newArray(int size) {
            return new SearchIndexItem[size];
        }
    };

    private SearchIndexItem(Parcel parcel) {
        this.breadcrumb = parcel.readString();
        this.resId = Parcels.readClass(parcel);
        this.searchConfiguration = null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.breadcrumb);
        Parcels.writeClass(dest, this.resId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
