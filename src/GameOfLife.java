import java.io.Serializable;
public class GameOfLife implements Serializable {
    private Grid grid;
    private boolean isRunning;
    private GameRules rules;

    public void setGrid(Grid grid) { //segéd függvény a FeleIO classhoz
        this.grid = grid;
    }

    public void setRules(GameRules rules) {  //segéd függvény a FeleIO classhoz
        this.rules = rules;
    }


    public void initializeGame(int rows, int cols) {
        grid = new Grid(rows, cols);
        isRunning = false;
    }


    public Grid getGrid() {
        return grid;
    }
    public void toggleCell(int row, int col){
        if (!isRunning) {
            grid.toggleCell(row, col);
        }
    }

    public void setRules(int[] survival, int[] birth) {
        rules = new GameRules();
        rules.setSurvivalRules(survival);
        rules.setBirthRules(birth);
    }
    public GameRules getRules () {
        return rules;
    }


}
