package com.example.finalproject;

public class Item {
    String name;
    String details;

    public Item(String name, String details){
        this.name = name;
        this.details=details;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public  String getName() {
        return name;
    }

    public String getDetails(){
        return this.details;
    }


}
