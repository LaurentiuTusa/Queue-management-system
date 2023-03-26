package com;

import com.model.Server;
import com.model.Task;
import com.presenter.SimulationManager;
import com.view.QueueView;

import java.util.ArrayList;

public class Main{

    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager(new QueueView());
    }
}
