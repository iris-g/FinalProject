package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface listDao {

    @Query("Select * from shoppingList ")
    List<ShoppingList> getAllItemsList();

    @Insert
    void insertItems(ShoppingList items);

    @Update
    void updateItems(ShoppingList items);

    @Delete
    void deleteItem(ShoppingList items);


}

