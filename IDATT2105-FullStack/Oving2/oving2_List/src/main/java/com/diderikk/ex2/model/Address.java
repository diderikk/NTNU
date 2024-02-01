package com.diderikk.ex2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Address {
    private long addressID;
    private String gateName;
    private int gateNumber;

    @JsonIgnore
    private Author author;

    public Address(){}

    public Address(String gateName, int gateNumber){
        this.gateName = gateName;
        this.gateNumber = gateNumber;
    }

    public long getAddressID(){
        return addressID;
    }

    public void setAddressID(long id){
        this.addressID = id;
    }

    public String getGateName(){
        return gateName;
    }

    public void setGateName(String gateName){
        this.gateName = gateName;
    }

    public int getGateNumber(){
        return gateNumber;
    }

    public void setGateNumber(int gateNumber){
        this.gateNumber = gateNumber;
    }

    public Author getAuthor(){
        return author;
    }

    public void setAuthor(Author author){
        this.author = author;
    }

    public String toString(){
        return "Address: " + addressID + "\nGate Name: " + gateName + "\nGate Number: " + gateNumber;
    }
}

