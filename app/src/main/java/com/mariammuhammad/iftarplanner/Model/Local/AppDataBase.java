package com.mariammuhammad.iftarplanner.Model.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mariammuhammad.iftarplanner.Model.MealStorage;

    @Database(entities = {MealStorage.class}, version = 2)
    public abstract class AppDataBase extends RoomDatabase {
        private static AppDataBase database = null;

        public abstract MealDAO mealDAO();

        public static synchronized AppDataBase getInstance(Context context) {
            if (database == null) {
                database = Room.databaseBuilder(context,
                                AppDataBase.class, "meals_database")
                        .fallbackToDestructiveMigration().build();
            }
            return database;
        }
    }

