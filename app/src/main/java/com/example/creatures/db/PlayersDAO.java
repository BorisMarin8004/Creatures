package com.example.creatures.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;



import com.example.creatures.other.Player;

import java.util.List;


@Dao
public interface PlayersDAO {

    @Insert
    void insert(Player... players);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Player... players);

    @Delete
    void delete(Player player);

    @Query("SELECT * FROM " + PlayersDatabase.PLAYERS_TABLE)
    List<Player> getPlayers();

    @Query("SELECT * FROM " + PlayersDatabase.PLAYERS_TABLE + " WHERE id = :id")
    Player getPlayerById(int id);

    @Query("SELECT * FROM " + PlayersDatabase.PLAYERS_TABLE + " WHERE username = :username")
    Player getPlayerByUsername(String username);

    @Query("SELECT * FROM " + PlayersDatabase.PLAYERS_TABLE + " WHERE active = 1")
    Player getActivePlayer();

    @Query("SELECT * FROM " + PlayersDatabase.PLAYERS_TABLE + " WHERE readyToFight = 1")
    List<Player> getPlayersToFight();

}
