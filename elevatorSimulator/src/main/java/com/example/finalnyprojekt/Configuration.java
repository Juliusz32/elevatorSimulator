package com.example.finalnyprojekt;

import javafx.animation.Animation;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    /*** screen ***/

    public int screenWidth;
    public int screenHeight;

    /*** floors ***/

    public int floorSize;
    public int floorSpace;
    public int minimumFloor;
    public int maximumFloor;
    public int numberOfFloors;
    public int floorsCordsXref = 650;
    public XY[] floorsCords;

    Rectangle[] floors;
    Label[] floorLabel;

    /*** Elevators ***/

    public int elevatorSize = 50;
    public XY[] elevatorCords;
    Rectangle[] elevators;
    List<Animation> eAnimations;
    public Configuration(int minimumFloor, int maximumFloor){
        this.minimumFloor = minimumFloor;
        this.maximumFloor = maximumFloor;

        numberOfFloors = maximumFloor - minimumFloor + 1;
        screenWidth = 1280;
        screenHeight = 720;

        floorSize = 200;
        floorSpace = 5;

        int free_space = screenHeight - numberOfFloors * (50 + floorSpace) + floorSpace;
        int yref = free_space/2;

        floorsCords = new XY[numberOfFloors];

        for(int i = 0; i < floorsCords.length; i++){
            floorsCords[i] = new XY();
            floorsCords[i].x = floorsCordsXref;
            floorsCords[i].y = yref + i *(50+floorSpace);
        }

        /*** elevators ***/

        elevatorCords = new XY[3];

        for(int i = 0; i < 3; i++){
            elevatorCords[i] = new XY();
            elevatorCords[i].x = floorsCordsXref  - 45 - 40*i;
            elevatorCords[i].y = floorsCords[(floorsCords.length+minimumFloor-1)].y;
        }

    }

    public void prepareAnimation(){
        /*** floors ***/

        floors = new Rectangle[numberOfFloors];
        floorLabel = new Label[numberOfFloors];
        for(int i = 0; i < floorsCords.length; i++){
            floors[i] = new Rectangle(floorsCords[i].x, floorsCords[i].y, floorSize, 50);
            floors[i].setFill(Color.LIGHTGRAY);
            floors[i].setStroke(Color.BLACK);
            floorLabel[i] = new Label(""+(maximumFloor-i));
            floorLabel[i].setLayoutX(floorsCords[i].x+(floorSize)+5);
            floorLabel[i].setLayoutY(floorsCords[i].y);
            floorLabel[i].setFont(new Font(30));
            HelloApplication.animationPane.getChildren().addAll(floors[i], floorLabel[i]);
        }

        /*** elevators ***/

        eAnimations = new ArrayList<Animation>();

        elevators = new Rectangle[3];
        for(int i = 0; i < 3; i++){
            elevators[i] = new Rectangle(elevatorCords[i].x, elevatorCords[i].y, elevatorSize-15, elevatorSize);
            elevators[i].setFill(Color.LIGHTGRAY);
            elevators[i].setStroke(Color.BLACK);
            HelloApplication.animationPane.getChildren().addAll(elevators[i]);
        }

    }


}
