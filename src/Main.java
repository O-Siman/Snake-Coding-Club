import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main extends PApplet {
    //   Vars
    //Board
    static int boardWidth = 20;
    static int boardHeight = 20;
    //Player (snake head)
    static int pX = 10;
    static int pY = 10;
    static int pdX = 0;
    static int pdY = 0;
    static int startingPositionX = boardWidth / 2;
    static int startingPositionY = boardHeight / 2;
    //Tail
    static ArrayList<Integer> tailX = new ArrayList<>(Arrays.asList(startingPositionX, startingPositionX, startingPositionX, startingPositionX));
    static ArrayList<Integer> tailY = new ArrayList<>(Arrays.asList(startingPositionY, startingPositionY, startingPositionY, startingPositionY));
    //Apple
    static int aXStartingPosition = (startingPositionX + boardWidth) / 2;
    static int aYStartingPosition = startingPositionY;
    static int aX = aXStartingPosition;
    static int aY = startingPositionY;

    //Controls
    static ArrayList<Integer> controlQueue = new ArrayList<>();

    public void setup() {
        frameRate(10);
    }

    public void settings() {
        size(boardWidth * 10, boardHeight * 10);
    }


    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case UP: {
                controlQueue.add(UP);
                break;
            }
            case DOWN: {
                controlQueue.add(DOWN);
                break;
            }
            case LEFT: {
                controlQueue.add(LEFT);
                break;
            }
            case RIGHT: {
                controlQueue.add(RIGHT);
                break;
            }
        }

    }

    public static void gameloop() {
        //If we have no controls in queue
        if (!(controlQueue.size() < 1)) {
            //Queue controls
            switch (controlQueue.get(0)) {
                case UP: {
                    if (pdY == 1)
                        return;
                    pdY = -1;
                    pdX = 0;
                    controlQueue.remove(0);
                    break;
                }
                case DOWN: {
                    if (pdY == -1)
                        return;
                    pdY = 1;
                    pdX = 0;
                    controlQueue.remove(0);
                    break;
                }
                case LEFT: {
                    if (pdX == 1)
                        return;
                    pdX = -1;
                    pdY = 0;
                    controlQueue.remove(0);
                    break;
                }
                case RIGHT: {
                    if (pdX == -1)
                        return;
                    pdX = 1;
                    pdY = 0;
                    controlQueue.remove(0);
                    break;
                }
            }
        }



        //Tail pieces
        tailX.remove(tailX.size() - 1);
        tailY.remove(tailY.size() - 1);

        tailX.add(0, pX);
        tailY.add(0, pY);

        //Move the player
        pX += pdX;
        pY += pdY;

        //Loop around the board
        if (pX >= boardWidth) {
            pX = 0;
        } else if (pX < 0) {
            pX = boardWidth - 1;
        }

        if (pY >= boardHeight) {
            pY = 0;
        } else if (pY < 0) {
            pY = boardHeight - 1;
        }


        //Apple collision
        //If player is on same space as apple
        if (pX == aX && pY == aY) {
            tailX.add(tailX.get(tailX.size() - 1));
            tailY.add(tailY.get(tailY.size() - 1));

            //Random var
            Random r = new Random();
            whileLabel:
            while (true) {
                aX = r.nextInt(boardWidth);
                aY = r.nextInt(boardHeight);

                //If apple spawns on current player
                if (aX == pX && aY == pY)
                    continue;

                //Loop through all tail pieces
                for (int i = 0; i < tailX.size(); i++) {
                    //If apple spawns inside of the current tail piece
                    if (aX == tailX.get(i) && aY == tailY.get(i))
                        continue whileLabel;
                }
                break;
            }

        }

        //Tail collision
        for (int i = 0; i < tailX.size(); i++) {
            if (pX == tailX.get(i) && pY == tailY.get(i)) {
                //If game has not started yet
                if (pdX == 0 && pdY == 0)
                    continue;

                //Send us back to center
                pX = startingPositionX;
                pY = startingPositionY;

                //Reset player tail size to 5
                tailX = new ArrayList<>(Arrays.asList(pX, pX, pX, pX));
                tailY = new ArrayList<>(Arrays.asList(pY, pY, pY, pY));

                pdX = 0;
                pdY = 0;

                aX = aXStartingPosition;
                aY = aYStartingPosition;
            }
        }


    }

    public void draw() {
        background(0);

        //Player head
        fill(255, 255, 255);
        rect(pX * 10, pY * 10, 10, 10);

        //Snake's tail
        for (int i = 0; i < tailX.size(); i++) {
            rect(tailX.get(i) * 10, tailY.get(i) * 10, 10, 10);
        }

        //Apple
        fill(255, 0, 0);
        rect(aX * 10, aY * 10, 10, 10);
        gameloop();
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}
