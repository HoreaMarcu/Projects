package org.example.Model;

public class Task implements Comparable<Task>{
    private int arrivalTime;
    private int serviceTime;
    private int id;
    private int waitingTime;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getId() {
        return id;
    }
    private static int nextId = 0;
    public Task(int arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.id = nextId;
        nextId ++;
    }

    public Task() {
    }


    public int getArrivalTime() {
        return arrivalTime;
    }


    public int getServiceTime() {
        return serviceTime;
    }


    @Override
    public int compareTo(Task o) {
        if(this.getArrivalTime() > o.getArrivalTime()) return 1;
        if(this.getArrivalTime() < o.getArrivalTime()) return -1;
        return 0;
    }
}
