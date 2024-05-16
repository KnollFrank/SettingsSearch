package com.bytehamster.lib.preferencesearch;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.XmlRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PreferenceParser {
    private static final int MAX_RESULTS = 10;
    private static final String NS_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final String NS_SEARCH = "http://schemas.android.com/apk/com.bytehamster.lib.preferencesearch";
    private static final List<String> BLACKLIST = Arrays.asList(SearchPreference.class.getName(), "PreferenceCategory");
    private static final List<String> CONTAINERS = Arrays.asList("PreferenceCategory", "PreferenceScreen");
    private Context context;
    private List<PreferenceItem> allEntries = new ArrayList<>();

    PreferenceParser(final Context context) {
        this.context = context;
    }

    void addResourceFile(@XmlRes final int preferenceScreen) {
        allEntries.addAll(getPreferenceItems(preferenceScreen));
    }

    void addPreferenceItems(final List<PreferenceItem> preferenceItems) {
        allEntries.addAll(preferenceItems);
    }

    private List<PreferenceItem> getPreferenceItems(@XmlRes final int preferenceScreen) {
        final PreferenceManager preferenceManager = new PreferenceManager(context);
        final PreferenceScreen _preferenceScreen = preferenceManager.inflateFromResource(preferenceManager.getContext(), preferenceScreen, null);
        final List<Preference> preferences = getPreferences(_preferenceScreen);
        final List<PreferenceItem> preferenceItems = new SearchConfiguration().indexItems(preferences);
        preferenceItems.forEach(preferenceItem -> preferenceItem.resId = preferenceScreen);
        return preferenceItems;
    }

    private ArrayList<String> cleanupKeyBreadcrumbs(ArrayList<String> keyBreadcrumbs) {
        ArrayList<String> result = new ArrayList<>();
        for (String keyBreadcrumb : keyBreadcrumbs) {
            if (keyBreadcrumb != null) {
                result.add(keyBreadcrumb);
            }
        }
        return result;
    }

    private String joinBreadcrumbs(ArrayList<String> breadcrumbs) {
        String result = "";
        for (String crumb : breadcrumbs) {
            if (!TextUtils.isEmpty(crumb)) {
                result = Breadcrumb.concat(result, crumb);
            }
        }
        return result;
    }

    private String getAttribute(XmlPullParser xpp, @Nullable String namespace, @NonNull String attribute) {
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            Log.d("ns", xpp.getAttributeNamespace(i));
            if (attribute.equals(xpp.getAttributeName(i)) &&
                    (namespace == null || namespace.equals(xpp.getAttributeNamespace(i)))) {
                return xpp.getAttributeValue(i);
            }
        }
        return null;
    }

    private String getAttribute(XmlPullParser xpp, @NonNull String attribute) {
        if (hasAttribute(xpp, NS_SEARCH, attribute)) {
            return getAttribute(xpp, NS_SEARCH, attribute);
        } else {
            return getAttribute(xpp, NS_ANDROID, attribute);
        }
    }

    private boolean hasAttribute(XmlPullParser xpp, @Nullable String namespace, @NonNull String attribute) {
        return getAttribute(xpp, namespace, attribute) != null;
    }

    private PreferenceItem parseSearchResult(XmlPullParser xpp) {
        PreferenceItem result = new PreferenceItem();
        result.title = readString(getAttribute(xpp, "title"));
        result.summary = readString(getAttribute(xpp, "summary"));
        result.key = readString(getAttribute(xpp, "key"));
        result.entries = readStringArray(getAttribute(xpp, "entries"));
        result.keywords = readString(getAttribute(xpp, NS_SEARCH, "keywords"));

        Log.d("PreferenceParser", "Found: " + xpp.getName() + "/" + result);
        return result;
    }

    private String readStringArray(@Nullable String s) {
        if (s == null) {
            return null;
        }
        if (s.startsWith("@")) {
            try {
                int id = Integer.parseInt(s.substring(1));
                String[] elements = context.getResources().getStringArray(id);
                return TextUtils.join(",", elements);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    private String readString(@Nullable String s) {
        if (s == null) {
            return null;
        }
        if (s.startsWith("@")) {
            try {
                int id = Integer.parseInt(s.substring(1));
                return context.getString(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    List<PreferenceItem> searchFor(final String keyword, boolean fuzzy) {
        if (TextUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        ArrayList<PreferenceItem> results = new ArrayList<>();

        for (PreferenceItem item : allEntries) {
            if ((fuzzy && item.matchesFuzzy(keyword))
                    || (!fuzzy && item.matches(keyword))) {
                results.add(item);
            }
        }

        Collections.sort(results, (i1, i2) -> floatCompare(i2.getScore(keyword), i1.getScore(keyword)));

        if (results.size() > MAX_RESULTS) {
            return results.subList(0, MAX_RESULTS);
        } else {
            return results;
        }
    }

    @SuppressWarnings("UseCompareMethod")
    private static int floatCompare(float x, float y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    private static List<Preference> getPreferences(final PreferenceGroup preferenceGroup) {
        final Builder<Preference> preferencesBuilder = ImmutableList.builder();
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            final Preference preference = preferenceGroup.getPreference(i);
            preferencesBuilder.add(preference);
            if (preference instanceof PreferenceGroup) {
                preferencesBuilder.addAll(getPreferences((PreferenceGroup) preference));
            }
        }
        return preferencesBuilder.build();
    }
}
