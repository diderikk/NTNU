package com.diderikk.ex2.controller;

import java.util.List;

import com.diderikk.ex2.model.Address;
import com.diderikk.ex2.service.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/addresses")
    public List<Address> getAddresses(){
        return addressService.getAddresses();
    }

    @GetMapping("/address/{id}")
    public Address getAddress(@PathVariable("id") Long id){
        return addressService.getAddress(id);
    }

    @PostMapping("/addresses")
    public Address addAddress(@RequestBody Address address){
        return addressService.addAddress(address);
    }

    @DeleteMapping("/address/{id}")
    public Address removeAddress(@PathVariable("id") Long id){
        return addressService.removeAddress(id);
    }
}
