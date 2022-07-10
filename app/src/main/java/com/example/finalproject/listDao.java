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
    void insertLists(ShoppingList items);

    @Query("Select * from Items where listName = :list_Name")
    List<Items> getAllItemsList(String list_Name);

    @Query("Select * from Items where itemName = :item_name")
    Items getItem(String item_name);


    @Delete
    void deleteLists(ShoppingList items);


    @Insert
    void insertItems(Items items);

    @Update
    void updateItems(Items items);

    @Delete
    void deleteItem(Items items);
}

