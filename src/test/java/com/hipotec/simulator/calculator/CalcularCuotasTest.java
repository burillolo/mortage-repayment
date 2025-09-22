package com.hipotec.simulator.calculator;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hipotec.simulator.Period;

import static org.junit.jupiter.api.Assertions.*;

class CalcularCuotasTest {

    private static final Logger log = LoggerFactory.getLogger(CalcularCuotasTest.class);
    
    private CalcularCuotas calculator;

    @BeforeEach
    void setUp() {
        // Initialize calculator with test parameters: principal=38000, duracion=420, tipo=2%, diferencial=0
        calculator = new CalcularCuotas(38000.0, 420, 2.0, 0.0);
    }

    @Test
    @DisplayName("Test cálculo de cuotas con principal=38000, duración=420 meses, tipo=2%, diferencial=0")
    void testCalcularCuotasWithSpecificParameters() {
        // Act
        List<Period> periods = calculator.getCuotasPeriodos();
        
        // Assert
        log.info("Generados {} períodos para el cálculo", periods.size());
        
        // Verify basic constraints
        assertNotNull(periods, "La lista de períodos no debe ser null");
        assertEquals(420, periods.size(), "Debe generar exactamente 420 períodos");
        
        // Verify that all periods are properly created
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            assertNotNull(period, "El período " + (i + 1) + " no debe ser null");
            assertTrue(period.getId() > 0, "El ID del período debe ser positivo");
            assertTrue(period.getPrincipal() >= 0, "El principal debe ser mayor o igual a 0");
            assertTrue(period.getInterest() >= 0, "Los intereses deben ser mayor o igual a 0");
        }
        
        // Verify first period
        Period firstPeriod = periods.get(0);
        log.info("Primer período - ID: {}, Principal: {}, Interés: {}, Restante: {}, Capital Pendiente: {}", 
                 firstPeriod.getId(), firstPeriod.getPrincipal(), firstPeriod.getInterest(), 
                 firstPeriod.getRestante(), firstPeriod.getCapitalPendiente());
        
        assertEquals(420, firstPeriod.getId(), "El primer período debe tener ID 420");
        assertTrue(firstPeriod.getPrincipal() > 0, "El principal del primer período debe ser positivo");
        assertTrue(firstPeriod.getInterest() > 0, "Los intereses del primer período deben ser positivos");
        
        // Verify last period (should have remaining balance = 0)
        Period lastPeriod = periods.get(periods.size() - 1);
        log.info("Último período - ID: {}, Principal: {}, Interés: {}, Restante: {}, Capital Pendiente: {}", 
                 lastPeriod.getId(), lastPeriod.getPrincipal(), lastPeriod.getInterest(), 
                 lastPeriod.getRestante(), lastPeriod.getCapitalPendiente());
        
        assertEquals(1, lastPeriod.getId(), "El último período debe tener ID 1");
        assertEquals(0.0, lastPeriod.getCapitalPendiente(), 0.01, "El capital pendiente al final debe ser 0");
        assertEquals(0.0, lastPeriod.getRestante(), 0.01, "Los meses restantes al final deben ser 0");
        
        // Verify total payments approximately equal initial principal
        double totalPrincipal = periods.stream()
                .mapToDouble(Period::getPrincipal)
                .sum();
        
        log.info("Total principal pagado: {}", totalPrincipal);
        assertEquals(38000.0, totalPrincipal, 1.0, "La suma total del principal debe ser aproximadamente 38000");
        
        // Calculate total interest paid
        double totalInterest = periods.stream()
                .mapToDouble(Period::getInterest)
                .sum();
        
        log.info("Total intereses pagados: {}", totalInterest);
        assertTrue(totalInterest > 0, "El total de intereses debe ser positivo");
        
        // Calculate total amount paid (principal + interest)
        double totalPaid = totalPrincipal + totalInterest;
        log.info("Total pagado (principal + intereses): {}", totalPaid);
        assertTrue(totalPaid > 38000.0, "El total pagado debe ser mayor al principal inicial");
        
        // Verify the periods are in descending order by ID
        for (int i = 0; i < periods.size() - 1; i++) {
            assertTrue(periods.get(i).getId() > periods.get(i + 1).getId(), 
                      "Los períodos deben estar ordenados en orden descendente por ID");
        }
    }

    @Test
    @DisplayName("Test validación de parámetros del constructor")
    void testConstructorParameters() {
        // Test that constructor properly initializes the calculator
        CalcularCuotas testCalculator = new CalcularCuotas(50000.0, 360, 3.5, 0.5);
        assertNotNull(testCalculator, "El calculador debe ser creado correctamente");
        
        // Generate periods to ensure the calculator works with different parameters
        List<Period> periods = testCalculator.getCuotasPeriodos();
        assertNotNull(periods, "La lista de períodos no debe ser null");
        assertEquals(360, periods.size(), "Debe generar exactamente 360 períodos");
    }

    @Test
    @DisplayName("Test comportamiento con duración pequeña")
    void testSmallDuration() {
        // Test with a smaller duration to verify behavior
        CalcularCuotas shortTermCalculator = new CalcularCuotas(10000.0, 12, 5.0, 0.0);
        List<Period> periods = shortTermCalculator.getCuotasPeriodos();
        
        assertNotNull(periods, "La lista de períodos no debe ser null");
        assertEquals(12, periods.size(), "Debe generar exactamente 12 períodos");
        
        // Verify last period has zero balance
        Period lastPeriod = periods.get(periods.size() - 1);
        assertEquals(0.0, lastPeriod.getCapitalPendiente(), 0.01, "El capital pendiente al final debe ser 0");
        
        log.info("Prueba con duración corta completada - {} períodos generados", periods.size());
    }
}
