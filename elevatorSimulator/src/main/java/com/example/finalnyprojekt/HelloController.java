package com.example.finalnyprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button applyButton;
    @FXML
    private TextField maxFText;
    @FXML
    private TextField minFText;
    @FXML
    private TextField numOfPText;
    @FXML
    private TextField elCapText;
    @FXML
    private TextField elevatorAnimationSpeedText;
    @FXML
    private TextField passengerAnimationSpeedText;
    @FXML
    protected void onStartButtonClick(){
        Simulation.startThreads();
        saveButton.setDisable(true);
        startButton.setDisable(true);
    }
    //@FXML


    public static int maxF = 10;    //maximum Floor
    public static int minF = -2;    //minimum Floor
    public static int numOfP = 1;   //number of Passengers
    public static int elCap = 1;    //Capacity of Elevator
    public static int elevatorAnimationSpeed = 3000;
    public static int passengerAnimationSpeed = 500;

    @FXML
    protected void onStopButtonClick(){
        Simulation.interruptThread();
    }
    @FXML
    protected void onSaveButtonClick(){

        try{
            maxF = Integer.parseInt(maxFText.getText());
        }catch(NumberFormatException e){
            maxFText.setText("Error");
        }
        try{
            minF = Integer.parseInt(minFText.getText());
        }catch(NumberFormatException e){
            minFText.setText("Error");
        }
        try{
            numOfP = Integer.parseInt(numOfPText.getText());
        }catch(NumberFormatException e){
            numOfPText.setText("Error");
        }
        try{
            elCap = Integer.parseInt(elCapText.getText());
        }catch(NumberFormatException e){
            elCapText.setText("Error");
        }

        HelloApplication.config = new Configuration(minF, maxF);
        HelloApplication.config.prepareAnimation();
        startButton.setDisable(false);
        stopButton.setDisable(false);
    }

    @FXML
    protected void onApplyButtonClick(){
        try{
            elevatorAnimationSpeed = Integer.parseInt(elevatorAnimationSpeedText.getText());
        }catch(NumberFormatException e){
            elevatorAnimationSpeedText.setText("Error");
        }

        try{
            passengerAnimationSpeed = Integer.parseInt(passengerAnimationSpeedText.getText());
        }catch(NumberFormatException e){
            passengerAnimationSpeedText.setText("Error");
        }

    }
}

