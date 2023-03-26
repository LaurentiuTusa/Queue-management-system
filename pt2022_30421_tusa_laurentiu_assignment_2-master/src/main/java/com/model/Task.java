package com.model;

public class Task implements Comparable<Task>{
    private int ID;
    private int serviceTime;
    private int arrivalTime;

    public Task(int ID, int serviceTime, int arrivalTime) {
        this.ID = ID;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int compareTo(Task t) {
        return getArrivalTime() - t.getArrivalTime();
    }
}

