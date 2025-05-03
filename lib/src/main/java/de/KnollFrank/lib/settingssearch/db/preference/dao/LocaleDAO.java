package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale;

@Dao
public abstract class LocaleDAO {

    public Set<Locale> getLocales() {
        return new HashSet<>(_getLocales());
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void persist(Locale locale);

    @Query("SELECT * FROM Locale")
    protected abstract List<Locale> _getLocales();
}