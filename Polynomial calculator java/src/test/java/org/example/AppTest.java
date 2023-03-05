package org.example;

import static org.junit.Assert.assertTrue;

import org.example.BusinessLogic.Operations;
import org.example.DataModels.MonomialDoubleCoefficient;
import org.example.DataModels.MonomialIntCoefficient;
import org.example.DataModels.Polynomial;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void AddMonomialTest(){
        Polynomial p1 = new Polynomial();
        MonomialIntCoefficient m1 = new MonomialIntCoefficient(1,1);
        MonomialIntCoefficient m2 = new MonomialIntCoefficient(2,2);
        p1.addMonomial(m1);
        p1.addMonomial(m2);
        System.out.println(p1.getMonomialsList().size());
        System.out.println("power: " +p1.getMonomialsList().get(0).getPower()+" coef: "+((MonomialIntCoefficient)p1.getMonomialsList().get(0)).getCoefficient());
        System.out.println("power: " +p1.getMonomialsList().get(1).getPower()+" coef: "+((MonomialIntCoefficient)p1.getMonomialsList().get(1)).getCoefficient());
    }

    @Test
    public void EqualsTest(){
        Polynomial p1 = Polynomial.readPolynomial("2x^2+1x^1+3x^0");
        MonomialIntCoefficient monomial1 = new MonomialIntCoefficient(2,2);
        MonomialIntCoefficient monomial2 = new MonomialIntCoefficient(1,1);
        MonomialIntCoefficient monomial3 = new MonomialIntCoefficient(0,3);
        Polynomial p2 = new Polynomial();
        p2.getMonomialsList().add(monomial1);
        p2.getMonomialsList().add(monomial2);
        p2.getMonomialsList().add(monomial3);
        assertTrue("Equals does not work as intended",p1.equals(p2));
    }
    @Test
    public void AdditionTest(){
        Polynomial p1 = Polynomial.readPolynomial("2x^2+1x^1+3x^0");
        Polynomial p2 = Polynomial.readPolynomial("3x^3+2x^1+5x^0");
        Polynomial result = Polynomial.readPolynomial("3x^3+2x^2+3x^1+8x^0");
        Polynomial resultAddition = Operations.addition(p1,p2);
        assertTrue("Addition does not work as intended",result.equals(resultAddition));
    }
    @Test
    public void SubtractionTest(){
        Polynomial p1 = Polynomial.readPolynomial("2x^2+1x^1+3x^0");
        Polynomial p2 = Polynomial.readPolynomial("3x^3+2x^1+5x^0");
        Polynomial result = Polynomial.readPolynomial("-3x^3+2x^2-1x^1-2x^0");
        Polynomial resultSubtraction = Operations.subtraction(p1,p2);
        assertTrue("Subtraction does not work as intended",result.equals(resultSubtraction));
    }
    @Test
    public void MultiplicationTest(){
        Polynomial p1 = Polynomial.readPolynomial("1x^1+1x^0");
        Polynomial p2 = Polynomial.readPolynomial("1x^1+2x^0");
        Polynomial result = Polynomial.readPolynomial("1x^2+3x^1+2x^0");
        Polynomial resultMultiplication = Operations.multiplication(p1,p2);
        assertTrue("Multiplication does not work as intended",result.equals(resultMultiplication));
    }
    @Test
    public void DerivativeTest(){
        Polynomial p1 = Polynomial.readPolynomial("3x^2+2x^1+5x^0");
        Polynomial result = Polynomial.readPolynomial("6x^1+2x^0");
        Polynomial resultDerivative = Operations.derivative(p1);
        assertTrue("Derivative does not work as intended",result.equals(resultDerivative));
    }
    @Test
    public void IntegrationTest(){
        Polynomial p1 = Polynomial.readPolynomial("3x^2+2x^1+5x^0");
        MonomialDoubleCoefficient m1 = new MonomialDoubleCoefficient(3,1.00);
        MonomialDoubleCoefficient m2 = new MonomialDoubleCoefficient(2,1.00);
        MonomialDoubleCoefficient m3 = new MonomialDoubleCoefficient(1,5.00);
        Polynomial result = new Polynomial();
        result.getMonomialsList().add(m1);
        result.getMonomialsList().add(m2);
        result.getMonomialsList().add(m3);
        Polynomial resultIntegration = Operations.integration(p1);
        assertTrue("Integration does not work as intended",result.equals(resultIntegration));
    }
}
