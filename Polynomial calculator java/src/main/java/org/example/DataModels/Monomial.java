package org.example.DataModels;

import java.util.Collections;

public class Monomial implements Comparable<Monomial> {
    private int power;

    public Monomial(int power) {
        this.power = power;
    }
    
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }


    @Override
    public int compareTo(Monomial o) {
        if(this.getPower()>o.getPower()) return -1;
        if(this.getPower()<o.getPower()) return 1;
        return 0;
    }
}
