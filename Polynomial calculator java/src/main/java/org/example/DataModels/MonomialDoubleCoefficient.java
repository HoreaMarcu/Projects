package org.example.DataModels;

public class MonomialDoubleCoefficient extends Monomial{
    private double coefficient;

    public MonomialDoubleCoefficient(int power, double coefficient) {
        super(power);
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
}

