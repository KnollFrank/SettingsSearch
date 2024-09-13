# SettingsSearch

This is a library for Android apps that allows to search Preferences.

<img width="200" src="https://raw.githubusercontent.com/ByteHamster/PreferenceSearch/master/screenshots/main.png" />
<img width="200" src="https://raw.githubusercontent.com/ByteHamster/PreferenceSearch/master/screenshots/history.png" />
<img width="200" src="https://raw.githubusercontent.com/ByteHamster/PreferenceSearch/master/screenshots/suggestions.png" />
<img width="200" src="https://raw.githubusercontent.com/ByteHamster/PreferenceSearch/master/screenshots/result.png" />

## Add SettingsSearch to your app

Add the JitPack repository to your `build.gradle`:

    allprojects {
        repositories {
            // ...
            maven { url 'https://jitpack.io' }
        }
    }

Add the SettingsSearch dependency to your `app/build.gradle`:

    dependencies {
        implementation 'com.github.KnollFrank:SettingsSearch:-SNAPSHOT'
    }

Make the Preferences of your `PreferenceFragment` searchable using a SearchPreference:

    public class PreferenceFragment extends PreferenceFragmentCompat {
    
        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            ...
            getPreferenceScreen().addPreference(createSearchPreference());
        }
       
        private SearchPreference createSearchPreference() {
            final SearchPreference searchPreference = new SearchPreference(getContext());
            searchPreference.setKey("keyOfSearchPreference");
            searchPreference.setOrder(-1);
            searchPreference.setOnPreferenceClickListener(
                    new OnPreferenceClickListener() {
    
                        private final SearchPreferenceFragments searchPreferenceFragments = createSearchPreferenceFragments();
    
                        @Override
                        public boolean onPreferenceClick(@NonNull final Preference preference) {
                            searchPreferenceFragments.showSearchPreferenceFragment();
                            return true;
                        }
                    });
            return searchPreference;
        }
    
        private SearchPreferenceFragments createSearchPreferenceFragments() {
            return SearchPreferenceFragments
                    .builder(
                            new SearchConfiguration(getId(), Optional.empty(), getClass()),
                            getParentFragmentManager())
                    .build();
        }
    }

Now, when your app displays the PreferenceScreen defined in your `PreferenceFragment` to your users, they see a SearchPreference
as the first preference of the PreferenceScreen which they can use to search and find preferences.

For a complete example use the activity `de.KnollFrank.settingssearch.PreferenceSearchExampleHavingSearchPreference`.