package com.hipotec.simulator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hipotec.simulator.calculator.CalcularCuotas;

@RestController
public class RequestSchedule {
  
  private static final Logger log = LoggerFactory.getLogger(RequestSchedule.class);

  @GetMapping("/api")
  public Page<Period> periods(@RequestParam(value="principal") Long principal, 
                               @RequestParam(value="interest") Float interestRate,
                               @RequestParam(value="duration") Integer duration,
                               @RequestParam(value="page", defaultValue = "0") Integer page,
                               @RequestParam(value="size", defaultValue = "20") Integer size) {
    log.info("Solicitud de cálculo de cuotas - Principal: {}, Tasa: {}%, Duración: {} años, Página: {}, Tamaño: {}", 
             principal, interestRate, duration, page, size);
    
    // Calculate all periods
    List<Period> allPeriods = new ArrayList<>();
    CalcularCuotas caluladora = new CalcularCuotas(principal.doubleValue(), duration * 12, interestRate.doubleValue(), 0.0);
    allPeriods.addAll(caluladora.getCuotasPeriodos());
    
    log.info("Cálculo completado. Generados {} períodos totales", allPeriods.size());
    
    // Create pageable object
    Pageable pageable = PageRequest.of(page, size);
    
    // Calculate pagination boundaries
    int totalElements = allPeriods.size();
    int startIndex = (int) pageable.getOffset();
    int endIndex = Math.min(startIndex + pageable.getPageSize(), totalElements);
    
    // Extract the page content
    List<Period> pageContent = new ArrayList<>();
    if (startIndex < totalElements) {
      pageContent = allPeriods.subList(startIndex, endIndex);
    }
    
    log.info("Devolviendo página {} de {} elementos (total: {} períodos)", 
             page, pageContent.size(), totalElements);
    
    // Create and return Page object
    return new PageImpl<>(pageContent, pageable, totalElements);
  }

  @GetMapping("/api/summary")
  public LoanSummary getLoanSummary(@RequestParam(value="principal") Long principal, 
                                   @RequestParam(value="interest") Float interestRate,
                                   @RequestParam(value="duration") Integer duration) {
    log.info("Solicitud de resumen de préstamo - Principal: {}, Tasa: {}%, Duración: {} años", 
             principal, interestRate, duration);
    
    // Calculate loan details
    CalcularCuotas calculadora = new CalcularCuotas(principal.doubleValue(), duration * 12, interestRate.doubleValue(), 0.0);
    List<Period> allPeriods = calculadora.getCuotasPeriodos();
    
    // Calculate summary values
    double monthlyPayment = allPeriods.isEmpty() ? 0 : allPeriods.get(allPeriods.size() - 1).getPrincipal() + allPeriods.get(allPeriods.size() - 1).getInterest();
    double totalInterest = allPeriods.stream().mapToDouble(Period::getInterest).sum();
    double totalCost = principal.doubleValue() + totalInterest;
    
    LoanSummary summary = new LoanSummary(
      principal.doubleValue(), // totalAmount (same as principal for now)
      principal.doubleValue(), // loanAmount
      interestRate.doubleValue(), // interestRate
      monthlyPayment, // monthlyPayment
      totalInterest, // totalInterest
      totalCost, // totalCost
      duration * 12 // durationMonths
    );
    
    log.info("Resumen calculado - Cuota mensual: {}, Intereses totales: {}, Coste total: {}", 
             monthlyPayment, totalInterest, totalCost);
    
    return summary;
  }
}