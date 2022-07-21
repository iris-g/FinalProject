package com.example.finalproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class usersViewModel extends AndroidViewModel {
    private MutableLiveData<List<User>> usersList ;
    ArrayList<User> usersArrayList;
    private MutableLiveData <Integer> itemsCount;
    private MutableLiveData <Integer> selectedRow;

    public usersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<User>> getUsers ()
    {
        if( usersList== null ){
            usersList = new MutableLiveData <List<User>>();

        }
        return usersList;

    }
    public void setUsers(ArrayList<User> users) {
        usersArrayList=users;
        this.usersList.setValue(usersArrayList);
    }
    public MutableLiveData <Integer>  getItemsCount ()
    {
        if( itemsCount== null ){
            itemsCount = new MutableLiveData <Integer>(0);
        }

        return itemsCount;



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
    public void setItemsCount(int itemsCount) {
        this.itemsCount.setValue(itemsCount);
    }
}
