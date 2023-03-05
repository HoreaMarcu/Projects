package org.example.DataModels;

import java.util.ArrayList;

public class Polynomial {
    private ArrayList<Monomial> monomialsList = new ArrayList<>();

    public ArrayList<Monomial> getMonomialsList() {
        return monomialsList;
    }

    public void setMonomialsList(ArrayList<Monomial> monomialsList) {
        this.monomialsList = monomialsList;
    }

    public boolean equals(Polynomial polynomial){
        if(this.getMonomialsList().size() != polynomial.getMonomialsList().size()) return false;
        for(int i = 0; i < this.getMonomialsList().size(); i++){
            if(this.getMonomialsList().get(i).getPower() != polynomial.getMonomialsList().get(i).getPower()) return false;
            if(this.getMonomialsList().get(i) instanceof MonomialIntCoefficient){
                if(((MonomialIntCoefficient)this.getMonomialsList().get(i)).getCoefficient() != ((MonomialIntCoefficient)polynomial.getMonomialsList().get(i)).getCoefficient()) return false;
            }
            else{
                if(((MonomialDoubleCoefficient)this.getMonomialsList().get(i)).getCoefficient() != ((MonomialDoubleCoefficient)polynomial.getMonomialsList().get(i)).getCoefficient()) return false;
            }
        }
        return true;
    }

    public int addMonomial(Monomial monomial){
        for (Monomial value : monomialsList) {
            if (value.getPower() == monomial.getPower()) {
                double coefficient1 = 0;
                double coefficient2 = 0;
                if (value instanceof MonomialIntCoefficient) {coefficient1 = ((MonomialIntCoefficient) value).getCoefficient();
                } else {coefficient1 = ((MonomialDoubleCoefficient) value).getCoefficient();
                }
                if (monomial instanceof MonomialIntCoefficient) {coefficient2 = ((MonomialIntCoefficient) monomial).getCoefficient();
                } else {coefficient2 = ((MonomialDoubleCoefficient) monomial).getCoefficient();
                }
                if (value instanceof MonomialIntCoefficient) {
                    ((MonomialIntCoefficient) value).setCoefficient((int) (coefficient1 + coefficient2));
                    if(((MonomialIntCoefficient) value).getCoefficient() == 0){monomialsList.remove(value);
                    }
                } else {
                    ((MonomialDoubleCoefficient) value).setCoefficient(coefficient1 + coefficient2);
                    if(((MonomialDoubleCoefficient) value).getCoefficient() == 0){monomialsList.remove(value);
                    }
                }
                return 1;
            }
        }
        if(monomial instanceof MonomialIntCoefficient && ((MonomialIntCoefficient)monomial).getCoefficient() != 0) {monomialsList.add(monomial);
        }
        else{if(monomial instanceof MonomialDoubleCoefficient && ((MonomialDoubleCoefficient)monomial).getCoefficient() != 0){monomialsList.add(monomial);
        }
        }
        return 1;
    }
    public static Polynomial readPolynomial(String polynomialAsString){
        Polynomial polynomial = new Polynomial();
        int positionInString = 0;
        boolean minus = false;
        while(positionInString < polynomialAsString.length()){
            int coefficient = 0;
            boolean negative = false;
            if(polynomialAsString.charAt(positionInString) =='-'){
                negative  =true;
                positionInString++;
            }
            if(minus) negative = true;
            while(polynomialAsString.charAt(positionInString) >= '0' && polynomialAsString.charAt(positionInString)<='9'){
                coefficient = coefficient * 10 + Character.getNumericValue(polynomialAsString.charAt(positionInString));
                positionInString++; // last is going to read x
            }
            if(negative) coefficient = -coefficient;
            positionInString++; // is going to read ^
            positionInString++; // read first digit from power
            int power = 0;
            while(polynomialAsString.charAt(positionInString) >= '0' && polynomialAsString.charAt(positionInString)<='9'){
                power = power * 10 + Character.getNumericValue(polynomialAsString.charAt(positionInString));
                positionInString++; //last is going to read the operand or /n
                if(positionInString == polynomialAsString.length()){
                    break;
                }
            }
            if(positionInString < polynomialAsString.length())minus = polynomialAsString.charAt(positionInString) == '-';
            positionInString++;
            MonomialIntCoefficient monomialIntCoefficient = new MonomialIntCoefficient(power,coefficient);
            polynomial.addMonomial(monomialIntCoefficient);
        }
        return polynomial;
    }

    public static String getStringFromPolynomial(Polynomial polynomial){
        String result = "";
        boolean first = true;
        for(Monomial value : polynomial.getMonomialsList()){
            if(value instanceof MonomialIntCoefficient) {
                if (first) first = false;
                else {
                    if (!first && ((MonomialIntCoefficient) value).getCoefficient() > 0) {
                        result += "+";
                    }
                }
                    result += ((MonomialIntCoefficient) value).getCoefficient() + "x^" + value.getPower();
                }
            else{
                if (first) first = false;
                else {
                    if (!first && ((MonomialDoubleCoefficient) value).getCoefficient() > 0) {
                        result += "+";
                    }
                }
                result+=String.format ("%.2f",((MonomialDoubleCoefficient) value).getCoefficient())+"x^"+value.getPower();
            }
        }
        return result;
    }
}
