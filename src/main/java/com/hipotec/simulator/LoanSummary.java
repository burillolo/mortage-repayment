package com.hipotec.simulator;

public class LoanSummary {
    private double totalAmount;
    private double loanAmount;
    private double interestRate;
    private double monthlyPayment;
    private double totalInterest;
    private double totalCost;
    private int durationMonths;
    
    public LoanSummary() {}
    
    public LoanSummary(double totalAmount, double loanAmount, double interestRate, 
                      double monthlyPayment, double totalInterest, double totalCost, int durationMonths) {
        this.totalAmount = totalAmount;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.monthlyPayment = monthlyPayment;
        this.totalInterest = totalInterest;
        this.totalCost = totalCost;
        this.durationMonths = durationMonths;
    }
    
    // Getters and Setters
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }
    
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    
    public double getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(double monthlyPayment) { this.monthlyPayment = monthlyPayment; }
    
    public double getTotalInterest() { return totalInterest; }
    public void setTotalInterest(double totalInterest) { this.totalInterest = totalInterest; }
    
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    
    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
}
