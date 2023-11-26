import java.io.Serializable;
import java.util.ArrayList;

public class Grid implements Serializable {
    private int sor; //1-tol indexel
    private int oszlop; //1-tol indexel
    /**
     * 2 dimenziós tömb helyett listát használunk, könnyebb vele dolgozni (előre elkészített fügvények)
     * */
    private ArrayList<Cell> grid;
    /**
     * Grid konstruktora hanyszor hanyas legyen.
     * */
    public Grid(int rows, int cols) {
        int count = rows*cols;
        sor = rows;
        oszlop = cols;
        grid = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            grid.add(new Cell(false));
        }
    }
    /**
     * @return visszaadja, hogy hány sor van
     * */
    public int getSor() { return sor; }
    /**
     * @return visszaadja, hogy hány oszlop van
     * */
    public int getOszlop() { return oszlop; }

    /**
     * @return visszaadja a listát, amiben tároljuk a gridet
     * */
    public ArrayList<Cell> getList() {
        return grid;
    }
    /**
     * megváltoztatjuk a cella állapotát
     * @param row hanyadik sorban van a cella
     * @param col hanyadik oszlopban van a cella
     * */
    public void toggleCell(int row, int col){
        int hanyadik = row*oszlop+col;
        grid.get(hanyadik).toggleState();
    }
    /**
     * szzomszédok meghatározása
     * @return visszaad egy listát, amiben a szomszédok vannak
     * @param row hanyadik sorban van a cella
     * @param col hanyadik oszlopban van a cella
     *
     * */
    public ArrayList<Cell> getNeighbours(int row, int col) {
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

    /**
     * segéd függvények, hogy indexből megkapjuk a sort
     * @param index hanyadik a listában
     * @return hanyadik sor
     * */
    public int calculateRow(int index){
        int i = index/oszlop;
        return (i);
    }
    /**
     * segéd függvények, hogy indexből megkapjuk a az oszlopot
     * @param index hanyadik a listában
     * @return hanyadik oszlop
     * */
    public int calculateCol(int index){
        int i= (index%oszlop);
        return (i);
    }

    /**
     * következő állapot kiszámítása
     * élő szomszédok meghatározása
     * szabályokkal való összehasonlítás(halott esetben, illetve élőben)
     * változtatások mentése egy listába, ami alapján beállítjuk a az eredeti grid listában a cellák állapotát.
     * */
    public void calculateNextGeneration(GameRules rules) {
        ArrayList<Cell> szomszedok;
        ArrayList<Cell> nextGeneration = new ArrayList<>();

        for (int i = 0; i < grid.size(); i++) {
            szomszedok = getNeighbours(calculateRow(i), calculateCol(i));
            int elo = 0;
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

