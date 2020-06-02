package com.hipotec.simulator.calculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hipotec.simulator.Period;

/**
 * Write a description of class CalcularCuotas here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CalcularCuotas
{
    // instance variables - replace the example below with your own
    private int periodo;
    private double capitalPendiente;
    private double tipo;
    private int numCuotas;
    private double cuota;
    private double cuotaIntereses;
    private double cuotaPrincipal;
    private double amortizacionParcial;
    private double diferencial;

    /**
     * Constructor for objects of class CalcularCuotas
     */
    public CalcularCuotas(double capitalPendiente, int duracion, double tipo, double diferenciañ)
    {
        // initialise instance variables
        periodo = 0;
        this.capitalPendiente = capitalPendiente;
        this.numCuotas = duracion;
        this.tipo = tipo;
        this.cuota = Double.MAX_VALUE;
        this.diferencial = diferencial;
    }
    
    public void printCuotas() {
        calcularCuota();
        System.out.println("\tCuota\tPrincipal\tIntereses\tPendiente\tRestantes");
        printPeriodos();
    }
    
    private List<Period> printPeriodos() {
        calcularPrincipalIntereses();
        numCuotas--;
        periodo++;
        String formato = "%d\t%.2f\t%.2f\t%.2f\t%.2f\t%d";
        Period currentPeriod;
        List<Period> periodsList;
        if (numCuotas == 0) {
            double ultimaCuota = (1+(tipo / 100 / 12))*capitalPendiente;
            double intereses = capitalPendiente*(tipo / 100 / 12);
            currentPeriod = new Period(periodo, this.capitalPendiente, intereses, 0.0, 0.0);
            periodsList = new ArrayList<>();
            periodsList.add(currentPeriod);
            System.out.println(String.format(formato, periodo, ultimaCuota, this.capitalPendiente, intereses, 0.0, 0));
        }
        else {
            this.capitalPendiente = (1 + (tipo / 100 / 12))*this.capitalPendiente - this.cuota;
            //amortizaCuota(9, 10000.0);
            //amortizaTiempo(5, 8000.0);
            currentPeriod = new Period(periodo, this.cuotaPrincipal, this.cuotaIntereses, this.numCuotas, this.capitalPendiente);
            System.out.println(String.format(formato, periodo, this.cuota, this.cuotaPrincipal, this.cuotaIntereses, this.capitalPendiente, this.numCuotas));
            periodsList = printPeriodos();
            periodsList.add(0, currentPeriod);
        }
        return periodsList;
    }
    
    private void calcularPrincipalIntereses() {
        double tipoEfectivoMensual = tipo / 100 / 12;
        cuotaPrincipal = cuota - (tipoEfectivoMensual*capitalPendiente);
        cuotaIntereses = tipoEfectivoMensual * capitalPendiente;
    }
    
    private void calcularCuota() {
        double tipoEfectivoMensual = tipo / 100 / 12;
        double numerador = capitalPendiente * tipoEfectivoMensual;
        double tipoCuotas = Math.pow(1 + tipoEfectivoMensual, -numCuotas);
        double denominador = 1 - tipoCuotas;
        cuota = numerador / denominador;
    }

    private void amortizaCuota(int periodo, double amortizacion) {
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
    }

	public List<Period> getCuotas() {
        calcularCuota();
        List<Period> cuotasPeriods = printPeriodos();
		return cuotasPeriods;
	}
}
