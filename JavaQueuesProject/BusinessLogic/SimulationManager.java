package org.example.BusinessLogic;

import org.example.GUI.SimulationFrame;
import org.example.Model.Task;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimulationManager implements Runnable {
    public static int timeLimit = 100;
    public static int maxProcessingTime = 4;
    public static int minProcessingTime = 2;
    public static int maxArrivalTime = 10;
    public static int minArrivalTime = 2;
    public static int numberOfServers = 3;
    public static int numberOfClients = 10;
    public static SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    public static int currentTime = -1;
    public static int peakHour = 0;
    public static double avgWaitingTime = 0D;
    private static Scheduler scheduler;
    private final SimulationFrame frame;
    private static final ArrayList<Task> generatedTasks = new ArrayList<>();
    public static final ArrayList<Task> auxGeneratedTasks = new ArrayList<>();
    public static ArrayList<Task> getGeneratedTasks() {
        return generatedTasks;
    }

    public SimulationManager() throws IOException {
        frame = new SimulationFrame("QUEUES MANAGEMENT APPLICATION");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //generateNRandomTasks();
    }

    public static void setSchedulerValues(){
        scheduler = new Scheduler(numberOfServers,1000);
        scheduler.changeStrategy(selectionPolicy);
        generateNRandomTasks();
    }

    private static void generateNRandomTasks(){
        Random r = new Random();
        int processingTime = 0;
        int arrivalTime = 0;
        for(int i =0 ;i < numberOfClients; i++){
            processingTime = r.nextInt(minProcessingTime,maxProcessingTime);
            arrivalTime = r.nextInt(minArrivalTime,maxArrivalTime);
            Task newTask = new Task(arrivalTime,processingTime);
            generatedTasks.add(newTask);
            auxGeneratedTasks.add(newTask);
        }
        Collections.sort(generatedTasks);
        Collections.sort(auxGeneratedTasks);
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void run() {
        int maxNumberOfClients = 0;
        do {
            currentTime ++;
            System.out.print("");
        }while(!frame.pressedRun);
        currentTime = 0;
        while (currentTime < timeLimit) {
            int currentNumberOfClients = 0;
            for (int i = 0; i < generatedTasks.size(); i++) {
                if (generatedTasks.get(i).getArrivalTime() == currentTime) {
                    generatedTasks.get(i).setWaitingTime(currentTime);
                    scheduler.dispatchTask(generatedTasks.get(i));
                    generatedTasks.remove(generatedTasks.get(i));
                    i--;
                    currentNumberOfClients ++;
                }
            }
            if(currentNumberOfClients > maxNumberOfClients){
                maxNumberOfClients = currentNumberOfClients;
                peakHour = currentTime;
            }
            try {
                frame.updateUserInterface(currentTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error waiting for 1 second");
            }
        }
    }


}

