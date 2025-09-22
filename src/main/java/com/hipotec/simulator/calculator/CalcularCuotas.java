package com.hipotec.simulator.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hipotec.simulator.Period;

/**
 * Write a description of class CalcularCuotas here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CalcularCuotas
{
    private static final Logger log = LoggerFactory.getLogger(CalcularCuotas.class);

    // instance variables - replace the example below with your own
    private double principal;
    private double tipo;
    private int duracion;
    private double cuota;
    private double diferencial;

    /**
     * Constructor for objects of class CalcularCuotas
     */
    public CalcularCuotas(double principal, int duracion, double tipo, double diferencial)
    {
        // initialise instance variables
        this.principal = principal;
        this.duracion = duracion;
        this.tipo = tipo;
        this.cuota = Double.MAX_VALUE;
        this.diferencial = diferencial;
    }

    public List<Period> getCuotasPeriodos() {
        calcularCuota(principal, duracion);
        log.debug("Cabeceras de tabla: Cuota\tPrincipal\tIntereses\tPendiente\tRestantes");
        return printPeriodos(duracion, principal);
    }

    private List<Period> printPeriodos(int numCuotas, double capitalPendiente) {
        if (numCuotas < 0 || numCuotas > duracion || numCuotas > 1000) {
            throw new IllegalArgumentException("El número de cuotas debe ser menor o igual a la duración del préstamo");
        }
        CuotaIntereses cuotaIntereses = calcularPrincipalIntereses(capitalPendiente);
        numCuotas--;
        String formato = "%d\t%.2f\t%.2f\t%.2f\t%.2f\t%d";
        Period currentPeriod;
        List<Period> periodsList;
        LocalDate fecha = LocalDate.now().plusMonths(duracion - numCuotas);
        if (numCuotas == 0) {
            double ultimaCuota = (1+(tipo / 100 / 12)) * capitalPendiente;
            double intereses = capitalPendiente*(tipo / 100 / 12);
            periodsList = new ArrayList<>();
            int periodo = periodsList.size()+1;
            currentPeriod = new Period(periodo, fecha, ultimaCuota, intereses, 0.0, 0.0);
            periodsList.add(currentPeriod);
            log.debug("Período final: {}", String.format(formato, periodo, ultimaCuota, capitalPendiente, intereses, 0.0, 0));
        }
        else {
            capitalPendiente = (1 + (tipo / 100 / 12)) * capitalPendiente - this.cuota;
            //amortizaCuota(9, 10000.0);
            //amortizaTiempo(5, 8000.0);
            periodsList = printPeriodos(numCuotas, capitalPendiente);
            int periodo = periodsList.size()+1;
            currentPeriod = new Period(periodo, fecha, cuotaIntereses.getCuotaPrincipal(), cuotaIntereses.getCuotaIntereses(), numCuotas, capitalPendiente);
            log.debug("Período {}: {}", periodo, String.format(formato, periodo, cuota, cuotaIntereses.getCuotaPrincipal(), cuotaIntereses.getCuotaIntereses(), capitalPendiente, numCuotas));
            periodsList.add(0, currentPeriod);
        }
        return periodsList;
    }

    private CuotaIntereses calcularPrincipalIntereses(double principal) {
        double tipoEfectivoMensual = tipo / 100 / 12;
        double cuotaPrincipal = cuota - (tipoEfectivoMensual * principal);
        double cuotaIntereses = tipoEfectivoMensual * principal;
        return new CuotaIntereses(cuotaPrincipal, cuotaIntereses);
    }

    private void calcularCuota(double capital, int numCuotas) {
        double tipoEfectivoMensual = tipo / 100 / 12;
        double numerador = capital * tipoEfectivoMensual;
        double tipoCuotas = Math.pow(1 + tipoEfectivoMensual, -numCuotas);
        double denominador = 1 - tipoCuotas;
        cuota = numerador / denominador;
        log.info("Cáculo de cuota. Principal: {}, tipo del préstamo: {}, número de cuotas: {}, Cuota: {}", capital, tipo, numCuotas, cuota);
    }

    /*private void amortizaCuota(int periodo, double amortizacion) {
        if (this.periodo != periodo) return;
        this.capitalPendiente = this.capitalPendiente - amortizacion;
        calcularCuota();
    }

    private void amortizaTiempo(int periodo, double amortizacion) {
        if (this.periodo != periodo || cuota == Double.MAX_VALUE) return;
        this.capitalPendiente = this.capitalPendiente - amortizacion;
        //t = NuevoT[Capital, i, c];
        double numerador = 1 - (this.capitalPendiente * (tipo / 100 / 12) / this.cuota);
        double denominador =  1 + (tipo / 100 / 12);
        double numCuotasNew = - Math.log10(numerador) / Math.log10(denominador);
        this.numCuotas = new Double(Math.ceil(numCuotasNew)).intValue();
    }*/

    record CuotaIntereses(double cuotaPrincipal, double cuotaIntereses) {
        public double getCuotaPrincipal() {
            return cuotaPrincipal;
        }
        public double getCuotaIntereses() {
            return cuotaIntereses;
        }
    }
}
