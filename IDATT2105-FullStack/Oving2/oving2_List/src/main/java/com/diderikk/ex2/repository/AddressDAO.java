package com.diderikk.ex2.repository;

import java.util.ArrayList;
import java.util.List;

import com.diderikk.ex2.model.Address;

import org.springframework.stereotype.Repository;

@Repository
public class AddressDAO {
    private List<Address> addresses;
    private int length;

    public AddressDAO(){
        this.addresses = new ArrayList<>();
        this.length = 0;
    }

    public AddressDAO(List<Address> addresses){
        this.addresses = addresses;
        this.length = 0;
    }

    public List<Address> getAddresses(){
        return addresses;
    }

    public Address getAddressByID(long id){
        for(Address address : addresses){
            if(address.getAddressID() == id) return address;
        }
        return null;
    }

    public Address addAddress(Address address){
        address.setAddressID(++length);
        addresses.add(address);
        return address;
    }

    public Address updateAddress(long id, Address address){
        int index = -1;
        for(int i = 0; i<addresses.size(); i++){
            if(addresses.get(i).getAddressID() == id){
                address.setAddressID(addresses.get(i).getAddressID());
                index = i;
            }
        }
        addresses.set(index, address);
        return address;
    }

    public Address removeAddressByID(long id){
        for(Address address : addresses){
            if(address.getAddressID() == id){
                addresses.remove(address);
                return address;
            }
        }
        return null;
    }
}
