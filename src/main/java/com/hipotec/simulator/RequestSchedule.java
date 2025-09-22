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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hipotec.simulator.calculator.CalcularCuotas;

@RestController
@RequestMapping("/api")
public class RequestSchedule {

  private static final Logger log = LoggerFactory.getLogger(RequestSchedule.class);

  @GetMapping("/periods")
  public Page<Period> periods(@RequestParam(value="principal") Double principal,
                               @RequestParam(value="interest") Double interestRate,
                               @RequestParam(value="duration") Integer duration,
                               @RequestParam(value="page", defaultValue = "0") Integer page,
                               @RequestParam(value="size", defaultValue = "20") Integer size) {
    log.info("Solicitud de cálculo de cuotas - Principal: {}, Tasa: {}%, Duración: {} años, Página: {}, Tamaño: {}",
             principal, interestRate, duration, page, size);

    // Calculate all periods
    List<Period> allPeriods = new ArrayList<>();
    CalcularCuotas caluladora = new CalcularCuotas(principal, duration * 12, interestRate, 0.0);
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

  @GetMapping("/summary")
  public LoanSummary getLoanSummary(@RequestParam(value="principal") Double principal,
                                   @RequestParam(value="interest") Double interestRate,
                                   @RequestParam(value="duration") Integer duration) {
    log.info("Solicitud de resumen de préstamo - Principal: {}, Tasa: {}%, Duración: {} años",
             principal, interestRate, duration);
    int durationMonths = duration * 12;

    // Calculate loan details
    CalcularCuotas calculadora = new CalcularCuotas(principal, durationMonths, interestRate, 0.0);
    List<Period> allPeriods = calculadora.getCuotasPeriodos();

    // Calculate summary values
    double monthlyPayment = allPeriods.stream().findFirst().map(p -> p.getPrincipal() + p.getInterest()).orElse(0.0);
    double totalInterest = allPeriods.stream().mapToDouble(Period::getInterest).sum();
    double totalCost = principal + totalInterest;

    LoanSummary summary = new LoanSummary(
      totalCost, // totalAmount (same as principal for now)
      principal, // loanAmount
      interestRate, // interestRate
      monthlyPayment, // monthlyPayment
      totalInterest, // totalInterest
      totalCost, // totalCost
      durationMonths, // durationMonths
      principal * 0.1
    );

    log.info("Resumen calculado - Cuota mensual: {}, Intereses totales: {}, Coste total: {}",
             monthlyPayment, totalInterest, totalCost);

    return summary;
  }
}
