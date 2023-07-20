package com.example.finalnyprojekt;

public class Simulation {
    public static int N = HelloController.maxF;  //maximum floor
    public static int M = HelloController.elCap; //elevator capacity
    public static int U = HelloController.minF; //minimum floor
    public static int numberOfPassengers = HelloController.numOfP;

    public static Elevator firstType;
    public static Elevator secondType;
    public static Elevator thirdType;

    public static Passenger[] passenger = new Passenger[numberOfPassengers];




    public static void startThreads() {

        System.out.println("E1 [0, "+N/2+"]");
        System.out.println("E2 ["+(N/2+1) +", "+N+"] i 0");
        System.out.println("E3 ["+U+", 0]");


        firstType = new Elevator("E1",0,N/2);
        secondType = new Elevator("E2", N/2+1, N);
        thirdType = new Elevator("E3",U ,0);

        firstType.start();
        secondType.start();
        thirdType.start();


        for(int i = 0; i < numberOfPassengers; i++){
            passenger[i] = new Passenger("P"+i);
        }

        for(int i = 0; i < numberOfPassengers; i++){
            passenger[i].start();
        }


    }

    public static void interruptThread(){
        firstType.interrupt();
        secondType.interrupt();
        thirdType.interrupt();
        for(int i = 0; i < numberOfPassengers; i++){
            passenger[i].interrupt();
        }
    }

}
