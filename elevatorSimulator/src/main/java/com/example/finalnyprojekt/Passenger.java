package com.example.finalnyprojekt;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Passenger extends Thread{
    public int currentFloor;
    public int destinationFloor;
    public int nextFloor;
    Random random = new Random();

    /*** Circles ***/


    /*** Elevators ***/

    Elevator firstType;
    Elevator secondType;
    Elevator thirdType;
    Elevator currentElevator;
    public int currentElevatorID;

    /*** Semaphores ***/
    Semaphore passengerSemaphore = new Semaphore(0,true);

    /*** for animation ***/
    public XY passengerCords;
    Configuration configuration;
    public Circle passenger;


    public Passenger(String name){
        super(name);
        this.currentFloor = random.nextInt(Simulation.N - Simulation.U) + Simulation.U;
        this.firstType = Simulation.firstType;
        this.secondType = Simulation.secondType;
        this.thirdType = Simulation.thirdType;

        /*** annimation ***/
        this.configuration = HelloApplication.config;

        passengerCords = new XY();
        passengerCords.x = configuration.floorsCords[(Simulation.N-currentFloor)].x+30;
        passengerCords.y = configuration.floorsCords[(Simulation.N-currentFloor)].y+25;
        passenger = new Circle(passengerCords.x,passengerCords.y,15);
        passenger.setFill(Color.ALICEBLUE);
        passenger.setStroke(Color.BLACK);
        Platform.runLater(() -> {
            HelloApplication.animationPane.getChildren().add(passenger);
        });

    }



    public void run(){
        while(true){
        /*
            try {
                sleep(500);
            } catch (InterruptedException e) {

            }
        */
            do{
                destinationFloor = random.nextInt(Simulation.N+1 - Simulation.U) + Simulation.U;
            }while(destinationFloor == currentFloor);

            System.out.println(getName()+" "+currentFloor+" -> "+destinationFloor);


            /*** choosing type of first elevator ***/

            if(firstType.canUseThisElevator(currentFloor) == 1){
                currentElevator = firstType;
                currentElevatorID = 0;
            }else if(secondType.canUseThisElevator(currentFloor) == 1){
                currentElevator = secondType;
                currentElevatorID = 1;
            }else if(thirdType.canUseThisElevator(currentFloor) == 1){
                currentElevator = thirdType;
                currentElevatorID = 2;
            }

            System.out.println(getName()+"["+currentElevator.getName()+"]");

            try {
                //calling elevator
                callElevator(Simulation.N, currentFloor, currentElevatorID, currentElevator);

                //choosing nextFloor
                if(currentElevator.minimumFloor <= destinationFloor && destinationFloor <= currentElevator.maximumFloor || destinationFloor == 0){
                    nextFloor = destinationFloor;
                }else{
                    nextFloor = 0;
                }

                //using elevator
                elevatorTravel(Simulation.N, nextFloor, currentElevatorID, currentElevator);

            } catch (InterruptedException e) {

            }

            /*** if necessary choosing second elevator ***/

            if(destinationFloor != currentFloor){
                if(firstType.canUseThisElevator(destinationFloor) == 1){
                    currentElevator = firstType;
                    currentElevatorID = 0;
                }else if(secondType.canUseThisElevator(destinationFloor) == 1){
                    currentElevator = secondType;
                    currentElevatorID = 1;
                }else if(thirdType.canUseThisElevator(destinationFloor) == 1){
                    currentElevator = thirdType;
                    currentElevatorID = 2;
                }

                System.out.println(getName()+"["+currentElevator.getName()+"]");

                try {
                    //calling elevator
                    callElevator(Simulation.N, currentFloor, currentElevatorID, currentElevator);

                    //choosing nextFloor
                    nextFloor = destinationFloor;

                    //using elevator
                    elevatorTravel(Simulation.N, nextFloor, currentElevatorID, currentElevator);


                } catch (InterruptedException e) {

                }


            }

        }

    }

    public void callElevator(int maximumFloor, int destinationFloor, int id, Elevator elevator) throws InterruptedException {

        elevator.semaphoreElevator.acquire();
        elevator.goToFloor(maximumFloor, destinationFloor, id, this);

        //waiting for elevator
        passengerSemaphore.acquire();

        //getting to elevator
        getToElevator(id);
        System.out.println(getName()+": IN("+currentElevator.getName()+")");

    }

    public void elevatorTravel(int maximumFloor, int destinationFloor, int id, Elevator elevator) throws InterruptedException{
        elevator.goToFloorWithPassenger(maximumFloor,destinationFloor, id, this);
        getOutOfElevator(id);
        System.out.println(getName()+": OUT("+elevator.getName()+")");
        elevator.semaphoreElevator.release();
    }

    public void getToElevator(int elevatorID) throws InterruptedException {
        /*** animation ***/
        MoveTo moveTo = new MoveTo();
        moveTo.setX(passengerCords.x);
        moveTo.setY(passengerCords.y);

        LineTo lineTo = new LineTo();
        lineTo.setX(configuration.elevatorCords[elevatorID].x + 17);
        lineTo.setY(passengerCords.y);

        Path path = new Path();
        path.getElements().addAll(moveTo, lineTo);

        PathTransition pathTransition = new PathTransition(Duration.millis(HelloController.passengerAnimationSpeed), path, passenger);

        pathTransition.setOnFinished(e -> {
            unblock();
        });

        Platform.runLater(() -> {
            pathTransition.setRate(1);
            configuration.eAnimations.add(pathTransition);
            pathTransition.play();
        });


        block();

        passengerCords.x = configuration.elevatorCords[elevatorID].x + 17;

    }

    public void getOutOfElevator(int elevatorID) throws InterruptedException{
        /*** animation ***/

        MoveTo moveTo = new MoveTo();
        moveTo.setX(passengerCords.x);
        moveTo.setY(passengerCords.y);

        LineTo lineTo = new LineTo();
        lineTo.setX(configuration.floorsCords[(Simulation.N-currentFloor)].x+30);
        lineTo.setY(passengerCords.y);

        Path path = new Path();
        path.getElements().addAll(moveTo, lineTo);

        PathTransition pathTransition = new PathTransition(Duration.millis(HelloController.passengerAnimationSpeed), path, passenger);

        pathTransition.setOnFinished(e -> {
            unblock();
        });

        Platform.runLater(() -> {
            pathTransition.setRate(1);
            configuration.eAnimations.add(pathTransition);
            pathTransition.play();
        });


        block();

        passengerCords.x = configuration.floorsCords[(Simulation.N-currentFloor)].x+30;

        Platform.runLater(() -> {
            //HelloApplication.animationPane.getChildren().remove(passenger);
        });
    }







    public synchronized void block() throws InterruptedException {
        wait();
    }

    public synchronized void unblock() {
        notify();
    }
}
