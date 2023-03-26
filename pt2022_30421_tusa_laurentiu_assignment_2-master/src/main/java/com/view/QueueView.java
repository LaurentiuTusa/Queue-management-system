package com.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class QueueView extends JFrame{
    private JTextField customerTextField;
    private JTextField queuesTextField;
    private JTextField simulatonTextField;
    private JTextField minArrival;
    private JTextField maxArrival;
    private JTextField minService;
    private JTextField maxService;
    private JButton simulateButton;
    private JTextArea textArea1;
    private JPanel queue_panel;
    private JLabel validateLabel;

    public QueueView(){
        this.setVisible(true);
        this.setContentPane(queue_panel);
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getCustomerTextField() {
        return customerTextField.getText();
    }

    public String getQueuesTextField() {
        return queuesTextField.getText();
    }

    public String getSimulatonTextField() {
        return simulatonTextField.getText();
    }

    public String getMinArrival() {
        return minArrival.getText();
    }

    public String getMaxArrival() {
        return maxArrival.getText();
    }

    public String getMinService() {
        return minService.getText();
    }

    public String getMaxService() {
        return maxService.getText();
    }

    public void setValidateLabel(String msg){
        validateLabel.setText(msg);
    }

    public void setTextArea1(String text) {
        textArea1.append(text);
    }

    public void simulate(ActionListener action) {
        simulateButton.addActionListener(action);
    }

    public String getTextArea1() {
        return textArea1.getText();
    }
}
