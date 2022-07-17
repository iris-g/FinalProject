package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface listDao {



    @Query("Select * from shoppingList where uid= :userId")
    List<ShoppingList> getAllLists(int userId);

    @Insert
    void insertLists(ShoppingList items);

    @Query("Select * from Items where listName = :list_Name")
    List<Items> getAllItemsList(String list_Name);

    @Query("Select * from Items where itemName = :item_name")
    Items getItem(String item_name );


    @Delete
    void deleteLists(ShoppingList items);


    @Insert
    void insertItems(Items items);

    @Update
    void updateItems(Items items);

    @Delete
    void deleteItem(Items items);


    ///for user login
    @Insert
    void insertDetails(LoginTable data);

    @Query("select * from LoginDetails")
    List<LoginTable> getDetails();

    @Query("delete from LoginDetails")
    void deleteAllData();

    @Query("SELECT * FROM LoginDetails WHERE Email=:email AND Password=:password")
     LoginTable getUserDetails(String email, String password);

    @Query("SELECT * FROM LoginDetails WHERE Email=:email ")
    LoginTable getUserByEmail(String email);
}

