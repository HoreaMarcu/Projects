package org.example.DataModels;

public class MonomialIntCoefficient extends Monomial {
    private int coefficient;

    public MonomialIntCoefficient(int power, int coefficient) {
        super(power);
        this.coefficient = coefficient;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = (int)coefficient;
    }
}
