package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;
import android.os.Parcel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SearchablePreferencePOJO implements Serializable {

    private int id;
    private String key;
    private byte[] icon;
    private int layoutResId;
    private String summary;
    private String title;
    private int widgetLayoutResId;
    private String fragment;
    private boolean visible;
    private String searchableInfo;
    private Bundle extras;
    private List<SearchablePreferencePOJO> children;

    public SearchablePreferencePOJO(
            final int id,
            final Optional<String> key,
            final Optional<byte[]> icon,
            final int layoutResId,
            final Optional<String> summary,
            final Optional<String> title,
            final int widgetLayoutResId,
            final Optional<String> fragment,
            final boolean visible,
            final Optional<String> searchableInfo,
            final Bundle extras,
            final List<SearchablePreferencePOJO> children) {
        this.id = id;
        this.key = key.orElse(null);
        this.icon = icon.orElse(null);
        this.layoutResId = layoutResId;
        this.summary = summary.orElse(null);
        this.title = title.orElse(null);
        this.widgetLayoutResId = widgetLayoutResId;
        this.fragment = fragment.orElse(null);
        this.visible = visible;
        this.searchableInfo = searchableInfo.orElse(null);
        this.extras = extras;
        this.children = children;
    }

    public int id() {
        return id;
    }

    public List<SearchablePreferencePOJO> children() {
        return children;
    }

    public Optional<String> key() {
        return Optional.ofNullable(key);
    }

    public Optional<byte[]> icon() {
        return Optional.ofNullable(icon);
    }

    public int layoutResId() {
        return layoutResId;
    }

    public Optional<String> summary() {
        return Optional.ofNullable(summary);
    }

    public Optional<String> title() {
        return Optional.ofNullable(title);
    }

    public int widgetLayoutResId() {
        return widgetLayoutResId;
    }

    public Optional<String> fragment() {
        return Optional.ofNullable(fragment);
    }

    public boolean visible() {
        return visible;
    }

    public Optional<String> searchableInfo() {
        return Optional.ofNullable(searchableInfo);
    }

    public Bundle extras() {
        return extras;
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
        return "SearchablePreferencePOJO[" +
                "id=" + id + ", " +
                "key=" + key + ", " +
                "icon=" + icon + ", " +
                "layoutResId=" + layoutResId + ", " +
                "summary=" + summary + ", " +
                "title=" + title + ", " +
                "widgetLayoutResId=" + widgetLayoutResId + ", " +
                "fragment=" + fragment + ", " +
                "visible=" + visible + ", " +
                "searchableInfo=" + searchableInfo + ", " +
                "extras=" + extras + ", " +
                "children=" + children + ']';
    }

    @Serial
    private void writeObject(final ObjectOutputStream outputStream) throws IOException {
        outputStream.writeInt(id);
        outputStream.writeObject(key);
        outputStream.writeObject(icon);
        outputStream.writeInt(layoutResId);
        outputStream.writeObject(summary);
        outputStream.writeObject(title);
        outputStream.writeInt(widgetLayoutResId);
        outputStream.writeObject(fragment);
        outputStream.writeBoolean(visible);
        outputStream.writeObject(searchableInfo);
        writeBytes(outputStream, bundle2bytes(extras));
        outputStream.writeObject(children);
    }

    @Serial
    private void readObject(final ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        id = inputStream.readInt();
        key = (String) inputStream.readObject();
        icon = (byte[]) inputStream.readObject();
        layoutResId = inputStream.readInt();
        summary = (String) inputStream.readObject();
        title = (String) inputStream.readObject();
        widgetLayoutResId = inputStream.readInt();
        fragment = (String) inputStream.readObject();
        visible = inputStream.readBoolean();
        searchableInfo = (String) inputStream.readObject();
        extras = bytes2bundle(readBytes(inputStream));
        children = (List<SearchablePreferencePOJO>) inputStream.readObject();
    }

    private static void writeBytes(final ObjectOutputStream outputStream, final byte[] bytes) throws IOException {
        // FK-TODO: just use writeObject()
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
    }

    private static byte[] readBytes(final ObjectInputStream inputStream) throws IOException {
        final int size = inputStream.readInt();
        final byte[] bytes = new byte[size];
        inputStream.read(bytes);
        return bytes;
    }

    private static byte[] bundle2bytes(final Bundle bundle) {
        final Parcel parcel = Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        final byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    private static Bundle bytes2bundle(final byte[] bytes) {
        final Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        final Bundle bundle = parcel.readBundle();
        parcel.recycle();
        return bundle;
    }
}
