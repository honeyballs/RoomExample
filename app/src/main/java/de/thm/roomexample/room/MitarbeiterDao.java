package de.thm.roomexample.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

@Dao
public interface MitarbeiterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMitarbeiter(Mitarbeiter...mitarbeiter);

    @Delete
    public void removeMitarbeiter(Mitarbeiter mitarbeiter);

    @Update
    public void updateMitarbeiter(Mitarbeiter...mitarbeiter);

    @Query("SELECT * FROM mitarbeiter")
    public List<Mitarbeiter> getAllMitarbeiter();

    @Query("SELECT * FROM mitarbeiter WHERE id = :id")
    public Mitarbeiter getMitarbeiterById(int id);

    @Query("SELECT * FROM mitarbeiter WHERE abt_id = :abtId")
    public List<Mitarbeiter> getMitarbeiterOfAbteilung(int abtId);

}
