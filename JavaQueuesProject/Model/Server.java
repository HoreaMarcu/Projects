package org.example.Model;

import org.example.BusinessLogic.SimulationManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    private final ArrayBlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    public Server(){
        tasks = new ArrayBlockingQueue<Task>(100);
        waitingPeriod = new AtomicInteger();
    }
    public void addTask(Task newTask){
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }
    public void run() {
        while(true){
            if(tasks.size() !=0) {
                Task currentTask = tasks.peek();
                try {
                    Thread.sleep(1000L * currentTask.getServiceTime());
                } catch (InterruptedException e) {
                    System.out.println("exception throw by sleep method");
                }
                waitingPeriod.addAndGet(-currentTask.getServiceTime());
                currentTask.setWaitingTime(SimulationManager.currentTime - currentTask.getWaitingTime() - currentTask.getServiceTime());
                SimulationManager.avgWaitingTime += currentTask.getWaitingTime();
                tasks.remove();
            }
        }
    }
    public Task[] getTasks(){
        Task[] currentTasks = new Task[tasks.size()];
        return tasks.toArray(currentTasks);
    }
}
