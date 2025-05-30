package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.LayoutRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepoetics.ambivalence.Either;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Classes;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;

@Entity
public final class SearchablePreference {

    public interface DbDataProvider {

        Set<SearchablePreference> getChildren(SearchablePreference preference);

        Optional<SearchablePreference> getPredecessor(SearchablePreference preference);

        SearchablePreferenceScreen getHost(SearchablePreference preference);
    }

    @Ignore
    private Optional<DbDataProvider> dao = Optional.empty();

    @PrimaryKey
    private final int id;
    private final String key;
    private final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData;
    @Ignore
    private Optional<Optional<Drawable>> iconCache = Optional.empty();
    private final @LayoutRes int layoutResId;

    private final Optional<String> title;
    @Ignore
    private Supplier<Optional<CharSequence>> highlightedTitleProvider;

    private Optional<String> summary;
    @Ignore
    private Supplier<Optional<CharSequence>> highlightedSummaryProvider;

    private final @LayoutRes int widgetLayoutResId;
    private final Optional<String> fragment;
    private final Optional<String> classNameOfReferencedActivity;
    private final boolean visible;
    private final Optional<String> searchableInfo;
    @Ignore
    private Supplier<Optional<CharSequence>> highlightedSearchableInfoProvider;
    private final Optional<Integer> parentId;
    private final Optional<Integer> predecessorId;
    private final String searchablePreferenceScreenId;

    public SearchablePreference(final int id,
                                final String key,
                                final Optional<String> title,
                                final Optional<String> summary,
                                final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
                                final @LayoutRes int layoutResId,
                                final @LayoutRes int widgetLayoutResId,
                                final Optional<String> fragment,
                                final Optional<String> classNameOfReferencedActivity,
                                final boolean visible,
                                final Optional<String> searchableInfo,
                                final Optional<Integer> parentId,
                                final Optional<Integer> predecessorId,
                                final String searchablePreferenceScreenId) {
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
        this.parentId = parentId;
        this.predecessorId = predecessorId;
        this.searchablePreferenceScreenId = searchablePreferenceScreenId;
    }

    public void setDao(final DbDataProvider dao) {
        this.dao = Optional.of(dao);
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
                .flatMap(
                        classNameOfReferencedActivity ->
                                Classes.classNameAsSubclassOfClazz(
                                        classNameOfReferencedActivity,
                                        Activity.class,
                                        context));
    }

    public boolean isVisible() {
        return visible;
    }

    public SearchablePreferenceScreen getHost() {
        return dao.orElseThrow().getHost(this);
    }

    public PreferencePath getPreferencePath() {
        return getPreferencePathOfPredecessor().append(this);
    }

    public Set<SearchablePreference> getChildren() {
        return dao.orElseThrow().getChildren(this);
    }

    public Optional<SearchablePreference> getPredecessor() {
        return dao.orElseThrow().getPredecessor(this);
    }

    public Optional<Integer> getParentId() {
        return parentId;
    }

    public Optional<Integer> getPredecessorId() {
        return predecessorId;
    }

    public String getSearchablePreferenceScreenId() {
        return searchablePreferenceScreenId;
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
        return new StringJoiner(", ", SearchablePreference.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("key='" + key + "'")
                .add("title=" + title)
                .add("summary=" + summary)
                .add("searchableInfo=" + searchableInfo)
                .add("fragment=" + fragment)
                .add("visible=" + visible)
                .add("parentId=" + parentId)
                .add("predecessorId=" + predecessorId)
                .add("searchablePreferenceScreenId='" + searchablePreferenceScreenId + "'")
                .toString();
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

    private PreferencePath getPreferencePathOfPredecessor() {
        return this
                .getPredecessor()
                .map(SearchablePreference::getPreferencePath)
                .orElseGet(() -> new PreferencePath(List.of()));
    }
}
