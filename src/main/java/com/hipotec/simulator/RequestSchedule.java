package com.hipotec.simulator;

import java.util.ArrayList;
import java.util.List;

import com.hipotec.simulator.calculator.CalcularCuotas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestSchedule {

  @GetMapping("/api")
  public List<Period> periods(@RequestParam(value="principal") Long principal, 
                               @RequestParam(value="interest") Float interestRate,
                               @RequestParam(value="duration") Integer duration) {
    List<Period> result = new ArrayList<>();
    CalcularCuotas caluladora = new CalcularCuotas(principal.doubleValue(), duration, interestRate, 0.0);
    result.addAll(caluladora.getCuotas());
    return result;
  }
}