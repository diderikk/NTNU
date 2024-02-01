package com.diderikk.backend.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.diderikk.backend.Model.CalculationDTO;

import org.springframework.stereotype.Service;

@Service
public class CalculationService {
    
    public CalculationDTO calculate(CalculationDTO calculation){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            Object answer = engine.eval(calculation.getCalculationString());
            calculation.setAnswer(answer);
            return calculation;
        } catch (ScriptException e) {
            calculation.setErrorString("Error in your syntax");
            return calculation;
        }
    }
}
