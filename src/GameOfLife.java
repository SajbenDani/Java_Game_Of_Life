import java.io.Serializable;
public class GameOfLife implements Serializable {
    private Grid grid;
    private boolean isRunning;
    private GameRules rules;

    /**
     * segéd függvény a FeleIO classhoz
     * @param grid tábla beállítása
     * */
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    /**
     * segéd függvény a FeleIO classhoz
     * @param rules szabályok beállításához
     * */
    public void setRules(GameRules rules) {
        this.rules = rules;
    }


    /**
     * játék inicializálása
     * */
    public void initializeGame(int rows, int cols) {
        grid = new Grid(rows, cols);
        isRunning = false;
    }

    /**
     * @return visszaadja a táblát (gridet)
     * */
    public Grid getGrid() {
        return grid;
    }
    /**
     * megváltoztatja a cella állapotát az ellenkezőre
     * @param row melyik sorban van a cella
     * @param col melyik oszlopban van a cella
     * */
    public void toggleCell(int row, int col){
        if (!isRunning) {
            grid.toggleCell(row, col);
        }
    }
    /**
     * szabályok beállítása
     * @param birth szülletési szabályok
     * @param survival tulélési szabályok
     * */
    public void setRules(int[] survival, int[] birth) {
        rules = new GameRules();
        rules.setSurvivalRules(survival);
        rules.setBirthRules(birth);
    }
    /**
     * @return visszaadja a szabályokat
     * */
    public GameRules getRules () {
        return rules;
    }


}
