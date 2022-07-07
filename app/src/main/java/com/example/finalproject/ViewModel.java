package com.example.finalproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ViewModel extends AndroidViewModel  {
    private MutableLiveData<List<Item>> itemsList ;
    ArrayList<Item> itemsArrayList;
    private MutableLiveData <Integer> selectedRow;



    private MutableLiveData <Integer> itemsCount;

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Item>> getItems ()
    {
        if( itemsList== null ){
            itemsList = new MutableLiveData <List<Item>>();

        }
        return itemsList;

    }
    public void setItems(  ArrayList<Item> items) {
        itemsArrayList=items;
        this.itemsList.setValue(itemsArrayList);
    }
    public void setItemsCount(int itemsCount) {
       this.itemsCount.setValue(itemsCount);
    }
    //set the live data for the selected row
    public void setSelectedRow(int  selectedRow) {
        this.selectedRow.setValue(selectedRow) ;
    }

    public MutableLiveData <Integer>  getSelectedRow ()
    {
        if( selectedRow== null ){
            selectedRow = new MutableLiveData <Integer>(-1);
        }

        return selectedRow;



    }
    public MutableLiveData <Integer>  getItemsCount ()
    {
        if( itemsCount== null ){
            itemsCount = new MutableLiveData <Integer>(0);
        }

        return itemsCount;



    }

}
