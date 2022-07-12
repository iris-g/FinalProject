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

        @ColumnInfo(name = "uid")
        public  int uid ;

        public void shoppingList() {
        }

        public int getUid() {
                return uid;
        }
        public void setId(int  id) {
                this.uid = id;
        }


}
