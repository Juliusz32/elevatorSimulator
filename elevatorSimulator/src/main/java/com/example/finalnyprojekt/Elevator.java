package com.example.finalnyprojekt;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.concurrent.Semaphore;

public class Elevator extends Thread{

    public int minimumFloor;
    public int maximumFloor;
    public int currentFloor;
    public int elevatorCall;
    public int elevatorMove;

    Semaphore semaphoreElevator = new Semaphore(1, true);
    Semaphore elevatorWaitSemaphore = new Semaphore(0, true);
    Configuration configuration;


    public Elevator(String name, int minimumFloor, int maximumFloor){
        super(name);
        this.minimumFloor = minimumFloor;
        this.maximumFloor = maximumFloor;
        this.currentFloor = 0;
        this.elevatorCall = 0;
        this.elevatorMove = 0;
        this.configuration = HelloApplication.config;
    }

    public void run(){
        while(true){
            try {
                //waiting for a call

                elevatorWaitSemaphore.acquire();

                //travel to destinationFloor

                elevatorWaitSemaphore.acquire();

                //elevator called



            } catch (InterruptedException e) {

            }
        }
    }

    public int canUseThisElevator(int Floor){
        if((minimumFloor <= Floor && Floor <= maximumFloor ) || (Floor == 0))
            return 1;
        else
            return 0;
    }



    public void goToFloor(int maximumFloor, int destinationFloor, int id, Passenger passenger) throws InterruptedException {

        System.out.println(getName() + "(" + currentFloor + " - " + destinationFloor+ ")");

        /*** beginning of animation ***/

        MoveTo moveTo = new MoveTo();
        moveTo.setX(configuration.elevatorCords[id].x+17);
        moveTo.setY(configuration.elevatorCords[id].y+25);

        LineTo lineTo = new LineTo();
        lineTo.setX(configuration.elevatorCords[id].x+17);
        lineTo.setY(configuration.floorsCords[(maximumFloor-destinationFloor)].y+25);

        Path path = new Path();
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(HelloController.elevatorAnimationSpeed), path, configuration.elevators[id]);

        pathTransition.setOnFinished(e -> {
            unblock();
        });

        Platform.runLater(() -> {
            pathTransition.setRate(1);
            configuration.eAnimations.add(pathTransition);
            pathTransition.play();
        });

        block();

        configuration.elevatorCords[id].y = configuration.floorsCords[(maximumFloor-destinationFloor)].y;

        /*** end of animation ***/

        System.out.println(getName() + "(" + currentFloor + " - " + destinationFloor+ ")");

        this.currentFloor = destinationFloor;




        passenger.passengerSemaphore.release();
        elevatorWaitSemaphore.release();


    }

    public void goToFloorWithPassenger(int maximumFloor, int destinationFloor, int id, Passenger passenger) throws InterruptedException{

        System.out.println(getName() + "(" + currentFloor + " - " + destinationFloor+ ")");

        /**** BEGINING OF ANIMATION ****/

        /*** elevator animation ***/

        MoveTo moveTo = new MoveTo();
        moveTo.setX(configuration.elevatorCords[id].x+17);
        moveTo.setY(configuration.elevatorCords[id].y+25);

        LineTo lineTo = new LineTo();
        lineTo.setX(configuration.elevatorCords[id].x+17);
        lineTo.setY(configuration.floorsCords[(maximumFloor-destinationFloor)].y+25);

        Path path = new Path();
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(HelloController.elevatorAnimationSpeed), path, configuration.elevators[id]);

        /*** passenger animation ***/

        MoveTo moveTo1 = new MoveTo();
        moveTo1.setX(passenger.passengerCords.x);
        moveTo1.setY(passenger.passengerCords.y);

        LineTo lineTo1 = new LineTo();
        lineTo1.setX(passenger.passengerCords.x);
        lineTo1.setY(configuration.floorsCords[(maximumFloor-destinationFloor)].y+25);

        Path path1 = new Path();
        path1.getElements().addAll(moveTo1, lineTo1);
        PathTransition pathTransition1 = new PathTransition(Duration.millis(HelloController.elevatorAnimationSpeed), path1, passenger.passenger);

        pathTransition.setOnFinished(e -> {
            unblock();
        });

        Platform.runLater(() -> {
            pathTransition.setRate(1);
            pathTransition1.setRate(1);
            configuration.eAnimations.add(pathTransition);
            configuration.eAnimations.add(pathTransition1);
            pathTransition.play();
            pathTransition1.play();
        });

        block();

        configuration.elevatorCords[id].y = configuration.floorsCords[(maximumFloor-destinationFloor)].y;
        passenger.passengerCords.y = configuration.floorsCords[(maximumFloor-destinationFloor)].y+25;

        /**** END OF ANIMATION ****/


        passenger.currentFloor = destinationFloor;
        this.currentFloor = destinationFloor;
    }

    public void elevatorCall(Passenger passenger){
        passenger.passengerSemaphore.release();
    }




    public synchronized void block() throws InterruptedException {
        wait();
    }

    public synchronized void unblock() {
        notify();
    }
}