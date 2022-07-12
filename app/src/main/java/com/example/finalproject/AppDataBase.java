package com.example.finalproject;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ShoppingList.class,Items.class,LoginTable.class}, version  = 7)
public abstract class AppDataBase extends RoomDatabase {

    public abstract listDao listDao();

    private static AppDataBase INSTANCE;

    public static AppDataBase getDbInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "shoppingList")
                    .allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build();

        }

        return INSTANCE;
    }
}