package com.hipotec.simulator;

public class Period {

    private int id;
    private double principal;
    private double interest;
    private double restante;
    private double capitalPendiente;

    public int getId() {
        return id;
    }

    public void setId(int index) {
        this.id = index;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interests) {
        this.interest = interests;
    }

    public double getRestante() {
        return restante;
    }

    public void setRestante(double pending) {
        this.restante = pending;
    }

    public Period(int index, double principal, double interests, double pending, double capitalPendiente) {
        this.id = index;
        this.principal = principal;
        this.interest = interests;
        this.restante = pending;
        this.capitalPendiente = capitalPendiente;
    }

    public double getCapitalPendiente() {
        return capitalPendiente;
    }

    public void setCapitalPendiente(double capitalPendiente) {
        this.capitalPendiente = capitalPendiente;
    }
}