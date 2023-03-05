package org.example.BusinessLogic;

import org.example.DataModels.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.Collections;

public class Operations {
    public static Polynomial addition(Polynomial polynomial1,Polynomial polynomial2){
        for(Monomial value: polynomial2.getMonomialsList()){
            polynomial1.addMonomial(value);
        }
        Collections.sort(polynomial1.getMonomialsList());
        return polynomial1;
    }
    public static Polynomial subtraction(Polynomial polynomial1, Polynomial polynomial2){
        for(Monomial value : polynomial2.getMonomialsList()){
            int coefficient = ((MonomialIntCoefficient)value).getCoefficient();
            ((MonomialIntCoefficient)value).setCoefficient(-coefficient);
        }
        return addition(polynomial1,polynomial2);
    }
    public static Polynomial multiplication(Polynomial polynomial1, Polynomial polynomial2){
        Polynomial result = new Polynomial();
        for(Monomial value1: polynomial1.getMonomialsList()){
            for(Monomial value2 : polynomial2.getMonomialsList()){
                int power = value1.getPower()+ value2.getPower();
                int coefficient = ((MonomialIntCoefficient) value1).getCoefficient()*((MonomialIntCoefficient) value2).getCoefficient();
                MonomialIntCoefficient monomialResult = new MonomialIntCoefficient(power,coefficient);
                result.addMonomial(monomialResult);
            }
        }
        Collections.sort(result.getMonomialsList());
        return result;
    }
    public static Polynomial derivative(Polynomial polynomial1){
        Polynomial result = new Polynomial();
        for(Monomial value: polynomial1.getMonomialsList()){
            if(value.getPower()!=0) {
                ((MonomialIntCoefficient) value).setCoefficient(((MonomialIntCoefficient) value).getCoefficient() * value.getPower());
                value.setPower(value.getPower() - 1);
                result.getMonomialsList().add(value);
            }
        }
        Collections.sort(result.getMonomialsList());
        return result;
    }
    public static Polynomial integration(Polynomial polynomial1){
        Polynomial result = new Polynomial();
        for(Monomial value : polynomial1.getMonomialsList()){
            MonomialDoubleCoefficient m = new MonomialDoubleCoefficient(value.getPower()+1,(double)((MonomialIntCoefficient)value).getCoefficient() / (value.getPower()+1) );
            result.addMonomial(m);
        }
        Collections.sort(result.getMonomialsList());
        return result;
    }
}
