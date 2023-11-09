import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.sleep;

public class GameOfLife {
    private Grid grid;
    private boolean isRunning;
    private GameRules rules;


    public void initializeGame(int rows, int cols) {
        grid = new Grid(rows, cols);
        isRunning = false;
    }
    public void startGame() throws InterruptedException {
        if(!isRunning){
            isRunning = true;
            runGenerations();
        }
    }

    public Grid getGrid() {
        return grid;
    }
    public void toggleCell(int row, int col){
        if (!isRunning) {
            grid.toggleCell(row, col);
        }
    }
    public void stopGame() {
        isRunning = false;
    }
    public void setRules(int[] survival, int[] birth) {
        rules = new GameRules();
        rules.setSurvivalRules(survival);
        rules.setBirthRules(birth);
    }
    public GameRules getRules () {
        return rules;
    }

    private void runGenerations() throws InterruptedException {
        while (isRunning) {
            grid.calculateNextGeneration(rules);
            System.out.println("1");
            sleep(500);
            //break;
            // You can add a delay here to control the generation speed.
        }
    }
}
