package com.example.creatures.db.typeConverters;

import androidx.room.TypeConverter;

import com.example.creatures.other.Creature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


public class ListTypeConverter {

    @TypeConverter
    public static List<Creature> StringToList(String creatures){
        Gson gson = new Gson();
        if (creatures == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Creature>>() {}.getType();

        return gson.fromJson(creatures, listType);
    }

    @TypeConverter
    public static String CreaturesToString(List<Creature> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
