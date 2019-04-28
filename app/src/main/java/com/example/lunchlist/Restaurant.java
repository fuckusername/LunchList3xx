package com.example.lunchlist;

public class Restaurant {
    private String name = "";
    private String address = "";
    private String type= "";
    private String notes="";

    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getType(){
        return type;
    }
    public String getNotes(){
        return notes;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }


//    public String toString(){
//        return getName();
//    }
}
