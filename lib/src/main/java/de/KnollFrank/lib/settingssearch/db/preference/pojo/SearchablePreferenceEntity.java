package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.LayoutRes;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codepoetics.ambivalence.Either;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

@Entity
public final class SearchablePreferenceEntity {

    public interface DbDataProvider {

        Set<SearchablePreferenceEntity> getChildren(SearchablePreferenceEntity preference);

        Optional<SearchablePreferenceEntity> getPredecessor(SearchablePreferenceEntity preference);

        SearchablePreferenceScreenEntity getHost(SearchablePreferenceEntity preference);
    }

    @PrimaryKey
    private final int id;
    private final String key;
    private final Optional<Either<Integer, String>> iconResourceIdOrIconPixelData;
    private final @LayoutRes int layoutResId;
    private final Optional<String> title;
    private final Optional<String> summary;
    private final @LayoutRes int widgetLayoutResId;
    private final Optional<String> fragment;
    private final Optional<String> classNameOfReferencedActivity;
    private final boolean visible;
    private final Optional<String> searchableInfo;
    private final Optional<Integer> parentId;
    private final Optional<Integer> predecessorId;
    private final String searchablePreferenceScreenId;

    public SearchablePreferenceEntity(final int id,
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

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public Optional<Either<Integer, String>> getIconResourceIdOrIconPixelData() {
        return iconResourceIdOrIconPixelData;
    }

    public @LayoutRes int getLayoutResId() {
        return layoutResId;
    }

    public Optional<String> getSummary() {
        return summary;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getSearchableInfo() {
        return searchableInfo;
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

    public boolean isVisible() {
        return visible;
    }

    public Set<SearchablePreferenceEntity> getChildren(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getChildren(this);
    }

    public Optional<SearchablePreferenceEntity> getPredecessor(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getPredecessor(this);
    }

    public SearchablePreferenceScreenEntity getHost(final DbDataProvider dbDataProvider) {
        return dbDataProvider.getHost(this);
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
        final SearchablePreferenceEntity that = (SearchablePreferenceEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SearchablePreferenceEntity.class.getSimpleName() + "[", "]")
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

}
