package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.LayoutRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepoetics.ambivalence.Either;
import com.google.common.collect.MoreCollectors;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;

@Entity
public final class SearchablePreferencePOJO {

    @PrimaryKey
    // FK-TODO: replace int with long?
    private final int id;
    private final String key;
    private final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData;
    @Ignore
    private Optional<Optional<Drawable>> iconCache = Optional.empty();
    private final @LayoutRes int layoutResId;
    private Optional<String> summary;
    @Ignore
    private Supplier<Optional<CharSequence>> highlightedSummaryProvider;
    private final Optional<String> title;
    @Ignore
    private Supplier<Optional<CharSequence>> highlightedTitleProvider;
    private final @LayoutRes int widgetLayoutResId;
    private final Optional<String> fragment;
    private final Optional<String> classNameOfReferencedActivity;
    private final boolean visible;
    private final Optional<String> searchableInfo;
    @Ignore
    private Supplier<Optional<CharSequence>> highlightedSearchableInfoProvider;
    // FK-TODO: ignore temporarily
    // private final Bundle extras;
    private final Class<? extends PreferenceFragmentCompat> host;
    private final Optional<Integer> parentId;
    private final Optional<Integer> predecessorId;

    public SearchablePreferencePOJO(
            final int id,
            final String key,
            final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
            final @LayoutRes int layoutResId,
            final Optional<String> summary,
            final Optional<String> title,
            final @LayoutRes int widgetLayoutResId,
            final Optional<String> fragment,
            final Optional<String> classNameOfReferencedActivity,
            final boolean visible,
            final Optional<String> searchableInfo,
            // final Bundle extras,
            final Class<? extends PreferenceFragmentCompat> host,
            final Optional<Integer> parentId,
            final Optional<Integer> predecessorId) {
        this.id = id;
        this.key = Objects.requireNonNull(key);
        this.iconResourceIdOrIconPixelData = iconResourceIdOrIconPixelData;
        this.layoutResId = layoutResId;
        this.summary = summary;
        this.title = title;
        this.widgetLayoutResId = widgetLayoutResId;
        this.fragment = fragment;
        this.classNameOfReferencedActivity = classNameOfReferencedActivity;
        this.visible = visible;
        this.searchableInfo = searchableInfo;
        // this.extras = extras;
        this.host = host;
        this.parentId = parentId;
        this.predecessorId = predecessorId;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public Optional<Either<Integer, String>> getIconResourceIdOrIconPixelData() {
        return iconResourceIdOrIconPixelData;
    }

    public Optional<Drawable> getIcon(final Context context) {
        if (iconCache.isEmpty()) {
            iconCache = Optional.of(_getIcon(context));
        }
        return iconCache.orElseThrow();
    }

    public @LayoutRes int getLayoutResId() {
        return layoutResId;
    }

    public void setSummary(final Optional<String> summary) {
        this.summary = summary;
    }

    public Optional<String> getSummary() {
        return summary;
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
        return title;
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
        return searchableInfo;
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
        return fragment;
    }

    public Optional<String> getClassNameOfReferencedActivity() {
        return classNameOfReferencedActivity;
    }

    public Optional<Class<? extends Activity>> getClassOfReferencedActivity(final Context context) {
        return this
                .getClassNameOfReferencedActivity()
                .flatMap(classNameOfReferencedActivity -> Classes.classNameAsSubclassOfClazz(classNameOfReferencedActivity, Activity.class, context));
    }

    public boolean isVisible() {
        return visible;
    }

//    public Bundle getExtras() {
//        return extras;
//    }

    public Class<? extends PreferenceFragmentCompat> getHost() {
        return host;
    }

//    public PreferencePath getPreferencePath() {
//        return getPreferencePathOfPredecessor().append(this);
//    }

    public List<SearchablePreferencePOJO> getChildren(final SearchablePreferencePOJODAO dao) {
        // FK-TODO: refactor using intermediate map?
        return dao
                .getPreferencesAndChildren()
                .stream()
                .filter(preferenceAndChildren -> preferenceAndChildren.preference().equals(this))
                .map(PreferenceAndChildren::children)
                .collect(MoreCollectors.onlyElement());
    }

    public Optional<SearchablePreferencePOJO> getPredecessor(final SearchablePreferencePOJODAO dao) {
        // FK-TODO: refactor using intermediate map?
        return dao
                .getPreferencesAndPredecessors()
                .stream()
                .filter(preferenceAndPredecessor -> preferenceAndPredecessor.getPreference().equals(this))
                .map(PreferenceAndPredecessor::getPredecessor)
                .collect(MoreCollectors.toOptional())
                .orElse(Optional.empty());
    }

    Optional<Integer> getParentId() {
        return parentId;
    }

    Optional<Integer> getPredecessorId() {
        return predecessorId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferencePOJO that = (SearchablePreferencePOJO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SearchablePreferencePOJO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", searchableInfo='" + searchableInfo + '\'' +
                ", key='" + key + '\'' +
                ", fragment='" + fragment + '\'' +
                ", visible=" + visible +
                //", extras=" + extras +
                // ", children=" + children +
                ", host=" + host +
                // ", predecessor=" + predecessor +
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

//    private PreferencePath getPreferencePathOfPredecessor() {
//        return this
//                .getPredecessor()
//                .map(SearchablePreference::getPreferencePath)
//                .orElseGet(() -> new PreferencePath(List.of()));
//    }
}
