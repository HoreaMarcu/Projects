package org.example.GUI;

import org.example.BusinessLogic.SimulationManager;
import org.example.Model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class SimulationFrame extends JFrame implements ActionListener {
    private JPanel contentPane;
    private JPanel inputPanel;
    private JLabel NLabel;
    private JLabel QLabel;
    private JLabel TMaxSimLabel;
    private JLabel TMinArrivalLabel;
    private JLabel TMaxArrivalLabel;
    private JLabel TMinServiceLabel;
    private JLabel TMaxServiceLabel;
    private JTextField NTextField;
    private JTextField QTextField;
    private JTextField TMinArrivalTextField;
    private JTextField TMaxArrivalTextField;
    private JTextField TMinServiceTextField;
    private JTextField TMaxServiceTextField;
    private JTextField TMaxSimTextField;
    private JButton RunButton;
    private JPanel logPanel;
    private JLabel logLabel;

    private JTextArea textArea;

    private File output = new File("outTest.txt");
    FileWriter write = new FileWriter(output);
    public SimulationFrame(String name) throws IOException {
        super(name);
        this.prepareGui();
    }
    public void prepareGui(){
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.contentPane = new JPanel(new GridLayout(2, 2));
        this.prepareInputPanel();
        this.prepareLogPanel();
        this.setContentPane(this.contentPane);
    }
    private void prepareLogPanel() {
        this.logPanel = new JPanel();
        this.logPanel.setLayout(new GridLayout(1,1));
        this.logLabel = new JLabel("Log", JLabel.CENTER);
        this.textArea = new JTextArea(5,10);
        this.logPanel.add(this.logLabel);
        this.logPanel.add(textArea);
        this.contentPane.add(this.logPanel);
    }

    private void prepareInputPanel() {
        this.inputPanel = new JPanel();
        this.inputPanel.setLayout(new GridLayout(8, 3));
        this.NLabel = new JLabel("Number of clients", JLabel.CENTER);
        this.inputPanel.add(this.NLabel);
        this.NTextField = new JTextField();
        this.inputPanel.add(this.NTextField);
        this.QLabel = new JLabel("Number of queues", JLabel.CENTER);
        this.inputPanel.add(QLabel);
        this.QTextField = new JTextField();
        this.inputPanel.add(QTextField);
        this.TMinArrivalLabel = new JLabel("Min arrival time", JLabel.CENTER);
        this.inputPanel.add(TMinArrivalLabel);
        this.TMinArrivalTextField = new JTextField();
        this.inputPanel.add(TMinArrivalTextField);
        this.TMaxArrivalLabel = new JLabel("Max arrival time", JLabel.CENTER);
        this.inputPanel.add(TMaxArrivalLabel);
        this.TMaxArrivalTextField = new JTextField();
        this.inputPanel.add(TMaxArrivalTextField);
        this.TMinServiceLabel = new JLabel("Min service time", JLabel.CENTER);
        this.inputPanel.add(TMinServiceLabel);
        this.TMinServiceTextField = new JTextField();
        this.inputPanel.add(TMinServiceTextField);
        this.TMaxServiceLabel = new JLabel("Max service time", JLabel.CENTER);
        this.inputPanel.add(TMaxServiceLabel);
        this.TMaxServiceTextField = new JTextField();
        this.inputPanel.add(TMaxServiceTextField);
        this.TMaxSimLabel = new JLabel("Max simulation time", JLabel.CENTER);
        this.inputPanel.add(this.TMaxSimLabel);
        this.TMaxSimTextField = new JTextField();
        this.inputPanel.add(TMaxSimTextField);
        this.RunButton = new JButton("Run");
        this.RunButton.setActionCommand("RUN");
        this.RunButton.addActionListener(this);
        this.inputPanel.add(this.RunButton);
        this.contentPane.add(this.inputPanel);
    }
    public boolean pressedRun = false;
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(Objects.equals(command, "RUN")){
            SimulationManager.timeLimit = Integer.parseInt(TMaxSimTextField.getText());
            SimulationManager.numberOfClients = Integer.parseInt(NTextField.getText());
            SimulationManager.maxArrivalTime = Integer.parseInt(TMaxArrivalTextField.getText());
            SimulationManager.minArrivalTime = Integer.parseInt(TMinArrivalTextField.getText());
            SimulationManager.maxProcessingTime = Integer.parseInt(TMaxServiceTextField.getText());
            SimulationManager.minProcessingTime = Integer.parseInt(TMinServiceTextField.getText());
            SimulationManager.numberOfServers = Integer.parseInt(QTextField.getText());
            SimulationManager.setSchedulerValues();
            pressedRun = true;
        }
    }
    public static String simulationResults(){
        String results;
        double avgServiceTime = 0D;
        for (Task generatedTask : SimulationManager.auxGeneratedTasks) {
            avgServiceTime += generatedTask.getServiceTime();
        }
        avgServiceTime /= SimulationManager.auxGeneratedTasks.size();
        SimulationManager.avgWaitingTime /= SimulationManager.auxGeneratedTasks.size();
        results = "Average service time was: " + avgServiceTime;
        results += "\n";
        results += "Peak second was: " + SimulationManager.peakHour;
        results += "\n";
        results += "Average waiting time was: " + SimulationManager.avgWaitingTime;
        results += "\n";
        return results;
    }
    public void updateUserInterface(int time) throws IOException {
        StringBuilder display = new StringBuilder();
        display.append("Time ").append(time);
        display.append("\n");
        display.append("Waiting clients: ");
        for(int i = 0; i < SimulationManager.getGeneratedTasks().size();i++){
            display.append("(");
            display.append(SimulationManager.getGeneratedTasks().get(i).getId());
            display.append(" ");
            display.append(SimulationManager.getGeneratedTasks().get(i).getArrivalTime());
            display.append(" ");
            display.append(SimulationManager.getGeneratedTasks().get(i).getServiceTime());
            display.append(");");
        }
        display.append("\n");
        for(int i = 0; i < SimulationManager.getScheduler().getServers().size(); i++){
            int j = i + 1;
            display.append("Queue ").append(j).append(": ");
            for(Task t:SimulationManager.getScheduler().getServers().get(i).getTasks() ){
                display.append("(");
                display.append(t.getId());
                display.append(" ");
                display.append(t.getArrivalTime());
                display.append(" ");
                display.append(t.getServiceTime());
                display.append(");");
            }
            display.append("\n");
        }
        if(pressedRun) {
            textArea.setText(display.toString());
            write.write(display.toString());
        }
        if(time == SimulationManager.timeLimit - 1){
            String finalStatistics = simulationResults();
            write.write(finalStatistics);
            write.close();
            textArea.setText(finalStatistics);
        }
    }
}
