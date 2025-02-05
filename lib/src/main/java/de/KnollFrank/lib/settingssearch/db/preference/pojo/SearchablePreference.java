package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceFragmentCompat;

import com.codepoetics.ambivalence.Either;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.Exclude;

public final class SearchablePreference {

    private final int id;
    private final String key;
    private final Either<Integer, String> iconResourceIdOrIconPixelData;
    @Exclude
    private Optional<Drawable> iconCache;
    private final @LayoutRes int layoutResId;
    private final String summary;
    @Exclude
    private Supplier<Optional<CharSequence>> highlightedSummaryProvider;
    private final String title;
    @Exclude
    private Supplier<Optional<CharSequence>> highlightedTitleProvider;
    private final @LayoutRes int widgetLayoutResId;
    private final String fragment;
    private final String classNameOfReferencedActivity;
    private final boolean visible;
    private final String searchableInfo;
    @Exclude
    private Supplier<Optional<CharSequence>> highlightedSearchableInfoProvider;
    private final Bundle extras;
    private final List<SearchablePreference> children;
    @Exclude
    private PreferencePath preferencePath;
    @Exclude
    private Class<? extends PreferenceFragmentCompat> host;

    public SearchablePreference(
            final int id,
            final Optional<String> key,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final @LayoutRes int layoutResId,
            final Optional<String> summary,
            final Optional<String> title,
            final @LayoutRes int widgetLayoutResId,
            final Optional<String> fragment,
            final Optional<String> classNameOfReferencedActivity,
            final boolean visible,
            final Optional<String> searchableInfo,
            final Bundle extras,
            final List<SearchablePreference> children) {
        this.id = id;
        this.key = key.orElse(null);
        this.iconResourceIdOrIconPixelData = iconResourceIdOrIconPixelData.orElse(null);
        this.layoutResId = layoutResId;
        this.summary = summary.orElse(null);
        this.title = title.orElse(null);
        this.widgetLayoutResId = widgetLayoutResId;
        this.fragment = fragment.orElse(null);
        this.classNameOfReferencedActivity = classNameOfReferencedActivity.orElse(null);
        this.visible = visible;
        this.searchableInfo = searchableInfo.orElse(null);
        this.extras = extras;
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public List<SearchablePreference> getChildren() {
        return children;
    }

    public Optional<String> getKey() {
        return Optional.ofNullable(key);
    }

    public Optional<Either<Integer, String>> getIconResourceIdOrIconPixelData() {
        return Optional.ofNullable(iconResourceIdOrIconPixelData);
    }

    public Optional<Drawable> getIcon(final Context context) {
        if (iconCache == null) {
            iconCache = _getIcon(context);
        }
        return iconCache;
    }

    public @LayoutRes int getLayoutResId() {
        return layoutResId;
    }

    public Optional<String> getSummary() {
        return Optional.ofNullable(summary);
    }

    public void setHighlightedSummaryProvider(final Supplier<Optional<CharSequence>> highlightedSummaryProvider) {
        this.highlightedSummaryProvider = highlightedSummaryProvider;
    }

    public Optional<CharSequence> getHighlightedSummary() {
        if (highlightedSummaryProvider == null) {
            highlightedSummaryProvider = Optional::empty;
        }
        return highlightedSummaryProvider.get();
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public void setHighlightedTitleProvider(final Supplier<Optional<CharSequence>> highlightedTitleProvider) {
        this.highlightedTitleProvider = highlightedTitleProvider;
    }

    public Optional<CharSequence> getHighlightedTitle() {
        if (highlightedTitleProvider == null) {
            highlightedTitleProvider = Optional::empty;
        }
        return highlightedTitleProvider.get();
    }

    public Optional<String> getSearchableInfo() {
        return Optional.ofNullable(searchableInfo);
    }

    public void setHighlightedSearchableInfoProvider(final Supplier<Optional<CharSequence>> highlightedSearchableInfoProvider) {
        this.highlightedSearchableInfoProvider = highlightedSearchableInfoProvider;
    }

    public Optional<CharSequence> getHighlightedSearchableInfo() {
        if (highlightedSearchableInfoProvider == null) {
            highlightedSearchableInfoProvider = Optional::empty;
        }
        return highlightedSearchableInfoProvider.get();
    }

    public boolean hasPreferenceMatchWithinSearchableInfo() {
        return getHighlightedSearchableInfo().isPresent();
    }

    public @LayoutRes int getWidgetLayoutResId() {
        return widgetLayoutResId;
    }

    public Optional<String> getFragment() {
        return Optional.ofNullable(fragment);
    }

    public Optional<String> getClassNameOfReferencedActivity() {
        return Optional.ofNullable(classNameOfReferencedActivity);
    }

    public Optional<Class<? extends Activity>> getClassOfReferencedActivity(final Context context) {
        return this
                .getClassNameOfReferencedActivity()
                .flatMap(classNameOfReferencedActivity -> Classes.classNameAsSubclassOfClazz(classNameOfReferencedActivity, Activity.class, context));
    }

    public boolean isVisible() {
        return visible;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setPreferencePath(final PreferencePath preferencePath) {
        this.preferencePath = preferencePath;
    }

    public PreferencePath getPreferencePath() {
        return preferencePath;
    }

    public Class<? extends PreferenceFragmentCompat> getHost() {
        return host;
    }

    public void setHost(final Class<? extends PreferenceFragmentCompat> host) {
        this.host = host;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreference that = (SearchablePreference) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SearchablePreference{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", searchableInfo='" + searchableInfo + '\'' +
                ", key='" + key + '\'' +
                ", fragment='" + fragment + '\'' +
                ", visible=" + visible +
                ", extras=" + extras +
                ", children=" + children +
                ", host=" + host +
                '}';
    }

    private Optional<Drawable> _getIcon(final Context context) {
        return this
                .getIconResourceIdOrIconPixelData()
                .map(iconResourceIdOrIconPixelData ->
                        iconResourceIdOrIconPixelData.join(
                                iconResourceId -> AppCompatResources.getDrawable(context, iconResourceId),
                                iconPixelData ->
                                        DrawableAndStringConverter.string2Drawable(
                                                iconPixelData,
                                                context.getResources())));
    }
}
