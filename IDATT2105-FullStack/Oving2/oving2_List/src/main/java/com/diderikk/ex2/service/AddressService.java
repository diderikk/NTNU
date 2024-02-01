package com.diderikk.ex2.service;

import java.util.List;

import com.diderikk.ex2.model.Address;
import com.diderikk.ex2.model.Author;
import com.diderikk.ex2.repository.AddressDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressDAO addressDAO;

    public List<Address> getAddresses(){
        return addressDAO.getAddresses();
    }

    public Address getAddress(long id){
        return addressDAO.getAddressByID(id);
    }

    public Address addAddress(Address address){
        return addressDAO.addAddress(address);
    }

    public Address addNewAuthor(long id, Author author){
        Address address = getAddress(id);
        address.setAuthor(author);
        return addressDAO.updateAddress(id, address);
    }

    public Address removeAddress(long id){
        return addressDAO.removeAddressByID(id);
    }
}
