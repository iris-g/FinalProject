package com.example.finalproject;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Items {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "listName")
    public String listName;

    @ColumnInfo(name = "itemName")
    public  String itemName;

    @ColumnInfo(name = "itemExtra")
    public  String itemExtra;


    @ColumnInfo(name = "completed")
    public  boolean completed;
}