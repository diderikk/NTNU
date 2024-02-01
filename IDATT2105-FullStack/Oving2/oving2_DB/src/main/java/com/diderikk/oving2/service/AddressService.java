package com.diderikk.oving2.service;

import java.util.List;
import java.util.Optional;

import com.diderikk.oving2.model.Address;
import com.diderikk.oving2.repository.AddressRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

    public List<Address> getAddresses(){
        LOGGER.warn("getAddresses() called: ");
        return addressRepository.findAll();
    }

    public Address getAddress(long id){
        LOGGER.warn("getAddress(long id) called: " + id);
        return addressRepository.findById(id).get();
    }

    public Address addAddress(Address address){
        return addressRepository.save(address);
    }

    public Address removeAddress(long id){
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()){
            addressRepository.deleteById(id);
            return address.get();
        }
        return address.get();
    }
}
