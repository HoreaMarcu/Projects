package org.example.GraphicalUserInterface;

import org.example.BusinessLogic.*;
import org.example.DataModels.MonomialIntCoefficient;
import org.example.DataModels.Polynomial;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Controller implements ActionListener {

    private View view;

    private Operations operations = new Operations();

    public Controller(View v){
        this.view = v;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(Objects.equals(command, "COMPUTE")){
            Polynomial polynomial1 = Polynomial.readPolynomial(view.getFirstNumberTextField().getText());
            Polynomial polynomial2 = Polynomial.readPolynomial(view.getSecondNumberTextField().getText());
            //String firstPolynomial = "2x^1";
            //String secondPolynomial = "2x^2";
           // polynomial1 = Polynomial.readPolynomial(firstPolynomial);
           // polynomial2 = Polynomial.readPolynomial(secondPolynomial);
            String operation = String.valueOf(view.getOperationsComboBox().getSelectedItem());
            Polynomial result = new Polynomial();
            switch(operation){
                case "Add": result = operations.addition(polynomial1, polynomial2);
                    break;
                case "Subtract": result = operations.subtraction(polynomial1, polynomial2);
                    break;
                case "Multiply": result = operations.multiplication(polynomial1, polynomial2);
                    break;
                case "Derivative": result = operations.derivative(polynomial1);
                    break;
                case "Integration": result = operations.integration(polynomial1);
                    break;
            }
            view.getResultValueLabel().setText(Polynomial.getStringFromPolynomial(result));
        }
    }
}
