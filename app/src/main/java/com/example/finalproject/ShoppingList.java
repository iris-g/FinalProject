package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShoppingList {

        @PrimaryKey
        @NonNull
        public String name ;

        @ColumnInfo(name = "details")
        public String details;

        @ColumnInfo(name = "itemName")
        public  String itemName;
        public void shoppingList() {
        }

    }
