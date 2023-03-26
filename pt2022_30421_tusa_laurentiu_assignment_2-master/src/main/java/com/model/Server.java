package com.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Server implements Runnable{

    private BlockingQueue<Task> tasks;
    private int waitingPeriod;
    private int srvTime;
    private Boolean active;

    public Server() {
        this.tasks = new LinkedBlockingDeque<>();
        this.waitingPeriod = 0;
        this.srvTime = 0;
        this.active = true;
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    public int getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setActive(Boolean value) {
        this.active = value;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public int getSrvTime() {
        return srvTime;
    }

    @Override
    public void run() {
        while (active){
            try {
                if(tasks.size()>0) { //clients exist in queue
                    Task t = tasks.peek();
                    while(t.getServiceTime() > 0) {//client still in the head of the queue
                        srvTime += 1;
                        for (Task h:tasks) {
                            if (h != t){
                                waitingPeriod += 1;
                            }
                        }
                        Thread.sleep(1000);
                        t.setServiceTime(t.getServiceTime() - 1);
                    }
                    tasks.remove();
                }
            } catch (InterruptedException e) {
                System.out.println("Problems at Thread in Server");
            }
        }
    }
}

