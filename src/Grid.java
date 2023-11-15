import java.io.Serializable;
import java.util.ArrayList;

public class Grid implements Serializable {
    private int sor; //1-tol indexel
    private int oszlop; //1-tol indexel
    private ArrayList<Cell> grid;  //2 dimenziós tömb helyett listát használunk, könnyebb vele dolgozni (előre elkészített fügvények)
    public Grid(int rows, int cols) {
        int count = rows*cols;
        sor = rows;
        oszlop = cols;
        grid = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            grid.add(new Cell(false));
        }
    }
    public int getSor() { return sor; }
    public int getOszlop() { return oszlop; }

    public ArrayList<Cell> getList() {
        return grid;
    }
    public void toggleCell(int row, int col){
        int hanyadik = row*oszlop+col;
        grid.get(hanyadik).toggleState();
    }

    public ArrayList<Cell> getNeighbours(int row, int col) {  //szzomszédok meghatározása
        ArrayList<Cell> szomszedok = new ArrayList<>();

        // szomszédok balra és jobbra
        if (col > 0) {
            szomszedok.add(grid.get((row * oszlop) + col - 1));
        }
        if (col < oszlop - 1) {
            szomszedok.add(grid.get((row * oszlop) + col + 1));
        }

        // felső és alsó szomszédok
        if (row > 0) {
            szomszedok.add(grid.get(((row - 1) * oszlop) + col));
            if (col > 0) {
                szomszedok.add(grid.get(((row - 1) * oszlop) + col - 1));
            }
            if (col < oszlop - 1) {
                szomszedok.add(grid.get(((row - 1) * oszlop) + col + 1));
            }
        }
        if (row < sor - 1) {
            szomszedok.add(grid.get(((row + 1) * oszlop) + col));
            if (col > 0) {
                szomszedok.add(grid.get(((row + 1) * oszlop) + col - 1));
            }
            if (col < oszlop - 1) {
                szomszedok.add(grid.get(((row + 1) * oszlop) + col + 1));
            }
        }

        return szomszedok;
    }

    //segéd függvények, hogy indexből megkapjuk a sort és oszlopot
    public int calculateRow(int index){
        int i = index/oszlop;
        return (i);
    }
    public int calculateCol(int index){
        int i= (index%oszlop);
        return (i);
    }
    public void calculateNextGeneration(GameRules rules) {  //következő állapot kiszámítása
        ArrayList<Cell> szomszedok; //ide nem kell new hisz ezt egyelővé fogjuk tenni a getNeighbours return-ével
        ArrayList<Cell> nextGeneration = new ArrayList<>();

        for (int i = 0; i < grid.size(); i++) {
            szomszedok = getNeighbours(calculateRow(i), calculateCol(i));
            int elo = 0;  //élő szomszédok meghatározása
            for (Cell it : szomszedok) {
                if (it.isAlive())
                    elo++;
            }
            boolean nextState = false;
            if (!(grid.get(i).isAlive())) {
                for (int j : rules.getBirthRules()) {
                    if (elo == j) {  //szabályokkal való összehasonlítás(halott eset)
                        nextState = true;
                        break;
                    }
                }
            } else { //grid state true
                for (int rule : rules.getSurvivalRules()) {
                    if (elo == rule) {  //szabályokkal való összehasonlítás (élő eset)
                        nextState = true;
                        break;
                    }
                }
            }
            nextGeneration.add(new Cell(nextState));  //if-elseből kapott értékre eltárolása
        }
        for (int i = 0; i < grid.size(); i++) {  //a cellák értékének beállítása
            grid.get(i).setState(nextGeneration.get(i).isAlive());
        }
    }

}

