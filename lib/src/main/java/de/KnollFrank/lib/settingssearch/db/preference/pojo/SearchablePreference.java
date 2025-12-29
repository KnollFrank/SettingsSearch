package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.content.res.AppCompatResources;

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

public final class SearchablePreference {

    private final String id;
    private final String key;
    private final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData;
    private Optional<Optional<Drawable>> iconCache = Optional.empty();
    private final @LayoutRes int layoutResId;
    private final Optional<String> title;
    private Supplier<Optional<CharSequence>> highlightedTitleProvider = Optional::empty;
    private Optional<String> summary;
    private Supplier<Optional<CharSequence>> highlightedSummaryProvider = Optional::empty;
    private final @LayoutRes int widgetLayoutResId;
    private final Optional<String> fragment;
    private final Optional<String> classNameOfReferencedActivity;
    private final boolean visible;
    private final PersistableBundle extras;
    private final Optional<String> searchableInfo;
    private Supplier<Optional<CharSequence>> highlightedSearchableInfoProvider = Optional::empty;
    private final Set<SearchablePreference> children;
    // FK-TODO: make predecessor final
    private Optional<SearchablePreference> predecessor;
    // FK-TODO: make host final
    private Optional<SearchablePreferenceScreen> host = Optional.empty();

    public SearchablePreference(final String id,
                                final String key,
                                final Optional<String> title,
                                final Optional<String> summary,
                                final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData,
                                final @LayoutRes int layoutResId,
                                final @LayoutRes int widgetLayoutResId,
                                final Optional<String> fragment,
                                final Optional<String> classNameOfReferencedActivity,
                                final boolean visible,
                                final PersistableBundle extras,
                                final Optional<String> searchableInfo,
                                final Set<SearchablePreference> children,
                                final Optional<SearchablePreference> predecessor) {
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
        this.extras = extras;
        this.searchableInfo = searchableInfo;
        this.children = children;
        this.predecessor = predecessor;
    }

    public String getId() {
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
        return highlightedSummaryProvider.get();
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setHighlightedTitleProvider(final Supplier<Optional<CharSequence>> highlightedTitleProvider) {
        this.highlightedTitleProvider = highlightedTitleProvider;
    }

    public Optional<CharSequence> getHighlightedTitle() {
        return highlightedTitleProvider.get();
    }

    public Optional<String> getSearchableInfo() {
        return searchableInfo;
    }

    public void setHighlightedSearchableInfoProvider(final Supplier<Optional<CharSequence>> highlightedSearchableInfoProvider) {
        this.highlightedSearchableInfoProvider = highlightedSearchableInfoProvider;
    }

    public Optional<CharSequence> getHighlightedSearchableInfo() {
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

    public PersistableBundle getExtras() {
        return extras;
    }

    public PreferencePath getPreferencePath() {
        return predecessor
                .map(SearchablePreference::getPreferencePath)
                .map(preferencePathOfPredecessor -> preferencePathOfPredecessor.append(this))
                .orElseGet(() -> new PreferencePath(List.of(this)));
    }

    public Set<SearchablePreference> getChildren() {
        return children;
    }

    public Optional<SearchablePreference> getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(final Optional<SearchablePreference> predecessor) {
        this.predecessor = predecessor;
    }

    public SearchablePreferenceScreen getHost() {
        return host.orElseThrow(IllegalStateException::new);
    }

    public void setHost(final SearchablePreferenceScreen host) {
        this.host = Optional.of(host);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreference that = (SearchablePreference) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SearchablePreference.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("key='" + key + "'")
                .add("title=" + title)
                .add("summary=" + summary)
                .add("extras=" + extras)
                .add("searchableInfo=" + searchableInfo)
                .add("fragment=" + fragment)
                .add("visible=" + visible)
                .add("predecessorId='" + predecessor.map(SearchablePreference::getId) + "'")
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
}
