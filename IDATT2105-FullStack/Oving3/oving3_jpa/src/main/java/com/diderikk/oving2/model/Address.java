package com.diderikk.oving2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private long addressID;
    private String gateName;
    private int gateNumber;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
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
