package com.diderikk.backend.Controller;

import com.diderikk.backend.Model.CalculationDTO;
import com.diderikk.backend.Service.CalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @PostMapping("/calculation")
    public CalculationDTO postCalculation(@RequestBody CalculationDTO calculation){
        return calculationService.calculate(calculation);
    }
}