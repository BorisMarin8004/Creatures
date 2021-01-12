package com.example.creatures.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.creatures.other.Player;
import com.example.creatures.db.typeConverters.ListTypeConverter;

@Database(entities = {Player.class}, version = 1)
@TypeConverters(ListTypeConverter.class)
public abstract class PlayersDatabase extends RoomDatabase {

    public static final String DB_NAME = "PLAYERS_DATABASE";
    public static final String PLAYERS_TABLE = "PLAYERS_TABLE";

    public abstract PlayersDAO getPlayersDAO();
}
