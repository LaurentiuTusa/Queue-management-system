package com.presenter;

import com.model.Server;
import com.model.Task;
import com.view.QueueView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationManager implements Runnable{
    private final QueueView queueView;
    private List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Server> Queues = new ArrayList<>();
    private int N;
    private int Q;
    private int simulationTime;
    private int arrivalMin;
    private int arrivalMax;
    private int serviceMin;
    private int serviceMax;

    public SimulationManager(QueueView queueView) {
        this.queueView = queueView;

        queueView.simulate(new simulateAction());

    }

    private class simulateAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            generatedTasks.clear();
            validateInput();
            generateNTasks();
            generateQQueues();
            Collections.sort(generatedTasks);

            for (Server d:Queues) {
                Thread r = new Thread(d);
                r.start();
            }
            startThread();
        }
    }

    public void validateInput(){
       try{
           N = Integer.parseInt(queueView.getCustomerTextField());
           Q = Integer.parseInt(queueView.getQueuesTextField());
           if(Q == 0)
               throw new NumberFormatException();
           simulationTime = Integer.parseInt(queueView.getSimulatonTextField());
           arrivalMax = Integer.parseInt(queueView.getMaxArrival());
           arrivalMin = Integer.parseInt(queueView.getMinArrival());
           serviceMax = Integer.parseInt(queueView.getMaxService());
           serviceMin = Integer.parseInt(queueView.getMinService());
           queueView.setValidateLabel("Input is correct!");
       } catch (NumberFormatException e) {
           queueView.setValidateLabel("Wrong input!");
           throw e;
       }
    }

    public void generateNTasks(){
        int id = 1;//increase this up to N
        int arrivalTime;
        int serviceTime;
        for (id = 1; id<=N; id++){
            arrivalTime = ThreadLocalRandom.current().nextInt(arrivalMin, arrivalMax);
            serviceTime = ThreadLocalRandom.current().nextInt(serviceMin, serviceMax);

            generatedTasks.add(new Task(id, serviceTime , arrivalTime));
        }
    }

    public void generateQQueues(){
        for (int i = 1; i <= Q; i++){
            //create queues
            Queues.add(new Server());
        }
    }

    public void startThread() {
        Thread t = new Thread(this);
        t.start();
    }

    public void constructResult(int time){
        queueView.setTextArea1("Time: " + time +"\nWaiting clients: ");
        for (Task a:generatedTasks) {
            queueView.setTextArea1("(" + a.getID() + "," + a.getArrivalTime() + "," + a.getServiceTime() + "); ");
        }
        queueView.setTextArea1("\n");
        int z= 1;
        for (Server s:Queues) {
            if(s.getTasks().size() > 0){
                queueView.setTextArea1("Queue " + z + ": ");
                for (Task w:s.getTasks()) {
                    queueView.setTextArea1("(" + w.getID() + "," + w.getArrivalTime() + "," + w.getServiceTime() + "); ");
                }
                queueView.setTextArea1("\n");
                z++;
            }
            else {
                queueView.setTextArea1("Queue " + z + ": closed\n");
                z++;
            }
        }
    }

    public void finalResults(int peakTime){
        float totalWaitingTime = 0;
        float totalServiceTime = 0;
        for (Server s:Queues) {
            totalServiceTime += s.getSrvTime();
            totalWaitingTime += s.getWaitingPeriod();
        }
        totalServiceTime = totalServiceTime/N;
        totalWaitingTime = totalWaitingTime/N;
        queueView.setTextArea1("Average service time: " + totalServiceTime + "\nAverage waiting time: " + totalWaitingTime + "\nPeak time: " + peakTime);
    }

    @Override
    public void run() {
        int time = 0, peakTime =-1, count = -1, ok = 0;
        while(time < simulationTime && ok == 0){
            try{
                List<Task> aux = Collections.synchronizedList(new ArrayList<>());
                for (Task t:generatedTasks) {
                    if (t.getArrivalTime() <= time)//client arrived and is ready to get served
                    {//choose the queue with the least members
                        int v = Queues.size(), index = 0, ref = 999;
                        for (int i = 0; i<v; i++){
                            if(Queues.get(i).getTasks().size() < ref){
                                ref = Queues.get(i).getTasks().size();
                                index = i;
                            }
                        }
                        Queues.get(index).addTask(t);
                        aux.add(t);
                    }
                }
                for (Task y:aux) {//remove ready tasks from "generatedTasks" list
                    generatedTasks.remove(y);
                }
                int c = -1;
                for (Server f:Queues) {
                    c += f.getTasks().size();
                }
                if (c > count) {
                    count = c;
                    peakTime = time;
                }
                constructResult(time);//update UI with results
                time++;
                if (generatedTasks.isEmpty())//reduntant Thread check
                {
                    int control = 0;
                    for (Server s:Queues) {
                        if(!(s.getTasks().isEmpty()))
                            control++;
                    }
                    if (control == 0)
                        ok = 5;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Main Thread has caught an exception!");
            }
        }//end while - print final results and stop the other threads
        finalResults(peakTime);
        for (Server p:Queues) {
            p.setActive(false);
            System.out.println("\nServer thread has been closed");
        }
        //outputFile();
    }//end run

    public void outputFile(){
        try {
            FileWriter file = new FileWriter("newOutput.txt");
            file.write(queueView.getTextArea1());
            file.close();
        } catch (IOException e) {
            System.out.println("Error at outputFile");
            e.printStackTrace();
        }
    }

}
