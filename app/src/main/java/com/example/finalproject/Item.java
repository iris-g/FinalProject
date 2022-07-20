package com.example.finalproject;

public class Item {
    String name;
    String details;
    String productId;
    String listName;
    boolean isChecked;



    public Item(String name, String details,String id,String listName){
        this.name = name;
        this.details=details;
        this.productId=id;
        this.listName=listName;
    }

    /*
    public Item(String title, String content,String itemId){
        this.name = title;
        this.details=content;
        this.productId=itemId;

    }
    */


    public Item() {
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
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }



}
