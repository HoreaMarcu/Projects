package org.example;

import org.example.GraphicalUserInterface.View;

import javax.swing.*;
import javax.*;

public class App
{
    public static void main( String[] args )
    {

        JFrame frame = new View("Simple calculator for polynomials using MVC");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }
}
